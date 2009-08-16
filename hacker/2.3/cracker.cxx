#include "glob.h"
#include "cracker.h"
#include "mod_ftp.h"
#include "mod_pop3.h"
#include <windows.h>
#include <winsock.h>
#include <cstdio>
#include <deque>
#include <vector>
#include <string>

using std::deque;
using std::string;
using std::vector;

char target_addr[256];
FILE *users_file, *pwd_file;
vector <HANDLE> threads;            //存放线程句柄
vector <_module_param> thread_info; //存放线程信息
DWORD WINAPI (*_crack_pro)(PVOID param);

int check_args(int argc, char *args[]){
    if (argc < 3) {
        printf("[i]usage: %s [protocol] [host ip]\n", args[0]);
        puts("protocol:");
        puts("\t-pop3    pop3 protocol.");
        puts("\t-ftp     ftp protocol.");
        exit(-1);
    } else {
        if (!strcmp(args[1], "-pop3")){
            _crack_pro = mod_pop3_run;
        } else if (!strcmp(args[1], "-ftp")){
            _crack_pro = mod_ftp_run;
        } else {
            exit(-1);
        }
        strcpy(target_addr, args[2]);
    }
    return 0;
}

//初始化
void init_cracker() {
    WSADATA wsd;
    WSAStartup(MAKEWORD(2, 2), &wsd);
    users_file = fopen(USERS_FILE, "r");    //用户名
    pwd_file = fopen(PWD_FILE,  "r");       //密码

    int queue_id = 0;
    unsigned long ID;
    struct _module_param t_param;
    HANDLE t_handle, q_handle;  
    
    printf("[i]Create Threads");            //创建线程, 数量为用户名的个数
    while (EOF != fscanf(users_file, "%s", t_param.user_name)){
        //充填_module_param结构体
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
    for (vector <_module_param>::iterator i = thread_info.begin(); i != thread_info.end(); ++i){
        DeleteCriticalSection(i->critical_sec);
        delete i->critical_sec;
        delete i->queue;
    }
    //关闭线程句柄
    for (vector <HANDLE>::iterator i = threads.begin(); i != threads.end(); ++i){
        CloseHandle(*i);
    }
    WSACleanup();
    printf("[i]Exit.\n");
}

void hold_cracker(){
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
        for (vector <_module_param>::iterator i = thread_info.begin(); i != thread_info.end(); ++i){
            EnterCriticalSection(i->critical_sec);
            i->queue->push_back(tmp);   
            LeaveCriticalSection(i->critical_sec);
        }
    }
    //没有密码了, 将结束标识压入队列
    for (vector <_module_param>::iterator i = thread_info.begin(); i != thread_info.end(); ++i){
        EnterCriticalSection(i->critical_sec);
        i->queue->push_back("{$END_LABLE$}"); 
        LeaveCriticalSection(i->critical_sec);
    }
    return 0;
}




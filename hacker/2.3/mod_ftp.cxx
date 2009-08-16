#include "mod_ftp.h"
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
DWORD WINAPI mod_ftp_run(PVOID param){
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
            printf("[ftp]thread %d receives a END msg, EXIT thread.\n", t_param.id);
            break;
        }

        logon_state = mod_ftp_check(&t_param, buf.c_str());

		if (logon_state == 2){ //返回登录成功, 输出
			printf("[ftp]try %16s on %16s   success!\n", buf.c_str(), t_param.user_name);
            printf("[ftp]thread %d has got a password for <%s>, EXIT thread.\n", t_param.id, t_param.user_name);
            break;
        }else if (logon_state == 1){ //登录时失败了?
			printf("[ftp]logon user %16s fail!\n", t_param.user_name);
		}else {            //密码和用户名不匹配, 也输出
			printf("[ftp]try %16s on %16s   fail!\n", buf.c_str(), t_param.user_name);
        }
    }
    return 0;
}



int mod_ftp_check(struct _module_param *param, const char *str_pass){

    int logon_state = 0, ret = 0;
    char send_buff[256] = {0};
    char recv_buff[256] = {0};
    struct sockaddr_in target = {0};
    target.sin_family = AF_INET; 
    target.sin_port = htons(21);
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
    sprintf(send_buff, "USER %s\n", param->user_name);
    ret = send(sock, send_buff, strlen(send_buff), 0);
    ret = recv(sock, recv_buff, 256, 0);
    if (3 < ret && recv_buff[0] == '3' & recv_buff[1] == '3' && recv_buff[2] == '1'){
        //copy密码到send_buff
        sprintf(send_buff, "PASS %s\n", str_pass);
        ret = send(sock, send_buff, strlen(send_buff), 0);
        ret = recv(sock, recv_buff, 256, 0);
        if (3 < ret && recv_buff[0] == '2' & recv_buff[1] == '3' && recv_buff[2] == '0'){
            logon_state = 2;
        }
    } else {
        logon_state = 1;
    }
    send(sock, "quit\n", 5, 0);
    closesocket(sock);
    return logon_state;
}


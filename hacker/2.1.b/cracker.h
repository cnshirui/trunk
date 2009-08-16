#ifndef __CRACKER_H__
#define __CRACKER_H__

#include <windows.h>

#define USERS_FILE  "user.in"
#define PWD_FILE    "pwd.in"
#define MAX_QUEUE_SIZE  2048 

void init_cracker();
void free_cracker();
void crack_pwd(void);
DWORD WINAPI process_queue(LPVOID param);
DWORD WINAPI _crack_pro(LPVOID param);


#endif


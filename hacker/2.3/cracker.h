#ifndef __CRACKER_INC__
#define __CRACKER_INC__

#include <windows.h>

int check_args(int argc, char *args[]);
void init_cracker();
void free_cracker();
void hold_cracker(void);
DWORD WINAPI process_queue(LPVOID param);


#endif


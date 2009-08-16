#ifndef __LOADER_H__
#define __LOADER_H__
int load_dll(DWORD PID, char *lib_name);
int free_dll(DWORD PID, char *lib_name);
int promote_privilege(int enable);
unsigned long name2processID(char *img_name);

#endif

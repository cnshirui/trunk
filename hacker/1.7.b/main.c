#include <windows.h>
#include <stdio.h>
#include "loader.h"

int main()
{
	unsigned long pid = 0, i =0;
	char buff[256] = {0};
	char current_path[MAX_PATH] = {0};
	char dll_path[MAX_PATH + 16];

	GetCurrentDirectory(MAX_PATH, current_path);
	sprintf(dll_path, "%s\\test.dll", current_path);

	scanf("%s", buff);
	promote_privilege(1);
	pid = name2processID(buff);
	printf("[i]PID of %s is %d.\n", buff, pid);
	printf("[i]inject DLL to %s.\n", buff);
	inject_dll(pid, dll_path);
	
    char c;
    while (EOF != scanf("%c", &c));

	printf("[i]free DLL in %s.\n", buff);
	free_dll(pid, dll_path);
	promote_privilege(0);
	return 0;
}



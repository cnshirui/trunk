/* Replace "dll.h" with the name of your header */
#include "door.h"
#include <windows.h>
#include <stdio.h>
#include <stdlib.h>
HANDLE thread_start;
DWORD ID;

//=======================================================================

BOOL APIENTRY DllMain (HINSTANCE hInst     /* Library instance handle. */ ,
                       DWORD reason        /* Reason this function is being called. */ ,
                       LPVOID reserved     /* Not used. */ )
{
    switch (reason) {
    case DLL_PROCESS_ATTACH:{
        thread_start = CreateThread(NULL, 0, start, (LPVOID)NULL, 0, &ID);
    }
    break;

    case DLL_PROCESS_DETACH:
    break;

    case DLL_THREAD_ATTACH:
	break;

    case DLL_THREAD_DETACH:
    break;
    default:
    break;
    }

    /* Returns TRUE on success, FALSE on failure */
    return TRUE;
}

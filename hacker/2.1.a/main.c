#include <stdio.h>
#include <string.h>
#include <windows.h>
CRITICAL_SECTION cs;
char share[64] = "ABCDEFGHIJKLMN";

DWORD WINAPI _thread_pro1(LPVOID str) {
    while (1) {
        EnterCriticalSection(&cs);

        strcpy(share, (char *)str);
        Sleep(100);
        printf("[1] %s\n", share);

        LeaveCriticalSection(&cs);
    }
    return 0;
}

DWORD WINAPI _thread_pro2(LPVOID str) {
    while (1) {
        EnterCriticalSection(&cs);

        strcpy(share, (char *)str);
        Sleep(100);
        printf("[2] %s\n", share);

        LeaveCriticalSection(&cs);
    }
    return 0;
}


int main() {
    InitializeCriticalSection(&cs);

    char *str1 = "Give you some colour see see";
    char *str2 = "What the fuck did you do?";

    HANDLE handle[2];
    unsigned long ID;

    handle[0] = CreateThread(NULL, 0, _thread_pro1, (LPVOID)str1, 0, &ID);
    Sleep(300);
    handle[1] = CreateThread(NULL, 0, _thread_pro2, (LPVOID)str2, 0, &ID);
    
    WaitForMultipleObjects(2, handle, TRUE,  10000);

    DeleteCriticalSection(&cs);
    return 0;
}

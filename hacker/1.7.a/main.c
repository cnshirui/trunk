#include <windows.h>
#include <stdio.h>

int main(int argc, char argv[])
{
    HINSTANCE dll_hinst = LoadLibrary("door.dll");
    FARPROC func = GetProcAddress(dll_hinst, "say_something");
    func("I am Here.");
    FreeLibrary(dll_hinst);
	return 0;
}


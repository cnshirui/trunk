#ifndef _DLL_H_
#define _DLL_H_
#include <windows.h>

#define DLLIMPORT __declspec (dllexport)


DLLIMPORT int say_something(char *sth);
#endif /* _DLL_H_ */

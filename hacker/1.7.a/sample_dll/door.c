#include <windows.h>
#include "door.h"

//=======================================================================

int say_something(char *sth) {
    MessageBox(NULL, sth, "Hello!", 0);
	return 0;
}


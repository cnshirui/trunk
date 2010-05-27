// LokiTest.cpp : Defines the entry point for the console application.
//

#include <tchar.h>

#include "static_check.h"

#include <iostream>

using namespace std;


int _tmain(int argc, _TCHAR* argv[])
{
	cout << sizeof(void*) << endl;
	cout << sizeof(char) << endl;

	return 0;
}


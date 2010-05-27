#include <stdio.h>
#include <iostream>

using namespace std;

extern "C" {
	
};

void print_lang() 
{
#if defined(__cplusplus)
	cout << "cpp" << endl;
#else
	printf("c lang\n");
#endif
}

void main() {
}
#include <iostream.h>

union test
{
	char a;
	struct 
	{
		char b;
		char c;
	};
};

void main()
{
	union test	d = {	(char)0x55	};
	cout<<d.a<<endl
		<<d.b<<endl
		<<d.c<<endl;
}
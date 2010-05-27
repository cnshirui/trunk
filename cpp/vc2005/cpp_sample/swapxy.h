#include <iostream.h>

void swapxy(char &a, char &b)
{
	int x=a, y=b;
	x = x+y;
	y = x-y;
	x = x-y;
	a = x,	b = y;
}

void main()
{
	char a='a', b='b';
	char &x=a, &y=b;
	cout<<a<<endl
		<<b<<endl;
	swapxy(x, y);
	cout<<a<<endl
		<<b<<endl;
}
#include <iostream.h>

void foo(int p[])
{
	*p += 5;
}


void bar(int p[])
{
	p[1] = 15;
}


void main()
{
	int a[] = {3,4,5};
	int b[] = {3,4,5};
	int *p;

	p = &a[1];
	bar(p);
	cout<<a[0]<<" "<<a[1]<<" "<<a[2]<<" "<<endl;

	p = &b[0];
	p++;
	foo(p);
	bar(p);
	cout<<b[0]<<" "<<b[1]<<" "<<b[2]<<" "<<endl;
}

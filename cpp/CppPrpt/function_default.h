#include <iostream>
using namespace std;

// default value must be in declare
int foo(int x=0, int y=0)
{
	return x+y;
}

void main()
{
	cout<<foo()<<endl;
	cout<<foo(5)<<endl;
	cout<<foo(2,4)<<endl;
}
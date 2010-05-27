#include <iostream.h>

void Foo(int x=0, int y=0);	// 正确，缺省值出现在函数的声明中

void Foo(int x=0, int y=0)		// 错误，缺省值出现在函数的定义体中, but vc6 passed
{
}

void fn(const char *a)
{
	cout<<"const"<<endl;
	cout<<a<<endl;
}

void fn(char *a)				// vc6 匹配的是这个
{
	cout<<"null"<<endl;
	cout<<a<<endl;
}


void main()
{
	fn("shirui");
}

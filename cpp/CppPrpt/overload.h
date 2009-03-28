#include <iostream.h>
void output( int x);	// 函数声明
void output( float x);	// 函数声明

void output( int x)
{
	cout << " output int " << x << endl ;
}

void output( float x)
{
	cout << " output float " << x << endl ;
}

void main(void)
{
	int   x = 1;
	float y = 1.0;
	output(x);			// output int 1
	output(y);			// output float 1
	output(1);			// output int 1
//	output(0.5);		// error! ambiguous call to overloaded, 因为自动类型转换
	output(int(0.5));	// output int 0
	output(float(0.5));	// output float 0.5
}

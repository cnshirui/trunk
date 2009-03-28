#include <iostream.h>

class A	{};
class A2 {};
class B: public A	{};
class C: public virtual B {};
class D: public A, public A2	{};


void main()
{
	cout<<"sizeof(A): "<<sizeof(A)<<endl;
	cout<<"sizeof(B): "<<sizeof(B)<<endl;
	cout<<"sizeof(C): "<<sizeof(C)<<endl;
	cout<<"sizeof(D): "<<sizeof(D)<<endl;
}
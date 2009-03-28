#include <iostream.h>

class A
{
public:
	A()
	{
		cout<<"in A's constructor..."<<endl;
		this->fn();
	}
	virtual ~A()
	{
		cout<<"in A's destructor..."<<endl;
	}
	virtual void fn()
	{
		cout<<"in A's fn()"<<endl;
	}
};

class B: public A
{
public:
	B()
	{
		cout<<"in B's constructor..."<<endl;
	}
	void fn()
	{
		cout<<"in B's fn()"<<endl;
	}
};

void main()
{
	A* pa = new B;
	delete pa;
}
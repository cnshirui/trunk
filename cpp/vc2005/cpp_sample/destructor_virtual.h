#include <iostream.h>

class Base
{
  public: 
	virtual ~Base() { cout<< "~Base" << endl ; }		// 为什么这样会被自动调用
};

class Derived : public Base
{
  public: 
	virtual ~Derived() { cout<< "~Derived" << endl ; }
};

void main(void)
{
	Base * pB = new Derived;  // upcast
	delete pB;
}

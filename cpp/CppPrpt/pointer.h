#include <iostream>
using namespace std;

class A 
{	
public:
	void Func(void){ cout << "Func of class A" << endl; }
};

void main(void)
{
	A  *p;
		{
			A  a;
			p = &a;	// 注意 a 的生命期
		}
		p->Func();		// p是"野指针",VC6运行正常。可能途中未被修改
}
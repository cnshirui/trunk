#include <iostream>
using namespace std;

class A
{
private:
	int value;
public:
	void setValue(int i)
	{
		value = i;
	}

	int getValue() const
	{
		for(int i=0; i<100; i++)			// local variable can be changed
//			cout<<"getValue()"<<endl;
		return value;
	}
};

void main()
{
	A a;
	a.setValue(5);
	cout<<a.getValue()<<endl;
}
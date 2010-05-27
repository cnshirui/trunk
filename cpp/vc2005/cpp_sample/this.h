#include <iostream.h>

class Point
{
public:
	Point()
	{
		cout<<this<<endl;
		cout<<this+1<<endl;
		cout<<this-1<<endl;
	}
};

void main()
{
	Point a;
	cout<<&a<<endl;
}
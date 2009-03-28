#include <iostream.h>

void main()
{
	static int a[3][3] = {1,3,5,7,9,11,13,15,17};
	int x, y = 0, *p = &a[2][2];
	for (x=0; x<3; x++)
	{
		y += *(p-4*x);
		cout<<"p: "<<p<<endl
			<<"4*x: "<<4*x<<endlm
			<<"p-4*x: "<<p-4*x<<endl
			<<*(p-4*x)<<endl;
	}
	cout<<y<<endl;
}
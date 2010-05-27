#include <iostream>

using namespace std;

#define POW(c) (1<<(c))
#define MASK(c) (((unsigned long)-1) / (POW(POW(c)) + 1))

void main_main()
{
	cout<<(unsigned long)-1<<endl;		// 32¸ö1
	for(int i=0; i<5; i++)
	{
		cout<<hex<<MASK(i)<<endl;
	}
	cout<<0xff/(2+1)<<endl
		<<0xff/(4+1)<<endl
		<<0xff/(16+1)<<endl;
	if(((unsigned long)-1)==0xffffffff)
		cout<<"hello"<<endl;
}

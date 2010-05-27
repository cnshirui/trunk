#include <iostream>
using namespace std;

const bufsize = 5;

void main()
{
//  P35:求两个数的最大者
	int a=3,b=4;
	int c = a - b;
	char* strs[2] = {"a bigger", "b bigger"};
	c = unsigned(c)>>(sizeof(int)*8-1);
	cout<<strs[c]<<endl;
}
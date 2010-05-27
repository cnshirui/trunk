#include <iostream>
using namespace std;

void main()
{
	char *p = (char *) malloc(100);
	strcpy(p, "hello");
	free(p);	    // p 所指的内存被释放，但是p所指的地址仍然不变

//	p = NULL;

	if(p != NULL)	// 没有起到防错作用,但VC6却运行了此句
	{
	   strcpy(p, "world");	// 出错
	}
	cout<<p<<endl;
}

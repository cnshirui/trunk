#include <iostream>
using namespace std;

void main()
{
//  P37:如果需要加入一段在main退出后执行的代码，可以使用atexit()函数注册一个函数
	atexit(fn);
	cout<<"end main()..."<<endl;
}

void fn()
{
	cout<<"in function fn()..."<<endl;
}

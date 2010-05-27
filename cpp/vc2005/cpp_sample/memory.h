//memory.cpp

#include <stdlib.h>
#include <string.h>
#include <iostream.h>

void GetMemory(char *p, int num)
{
	p = (char *)malloc(sizeof(char) * num);
}

void GetMemory2(char **p, int num)
{
	*p = (char *)malloc(sizeof(char) * num);
}

char *GetMemory3(int num)
{
	char *p = (char *)malloc(sizeof(char) * num);
	return p;
}

char *GetString(void)
{
	char p[] = "hello world";
	return p;	// 编译器将提出警告
}

char *GetString2(void)
{
	char *p = "hello world";
	return p;
}

void main(void)
{
	char *str = NULL;

//	GetMemory(str, 100);	// str 仍然为 NULL	
//	strcpy(str, "hello");	// 运行错误
	//编译器总是要为函数的每个参数制作临时副本，指针参数p的副本是 _p，
	//编译器使 _p = p。如果函数体内的程序修改了_p的内容，
	//就导致参数p的内容作相应的修改。这就是指针可以用作输出参数的原因。
	//在本例中，_p申请了新的内存，只是把_p所指的内存地址改变了，但是p丝毫未变。

/*	GetMemory2(&str, 100);	// 注意参数是 &str，而不是str，成功!
	strcpy(str, "hello");	
	cout<<str<<endl;
	free(str);	
*/

/*	str = GetMemory3(100);		//用函数返回值来传递动态内存,成功!
	strcpy(str, "hello");
	cout<< str << endl;
	free(str);	
*/

//	str = GetString();			// str 的内容是垃圾, error!
//	cout<< str << endl;

	str = GetString2();
	cout<< str << endl;		//函数Test5运行虽然不会出错，但设计概念却是错误的。
	//因为GetString2内的"hello world"是常量字符串，位于静态存储区，
	//它在程序生命期内恒定不变。无论什么时候调用GetString2，
	//它返回的始终是同一个"只读"的内存块。
}
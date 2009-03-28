#include <iostream>
#include <stdlib.h>
using namespace std;

void main()
{
	bool is_num = false;
	try
	{
		if(is_num)
			throw 10;
		else
			throw "abc";
	}
	catch (int k)
	{
		cout<<"int k: "<<k<<endl;
	}
	catch (char* pstr)
	{
		cout<<"char* pstr: "<<pstr<<endl;
	}

}
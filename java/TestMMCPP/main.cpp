#include <iostream>
using namespace std;

void outOfMem();

int main()
{
	std::set_new_handler(outOfMem);
	int* pBigDataArray;
	pBigDataArray = new int[100000000L];
	cout<<"allocate over..."<<endl;
	return 1;
}

void outOfMem()
{
	cerr<<"operator new error!"<<endl;
	abort();
}

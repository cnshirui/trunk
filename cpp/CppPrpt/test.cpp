#include <iostream>
using namespace std;

void outOfMem();


int main()
{
	set_new_handler(outOfMem);
	int* pBigDataArray;
	for(int i=0; i<100000000L; i++)
		pBigDataArray = new int[100000000L];
	cout<<"allocate over..."<<endl;
	return 1;
}

void outOfMem()
{
	cerr<<"Unable to satisfy request for memory!"<<endl;
	abort();
}

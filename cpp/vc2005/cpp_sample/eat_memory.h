#include <iostream>
using namespace std;

void main(void)
{
	float *p = NULL;
	while(true)
	{
		p = new float[1000000];	
		cout << "eat memory" << endl;
		if(p==NULL)
			exit(1);
	}
}

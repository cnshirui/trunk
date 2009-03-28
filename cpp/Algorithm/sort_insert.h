#include <iostream>
using namespace std;

void insertsort(int data[], int n)
{
	int i,j,k,t;
	for(i=1; i<n; i++)
	{
		for(j=0; j<i; j++)
		{
			if(data[i]<data[j])
				break;
		}
		t = data[i];
		for(k=i; k>j; k--)			// should be back to front
			data[k] = data[k-1];
		data[j] = t;
		for(k=0; k<n; k++)			// demostration
			cout<<data[k]<<" ";
		cout<<endl;
	}
}



void main()
{
	int data[] = {5,9,8,6,7,1,3,2,4,0};
	insertsort(data,10);

	for (int i=0; i<10; i++)
		cout<<data[i]<<" ";
	cout<<endl;
}	

#include <iostream>
using namespace std;

void bubblesort(int data[], int n)
{
	int i,j,tag,temp;
	for(i=0,tag=1; tag==1&&i<n-1; i++)
	{
		tag = 0;		//满足顺序条件则停止比较
		for(j=0; j<n-1-i; j++)
		{
			if(data[j]>data[j+1])
			{
				temp = data[j];
				data[j] = data[j+1];
				data[j+1] = temp;
				tag = 1;
			}
		}
	}
}

void main()
{
	int data[] = {5,9,8,6,7,1,3,2,4,0};
	bubblesort(data,10);

	for (int i=0; i<10; i++)
		cout<<data[i]<<" ";
	cout<<endl;
}	

#include <iostream>
using namespace std;

void shellsort(int data[], int n)
{
	int *delta,k,i,t,dk,j;
	k = n;					// k = n,n/2 to 0, all k's are stored into delta
	delta = (int*)malloc(sizeof(int)*(n/2));
	i = 0;
	do
	{
		k = k/2;
		delta[i++] = k;
	}while(k>0);
	
	i = 0;
	while(dk=delta[i]>0)
	{
		for(k=delta[i]; k<n; ++k)
		{
			if(data[k]<data[k-dk])
			{
				t = data[k];		// t means temp
				for(j=k-dk; j>=0&&t<data[j]; j-=dk)
					data[j+dk] = data[j];
				data[j+dk] = t;
			}
		}
		i++;
	}
}

void main()
{
	int data[] = {5,9,8,6,7,1,3,2,4,0};
	shellsort(data,10);

	for (int i=0; i<10; i++)
		cout<<data[i]<<" ";
	cout<<endl;
}	

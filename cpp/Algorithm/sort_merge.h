#include <iostream>
using namespace std;

int data[] = {5,9,8,6,7,1,3,2,4,0};
int ii;

// 将分别有序的data[s..m]和data[m+1..n]归并为有序的temp[s..n]
void merge(int data[], int s, int m, int n)
{
	int i,j,k;
	int first = s;
	int size = n-s+1;
//	int *temp = (int*)malloc(sizeof(int)*size);
	int *temp = new int[size];
	for(i=m+1,k=0; s<=m && i<=n; k++)
	{
		if(data[s]<data[i])
			temp[k] = data[s++];
		else
			temp[k] = data[i++];
	}
	for(j=s; j<=m; j++,k++)					// leaving data[s..m]
		temp[k] = data[j];

	for(j=i; j<=n; j++,k++)					// leaving data[i..n]
		temp[k] = data[j];

	for(k=0; k<size; k++)
		data[first+k] = temp[k];

	for(ii=0; ii<10; ii++)					/// demostration
		cout<<data[ii]<<" ";
	cout<<endl;

	delete []temp;
}

void mergesort(int data[], int first, int last)
{
	if(first<last)
	{
		int mid = (first+last)/2;
		mergesort(data, first, mid);
		mergesort(data, mid+1, last);
		merge(data, first, mid, last);
	}
}


void main()
{
	mergesort(data,0,9);

	for (int i=0; i<10; i++)
		cout<<data[i]<<" ";
	cout<<endl;
}	

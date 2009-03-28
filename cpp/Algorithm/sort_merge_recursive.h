#include <iostream>
using namespace std;

// 未调试通过

// 将分别有序的data[s..m]和data[m+1..n]归并为有序的temp[s..n]
void merge(int data[], int s, int m, int n)
{
	cout<<" s:"<<s<<" m:"<<m<<" n :"<<n<<endl;		///
	int i,j,k;
	int *temp = new int[n-s+1];
	for(i=m+1,k=s; s<=m && i<=n; k++)
		if(data[s]<data[i])
			temp[k] = data[s++];
		else
			temp[k] = data[i++];
	for(j=s; j<=m; j++,k++)
		temp[k] = data[j];
	for(j=i; j<=n; j++,k++)
		temp[k] = data[j];

	for(k=0; k<n-s+1; k++)
		data[s+k] = temp[k];
	delete []temp;
}

void mergesort(int data[], int first, int last)
{
	cout<<"first: "<<first<<"	last:"<<last<<endl;
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
	int data[] = {5,9,8,6,7,1,3,2,4,0};
	mergesort(data,0,9);

	for (int i=0; i<10; i++)
		cout<<data[i]<<" ";
	cout<<endl;
}	

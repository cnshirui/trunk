#include <iostream>
using namespace std;

//∑«µ›ºı≈≈–Ú
void quicksort(int data[], int low, int high)
{
	int i,j,pivot;
	if (low<high)
	{
		pivot = data[low];
		i = low;
		j = high;
		while (i<j)
		{
			while(i<j && data[j]>=pivot)
				j--;
			if(i<j)
				data[i++] = data[j];
			while(i<j && data[i]<=pivot)
				i++;
			if(i<j)
				data[j--] = data[i];
		}
		data[i] = pivot;
		quicksort(data,low,i-1);
		quicksort(data,i+1,high);
	}
}

void main()
{
	int data[] = {5,9,8,6,7,1,3,2,4,0};
	quicksort(data,0,9);

	for (int i=0; i<10; i++)
		cout<<data[i]<<" ";
	cout<<endl;
}	
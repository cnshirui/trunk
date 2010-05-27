#include <iostream.h>

// maximum consecutive subsequence
int mcs(int arr[], int n, int &begin, int &end)
{
	int sum=0, b=0;
	begin = end = 0;
//	int* suffix = new int[n];	
	for(int i=0; i<n; i++)
	{
		if(b>0)
		{
			b += arr[i];
		}
		else
		{
			b = arr[i];
			begin = i;			// begin
		}
//		suffix[i] = b;
		if(b>sum)
		{
			sum = b;
			end = i;			// end
		}
	}
//	for(i=0; i<n; i++)
//		cout<<suffix[i]<<" ";
//	cout<<endl;
	return sum;
}

void main()
{
	int arr[10] = { -2,11,-4,13,-5,-2,0};
	int begin,end;
	cout<<mcs(arr,7,begin,end)<<endl;
	cout<<begin<<endl
		<<end<<endl;
}
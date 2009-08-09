#include <iostream.h>

int* input(int dig[])
{
	int num;
	bool test[4],same;
	while(true)
	{
		cout<<"Please input four different number, such as \"1234\""<<endl;
		cin>>num;
		if(num<0123 || num>9876)
		{
			cout<<"illegal number!"<<endl;
			continue;
		}
		for(int i=3; i>=0; i--)
		{
			dig[i] = num%10;
			num /= 10;
		}
	    for(i=0; i<4; i++)							// judge whether they are same
			test[i] = false;
		same=false;
	    for(i=0; i<4; i++)						
		{
			if(test[dig[i]] == true)
			{
				cout<<"there are different number"<<endl;
				same = true;
			}
			else
				test[dig[i]] = true;
		}
		if(same == true)
			continue;
	}
			

}


bool calc(int a[], int b[], int n)
{
	int x=0, y=0;
	for(int i=0; i<n; i++)
	{
		for(int j=0; i<n; j++)
		{
			if(a[i]==b[j])
			{
				if(i==j)
					x++;
				else
					y++;
			}
		}
	}
	cout<<x<<"A"<<y<<"B"<<endl;		// A位与数皆一致，B数一致位不一致
	if(x==n)				// n numbers are all right;
		return true;
	else
		return false;
}



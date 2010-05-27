//	请用C或C++语言编写一个程序计算在m和n之间的质数之和。（m和n为输入, m<n）
#include <iostream.h>
#include <math.h>
#include <assert.h>

int sum_prime(int m, int n)
{
	assert((m>1)&&(n>m));			// 素数从2开始,且m<n
	int i, j, root, sum=0;
	bool isprime = true;

	for(i=m; i<=n; i++)				// 判断i是否是素数,从m到n
	{
		isprime = true;
		root = (int)sqrt(i);
		for(j=2; j<=root; j++)		// 判断j是否是i的因子,从2到sqrt(i)
		{
			if((i%j)==0)			// i若有因子
			{
				isprime = false;
				break;
			}
		}
		if(isprime)					//	i为素数
		{
			sum += i;
			cout<<i<<endl;
		}
	}
	return sum;
}

void main()
{
	cout<<sum_prime(2,100)<<endl;
}

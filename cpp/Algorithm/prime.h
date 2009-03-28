#include <math.h>
#include <stdio.h>


void main()
{
	int i, j, k, count=0;
	for (i=0; i<100; i++)
	{
		k = (int)sqrt(i);
		for (j=2; j<=k; j++)
		{
			if (i%j == 0)
				break;
		}
		if (j>k)
		{
			printf("%5d\n", i);
			count++;
		}
	}
	printf("%d\n", count);
}
#include <cstdio>

int max(int x, int y)
{
	return x>y?x:y;
}

void main()
{
	int max(int, int);
	int (*p)(int, int) = &max;
	int d;
	d = (*p)((*p)(4, 5), 7);
	printf("%d...\n", d);
}
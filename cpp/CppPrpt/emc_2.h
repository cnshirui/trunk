#include <stdio.h>

int p(int i, int N)
{
     return (printf("%d*\n", i))
       && ( i<N
          && (   p(i+1, N)
                  || (!printf("%d\n", i))));
}

int main(void)
{
     p(1,7);
}
#include <stdio.h>
#include <stdlib.h>

void main()
{
  FILE *fp;
  int i;

  if((fp=fopen("c:\\test.txt","w+"))==NULL)
  {
    printf("Cannot open file strike any key exit!");
    exit(1);
  }

  for(i=1; i<=50; i++)
	  fprintf(fp,"%d.\n",i);

  printf("over...\n");

  fclose(fp);

}

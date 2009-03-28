//打靶问题：一共十环，连开十枪打中90环的可能性有多少种。用递归实现。
//target_shot.cpp

#include <cstdio>
#include <cstdlib>
#include <iostream.h>

int sum = 0;
int store[10];
FILE *fp;


// score: 还需的分数，num+1: 还剩的次数
void cumput(int score, int num)
{
	if (score<0 || score>(num+1)*10)
		return;
	if (num == 0)
	{
		store[num] = score;
		for (int i=9; i>=0; --i)
		{
			cout<<store[i]<<" ";
            fprintf(fp,"%d ",store[i]);
		}
		cout<<endl;
        fprintf(fp,"\n");
		sum++;
	}
	else
	{
		for (int i=0; i<=10; i++)
		{
			store[num] = i;
			cumput(score-i, num-1);
		}
	}
}

void main()
{
  if((fp=fopen("c:\\test.txt","w+"))==NULL)
  {
    printf("Cannot open file strike any key exit!");
    exit(1);
  }

  cumput(90, 9);
  cout<<sum<<endl;

  printf("over...\n");
  fclose(fp);
}

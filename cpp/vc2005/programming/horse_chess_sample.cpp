#include <iostream>
using namespace std;

struct info {int x,y,out;};
const int dx[8]={-2,-2,-1,-1, 1, 1, 2, 2};
const int dy[8]={-1, 1,-2, 2,-2, 2,-1, 1};
const int R=8,C=8;
int board[R][C];

int outlet(int x,int y)
{
	int ct=0;
	for(int i=0;i<8;++i)
		if(x+dx[i]<0||y+dy[i]<0||x+dx[i]>=R||y+dy[i]>=C||board[x+dx[i]][y+dy[i]]) 
			continue;
		else 
			++ct;
		return ct;
}//计算(x,y)的出口数

void sort(info *p,int n)
{
	for(int i=n-1;i>0;--i)
		if(p[i].out<p[i-1].out) 
			swap(p[i],p[i-1]);
		else 
			break;
}//按出口数由小到大排序

bool search(int x,int y,int step)
{
	if(board[x][y]) return false;
	if(step==R*C)
	{
		board[x][y]=step;
		return true;
	}
	else
	{
		board[x][y]=step;
		int i,j; info dir[8];
		for(i=j=0;i<8;++i)
			if(x+dx[i]<0||y+dy[i]<0||x+dx[i]>=R||y+dy[i]>=C||board[x+dx[i]][y+dy[i]]) 
				continue;
			else
			{
				dir[j].x=x+dx[i];dir[j].y=y+dy[i];
				dir[j].out=outlet(dir[j].x,dir[j].y);
				sort(dir,++j);
			}
			for(i=0;i<j;++i)
				if(search(dir[i].x,dir[i].y,step+1)) return true;
			board[x][y]=0;
			return false;
	}
}//求解

int main_chess() 
{
	int i,j,m,n;
	for(i=0;i<R;++i)
		for(j=0;j<C;++j)
		{
			memset(board,0,R*C*sizeof(int));
			if(search(i,j,1))
			{                
				printf("start at(%d,%d):\n",i+1,j+1);
				for(m=0;m<R;++m)
				{
					for(n=0;n<C;++n)
						printf("%4d",board[m][n]);
					printf("\n\n");
				}
			}
			else
				printf("start at(%d,%d) has no solve!\n",i+1,j+1);
		}    
		system("pause"); 
		return 0; 
} 
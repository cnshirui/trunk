#include <iostream>
using namespace std;

static char Queen[8][8];
static int a[8];
static int b[15];
static int c[15];
static int iQueenNum = 0;	//记录总的棋盘状态数

void qu(int i);		// line i

void main()
{
	int iLine,iColumn;

	// initialization, * means blank, @ means a queen
	for (iLine=0; iLine<8; iLine++)
	{
		a[iLine] = 0;	//列标记初始化，表示没有列冲突
		for (iColumn=0; iColumn<8; iColumn++)
			Queen[iLine][iColumn] = '*';
	}

	// 主从对角线标记初始化，表示没有冲突
	for (iLine=0; iLine<15; iLine++)
		b[iLine] = c[iLine] = 0;

	qu(0);
}

void qu(int i)
{
	int iColumn;
	for (iColumn=0; iColumn<8; iColumn++)
	{
		if (a[iColumn]==0 && b[i-iColumn+7]==0 && c[i+iColumn]==0)
		{
			Queen[i][iColumn] = '@';
			a[iColumn] = 1;				//a[0]-a[7]
			b[i-iColumn+7] = 1;			//b[0]-b[14]
			c[i+iColumn] = 1;			//c[0]-c[14]
			
			if(i<7)
				qu(i+1);
			else
			{
				// output result
				int iLine,iColumn;
				cout<<"第"<<++iQueenNum<<"种状态为:"<<endl;
				for (iLine=0; iLine<8; iLine++)
				{
					for (iColumn=0; iColumn<8; iColumn++)
						cout<<Queen[iLine][iColumn]<<" ";
					cout<<endl;
				}
				cout<<endl;
			}

			// 如果前次的皇后放置导致后面的放置无论如何都不能满足要求，则回溯，重置
			Queen[i][iColumn] = '*';
			a[iColumn] = 0;
			b[i-iColumn+7] = 0;
			c[i+iColumn] =0;
		}
	}
}
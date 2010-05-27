#include <iostream>
using namespace std;

void heapadjust(int data[], int s, int m)
{
	// data[s..m]所构成的一个元素序列中，除了data[s]外，其他元素均满足堆的定义
	// 调整data[s]的位置，使data[s..m]成为一个大根堆
	int t,j;
	t = data[s];				// backup
	for(j=s*2+1; j<m; j=j*2+1)	// 沿值较大的子女节点向下筛选
	{
		if(j<m && data[j]<data[j+1])
			j++;				// j是值较大的元素的下标
		if(!(t<data[j]))
			break;
		data[s] = data[j];
		s = j;					// 用s记录待插入元素的位置
	}
	data[s] = t;
}

void heapsort(int data[], int n)
{
	register int i;
	int t;
	for(i=n/2-1; i>=0; i--)
		heapadjust(data,i,n-1);
	for(i=n-1; i>0; i--)
	{
		t = data[0];
		data[0] = data[i];
		data[i] = t;
		heapadjust(data,0,i-1);
	}
}




void main()
{
	int data[] = {5,9,8,6,7,1,3,2,4,0};
	heapsort(data,10);

	for (int i=0; i<10; i++)
		cout<<data[i]<<" ";
	cout<<endl;
}	

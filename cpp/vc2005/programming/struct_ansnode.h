#include "stdafx.h"
#include <cstdio>
#include <vector>
#include <cmath>
#include <math.h>
using namespace std;

struct ansnode {
	double a,b;
	char op;
	ansnode()
	{
	}

	ansnode (double na,double nb,char nop)
	{
		a=na;
		b=nb;
		op=nop;
	}
};

ansnode ans[3];
vector<double> nums[4];
char f;


void dfs(vector<double> n)
{
	int length=n.size();
	nums[4-length]=n;
	if(length==1)
	{
		if(fabs(n[0]-24)<0.00001)
		{
			f=1;
		}
		return;
	};
	int i,j,k;
	vector<double> buf;
	char una[4];
	memset(una,0,4);
	double a,b;
	double bufd;
	for(i=0;i<length;++i)
	{
		una[i]=1;
	}
	for(j=0;j<length;++j)
	{
		if(i==j)
		{
			continue;
		}
		una[j]=1;
		buf.clear();
		buf.push_back(n[i]+n[j]);
		for(k=0;k<length;++k)
		{
			if(una[k]==0)
			{
				buf.push_back(n[k]);
			}
		}
		ans[4-length]=ansnode(n[i],n[j],'+');
		dfs(buf);
		if(f==1)
		{
			return;
		}
		if(n[i]-n[j]>=0)
		{
			buf.clear();
			buf.push_back(n[i]-n[j]);
			for(k=0;k<length;++k){
				if(una[k]==0){
					buf.push_back(n[k]);
				}
				{//}
					ans[4-length]=ansnode(n[i],n[j],'-');
					dfs(buf);
					if(f==1){
						return;
					}
					{//}
						buf.clear();
						buf.push_back(n[i]*n[j]);
						for(k=0;k<length;++k){
							if(una[k]==0){
								buf.push_back(n[k]);
							}
						}
						ans[4-length]=ansnode(n[i],n[j],'*');
						dfs(buf);
						if(f==1){
							return;
						}
						if(n[j]!=0){
							buf.clear();
							buf.push_back(n[i]/n[j]);
							for(k=0;k<length;++k){
								if(una[k]==0){
									buf.push_back(n[k]);
								}
							}
							ans[4-length]=ansnode(n[i],n[j],'/');
							dfs(buf);
							if(f==1){
								return;
							}
						}

						una[j]=0;
					}
					una[i]=0;
				}
			}
		}
	}
}


//int main(){
//	vector<double> a;
//	int i,j;
//	f=0;
//	double onum[4];
//	printf("input 4 numbers separated by space:\n");
//	for(i=0;i<4;i++){
//		scanf("%lf",&onum[i]);
//		a.push_back(onum[i]);
//	}
//	dfs(a);
//	if(f==0){
//		printf("no solution!\n");
//		return 0;
//	}
//
//	for(i=0;i<3;++i){
//		printf("{ ");
//		for(j=0;j<nums[i].size();++j){
//			printf("%.2lf ",nums[i][j]);
//		}
//		printf("}\n");
//		printf("%.2lf %c %.2lf\n\n",ans[i].a,ans[i].op,ans[i].b);
//	}
//
//	return 0;
//} 
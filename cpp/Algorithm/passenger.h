/*
问题描述:在一个旅馆中住着六个不同国籍的人，他们分别来自美国、德国、英国、法国、俄罗斯和意大利。他们的名字叫A、B、C、D、E和F。名字的顺序与上面的国籍不一定是相互对应的。现在已知：
1)A美国人是医生。
2)E和俄罗斯人是技师。
3)C和德国人是技师。
4)B和F曾经当过兵，而德国人从未参过军。
5)法国人比A年龄大；意大利人比C年龄大。
6)B同美国人下周要去西安旅行，而C同法国人下周要去杭州度假。
试问由上述已知条件，A、B、C、D、E和F各是哪国人？

比如：
A,B,C,D,E,F可能国籍分别为:
A是意大利人
B是俄罗斯人
C是英国人
D是法国人
E是德国人
F是美国人
63
*/

#include<iostream>
using namespace std;

int Nation[6];				//A--F的国籍，0--5分别代表美国---意大利
int count=0;				//记录有多少种情况
int main()
{
	void Passenger(int num);
	Passenger(0);
	cout<<count<<endl;
	return 0;
}

void Passenger(int num)
{
	 if(num==6)
	 {
		  bool temp;
		  for(int j=0;j<=5;j++)
		  {
			   temp=false;
			   for(int k=j+1;k<=5;k++)
			   {
					if(Nation[j]==Nation[k])
					{
						 temp++;
						 break;
					}
			   }
			   if(temp)
				   break;
		  }

		  if(temp)
			  return;											/*先确保不同国籍*/
  
			  else
			  {
				   if(Nation[0]!=0 && Nation[4]!=4 && Nation[2]!=1		/*代表已知条件1,2,3*/
					&& Nation[1]!=1 && Nation[5]!=1						/*条件4*/
					&& Nation[0]!=3 && Nation[2]!=5						/*条件5*/
					&& Nation[1]!=0 && Nation[2]!=3						/*条件6*/
					&& Nation[2]!=0 )
				   {
						count++;
						cout<<"A,B,C,D,E,F可能国籍分别为:"<<endl;
						for(int k=0;k<=5;k++)
						{
							 cout<<char('A'+k)<<"是";
							 switch(Nation[k]){
							 case 0:cout<<"美国人"<<endl;break;
							 case 1:cout<<"德国人"<<endl;break;
							 case 2:cout<<"英国人"<<endl;break;
							 case 3:cout<<"法国人"<<endl;break;
							 case 4:cout<<"俄罗斯人"<<endl;break;
							 case 5:cout<<"意大利人"<<endl;break;
							 default:break;
						 }
					}
			   }
		  }
		  return;
	 }
	 else
	 {
		  for(int i=0;i<=5;i++)
		  {
			   Nation[num]=i;
			   num++;
			   Passenger(num);
			   num--;
		  }
	 }
}

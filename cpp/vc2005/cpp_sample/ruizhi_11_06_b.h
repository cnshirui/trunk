#include <iostream>
using namespace std;


/*
．判断一个输入的整数的位数是否为4位。并分别求出各个位上的数字。
程序运行效果：
C:\>请输入一个四位数：1234
         个位是：4；
         十位是：3；
         百位是：2；
         千位是：1。
		 ,quotient,residue
*/
void main()
{
	int num;
	cout<<"请输入一个四位数：";
	cin>>num;
	if(num<1000||num>9999)
	{
		cout<<"输入的整数的位数不是4位!"<<endl;
		return;
	}
	
	cout<<"个位是："<<num%10<<endl;
	cout<<"十位是："<<num/10%10<<endl;
	cout<<"百位是："<<num/100%10<<endl;
	cout<<"千位是："<<num/1000<<endl;
}
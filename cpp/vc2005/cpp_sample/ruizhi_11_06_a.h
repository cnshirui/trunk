#include <iostream>
using namespace std;

void main()
{
	char name[20],gender[5],addr[50],tel[20];
	int age;
	cout<<"请输入用户姓名：";
	cin>>name;
	cout<<"请输入用户性别：";
	cin>>gender;
	cout<<"请输入用户居住地：";
	cin>>addr;
	cout<<"请输入用户电话：";
	cin>>tel;
	cout<<"请输入用户年龄：";
	cin>>age;
	cout<<"你输入的用户信息是："<<endl
        <<"姓名："<<name<<endl
        <<"年龄："<<age<<endl
        <<"性别："<<gender<<endl
        <<"电话："<<tel<<endl
        <<"居住地："<<addr<<endl;
}
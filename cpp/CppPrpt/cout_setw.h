#include <iostream>
#include <stdlib.h>
#include <iomanip>
using namespace std;

void main()
{
	int i=2, j=3;
	double x=2.601, y=1.8;
	cout<< setw(6) << i << setw(10) << j << endl;
	cout<< setw(10) << i*j << endl;
	cout<< "_ _ _ _ _ _"<<endl;
	cout<< setw(8) << x << setw(8) << y << endl;
}

/*
	int i, j, k, l;
	cout<<"Input i(oct), j(hex), k(hex), l(dec):"<<endl;
	cin>> oct>> i; //输入为八进制数
	cin>> hex>> j; //输入为十六进制数
	cin>> k; //输入仍为十六进制数
	cin>> dec>> l; //输入为十进制数
	cout<<" hex:"<<" i="<< hex<< i<< endl;
	cout<<" dec:"<<" j="<< dec<< j<<'\t';
	cout<<" k="<< k<< endl;
	cout<<" oct:"<<" l="<< oct<< l;
	cout<< dec << endl; //恢复十进制输出状态


032 0x3f 0xa0 17 <CR>
则输出结果为：
hex:i=1a
dec:j=63 k=160
oct:l=21
注意：使用不带.h 的头文件<iostream> 时，必须在cin
中指明数制，否则从键盘输入时，不认八进制和十六进制数
开头的0和0x 标志。指明后可省略0和0x 标志。
注意：在cin或cout中指明数制后，该数制将一直有效，
直到重新指明用其他数制。
特别注意：输入数据的格式、个数和类型必须与cin 中的
变量一一对应，否则不仅使输入数据错误，而且影响后面其
他数据的正确输入。
*/
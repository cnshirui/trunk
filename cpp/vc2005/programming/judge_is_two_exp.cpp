#include <cmath>
#include <iostream>
using namespace std;

// how to judge whether a number is the pow of 2
// #define   IsPow(a)   (((~a+0x01)&a)   ==   a)   
#define   IsPow(v)  (0   ==   (v   &   (v-1)))

int main_two_exp() {
	int   i;  
	for   (i=2;   i<=100;   i++)  {
		if(IsPow(i))  
			cout<<i<<endl;  
	}
	cout<<endl;   

	//int a = 8;
	//int b = ~a;
	//int c = b + 0x01;
	//int d = c & a;

	return 0;
}
#include <cmath>
#include <iostream>
using namespace std;

// shift operation, log(2, v)
int count_1(int v) {
	int num = 0;
	while(v) {
		num += v & 0x01;
		v >>= 1;
	}
	return num;
}

// tricky, O(M), M is the count of 1
int count_2(int v) {
	int num = 0;
	while(v) {
		v &= v - 1;
		num ++;
	}
	return num;
}

/*
1. HAKMEM算法：
说明：首先是将二进制各位三个一组，求出每组中1的个数，
然后相邻两组归并，得到六个一组的1的个数，最后很巧妙的用除63取余得到了结果。
因为2^6 = 64，也就是说 x_0 + x_1 * 64 + x_2 * 64 * 64 = x_0 + x_1 + x_2 (mod 63)，这里的等号表示同余。
这个程序只需要十条左右指令，而且不访存，速度很快。
*/
int count_3(unsigned x)
{
    unsigned n;    

    n = (x >> 1) & 033333333333;    
    x = x - n;   
    n = (n >> 1) & 033333333333;   
    x = x - n;    
    x = (x + (x >> 3)) & 030707070707;   
	// x = x - int(x/63); 
	x = (int)fmod((double)x, 63);  // modu(x, 63);  
    return x;   
}  



/*
说明： 这里用的是二分法，两两一组相加，之后四个四个一组相加，接着八个八个，最后就得到各位之和了
x = 342144321, 
x = 0x1464B541, 10100011001001011010101000001

x = 10111101
x >> 1 => 01011110 & 0x55 => 01010100
x - ((x >> 1) & 0x55) = > 01101001 (1, 2, 2, 1)

<--
x & 0x55 => 00010101
(x >> 1) & 0x55 => 01010100
(x & 0x55) + ((x >> 1) & 0x55) => 01101001 (1, 2, 2, 1)
<--

x & 0x33 => 00100001
((x >> 2) & 0x33) => 00010010
(x & 0x33) + ((x >> 2) & 0x33) => 00110011 (3, 3)

(x + (x >> 4)) = 00000100 => 6

*/
int count_4(unsigned x)
{
	x = x - ((x >> 1) & 0x55555555);
	x = (x & 0x33333333) + ((x >> 2) & 0x33333333);
	x = (x + (x >> 4)) & 0x0F0F0F0F;
	x = x + (x >> 8);
	x = x + (x >> 16);
	return x & 0x0000003F;
}

// simple format of count_4
int count_4_0(unsigned x)
{
	x = (x & 0x55555555) + ((x >> 1) & 0x55555555);
	x = (x & 0x33333333) + ((x >> 2) & 0x33333333);
	x = (x & 0x0F0F0F0F) + ((x >> 4) & 0x0F0F0F0F);
	x = (x & 0x00FF00FF) + ((x >> 8) & 0x00FF00FF);
	x = (x & 0x0000FFFF) + ((x >> 16) & 0x0000FFFF);

	return x;
}

void main() {
	int v = 342144321;
	cout << count_1(v) << endl;
	cout << count_2(v) << endl;
	cout << count_3(v) << endl;
	cout << count_4(v) << endl;
	cout << count_4_0(v) << endl;

	cout << "end..." << endl;
}
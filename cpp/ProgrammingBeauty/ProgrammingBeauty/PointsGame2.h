#include <iostream>
#include <string>
#include <cmath>
using namespace std;

const double Threshold = 1E-6;
const int CardsNumber = 4;

const int ResultValue = 24;
double number[CardsNumber] = {1, 8, 3, 5};
string result[CardsNumber];
int ComparedCount = 0;
const int SetNumber = 16;


struct Node {
	string exp;
	double value;
	Node() {
		value = 0;
	}
	Node(const string& str, double v) {
		exp = str;
		value = v;
	}
	bool operator<(const Node& n) const {
		return value < n.value;
	}
} cards[CardsNumber];

set<Node> S[16];

// f(i)的作用是求出i代表的那个集合中的所有元素进行四则运算的
// 结果，并将结果存放在S[i]中
void f(int i) {
	if(!S[i].empty()) return;

	for(int x = 1; x <= (i-x); x++) {
		if((x & i) == x) { // 找到一个真子集
			f(x); //计算集合x，并将结果存放到S[x]中
			f(i-x);	//计算集合i-x，并将结果存放到S[i-x]中

			set<Node>::iterator it1, it2;
			for(it1 = S[x].begin(); it1 != S[x].end(); it1++) {
				for(it2 = S[i-x].begin(); it2 != S[i-x].end(); it2++) {
					const Node& a = *it1, b = *it2;

					COUNT();
					S[i].insert(
						Node("("+a.exp+"+"+b.exp+")", a.value+b.value));

					COUNT();
					S[i].insert(
						Node("("+a.exp+"-"+b.exp+")", a.value-b.value));

					COUNT();
					S[i].insert(
						Node("("+b.exp+"-"+a.exp+")", b.value-a.value));

					COUNT();
					S[i].insert(
						Node("("+a.exp+"*"+b.exp+")", a.value*b.value));

					if(b.value != 0) {
						COUNT();
						S[i].insert(
							Node("("+a.exp+"/"+b.exp+")", a.value/b.value));
					}

					if(a.value != 0) {
						COUNT();
						S[i].insert(
							Node("("+b.exp+"/"+a.exp+")", b.value/a.value));
					}
				}
			}
		}
	}
}


bool PointsGame2(int n)
{

	return false;
}

void TestPointsGame2()
{
	char buffer[20];
	for(int i=0; i<CardsNumber; i++)
	{
		itoa(number[i], buffer, 10);
		result[i] = buffer;
	}

	for(int i=1; i<SetNumber; i++)
	{
		S[i] = 0;
	}



	PointsGame2(4);
	cout << "Compared Count: " << ComparedCount << endl;
}
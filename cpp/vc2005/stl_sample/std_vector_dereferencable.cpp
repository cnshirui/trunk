#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int main_der()
{
	vector< int > ivec;
	ivec.push_back( 54 );
	ivec.push_back( 47 );
	ivec.push_back( 22 );
	ivec.push_back( 74 );
	ivec.push_back( 42 );
	ivec.push_back( 44 );
	ivec.push_back( 46 );

	vector<int>::iterator iter1 = ivec.begin();
	vector<int>::iterator iter2 = ivec.begin();
	vector<int>::iterator iter3 = ivec.end();
	iter3--;

	ivec.push_back(23);
	ivec.push_back(123);
	ivec.push_back(12);
	ivec.push_back(121);


	cout << *iter1 << endl;
	cout << *iter2 << endl;
	cout << *iter3 << endl;

	iter1++;
	cout << *iter1 << endl;

	return 0;
} 
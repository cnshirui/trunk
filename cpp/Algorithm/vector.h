#include <iostream>
#include <vector>
using namespace std;

int sum(vector<int> vec)
{
	int result = 0;
	vector<int>::iterator p = vec.begin();
	while (p!=vec.end())
	{	
		result += *p++;
	}
	return result;
}

void main()
{
	vector<int> v(100);
	cout<<v.size()<<endl;			// 100
	cout<<sum(v)<<endl;				// 0
	v.push_back(23);
	cout<<v.size()<<endl;			// 101
	cout<<sum(v)<<endl;				// 23
	v.reserve(1000);				// reserve? -shirui
	v[900] = 900;
	cout<<v[900]<<endl;				// 900
	cout<<v.front()<<endl;			// 0
	cout<<v.back()<<endl;			// 23
	v.pop_back();
	cout<<v.back()<<endl;
}

// stl_sample.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <vector>
#include <set>
#include <iostream>

//using namespace std;

int _tmain(int argc, _TCHAR* argv[])
{
	std::set<int> v;
	v.insert(1);
	v.insert(2);
	v.insert(1);

	for(std::set<int>::iterator i=v.begin(); i!=v.end(); i++) {
		std::cout << *i << std::endl;
	}
	std::cout << "end..." << std::endl;
	return 0;
}


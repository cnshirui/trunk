// boost_sample.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include <boost/lexical_cast.hpp>
#include <iostream>
int main()
{
	using std::cout;
	using std::endl;
	int i;
	try{
		i = boost::lexical_cast<int>("abcd");
	}
	catch(boost::bad_lexical_cast& e)
	{
		cout<<e.what()<<endl;
		return 1;
	}
	cout<<i<<endl;
	return 0;
}

/*	==========================================================
#include <boost/lexical_cast.hpp>
#include <string>
#include <iostream>
int main()
{
	using std::string;
	const double d = 5.3;
	string s = boost::lexical_cast<string>(d);
	std::cout<<s<<std::endl;
	return 0;
}


/*	==========================================================
#include <boost/lexical_cast.hpp>
#include <iostream>
int _tmain(int argc, _TCHAR* argv[])
{
	using boost::lexical_cast;
	int a = boost::lexical_cast<int>("123");
	double b = boost::lexical_cast<double>("123.12");
	std::cout << a << std::endl;
	std::cout << b << std::endl;
	return 0;
}
*/

// boost_sample.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include "boost/shared_ptr.hpp"
#include <cassert>
class A {  boost::shared_ptr<int> no_;public:  A(boost::shared_ptr<int> no) : no_(no) {}  void value(int i) {    *no_=i;  }};
class B {  boost::shared_ptr<int> no_;public:  B(boost::shared_ptr<int> no) : no_(no) {}  int value() const {    return *no_;  }};
int main() {    boost::shared_ptr<int> temp(new int(14));    A a(temp);    B b(temp);    a.value(28);    assert(b.value()==28);}


// dymanic link: boost_regex-vc80-mt-gd-1_39.lib
// static link:	 libboost_regex-vc80-mt-gd-1_39.lib
// #define BOOST_ALL_DYN_LINK	


/*	==========================================================
#include <boost/regex.hpp>
#include <iostream>
#include <string>


int main()
{
    std::string line;
    boost::regex pat( "^Subject: (Re: |Aw: )*(.*)" );

    while (std::cin)
    {
        std::getline(std::cin, line);
        boost::smatch matches;
        if (boost::regex_match(line, matches, pat))
            std::cout << matches[2] << std::endl;
    }
}

/*	==========================================================
#include <boost/lambda/lambda.hpp>
#include <iostream>
#include <iterator>
#include <algorithm>

int main()
{
    using namespace boost::lambda;
    typedef std::istream_iterator<int> in;

    std::for_each( in(std::cin), in(), std::cout << (_1 * 3) << " " );

	return 0;
}

/*	==========================================================
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

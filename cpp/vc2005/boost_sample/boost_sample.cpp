// boost_sample.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include<string>
#include<iostream>
using namespace std;

#define  BOOST_SPIRIT_NO_REGEX_LIB

#include <boost/spirit/include/classic_actor.hpp>
#include <boost/spirit/include/classic_core.hpp>
#include <boost/spirit/include/classic_push_back_actor.hpp>
using namespace BOOST_SPIRIT_CLASSIC_NS;

const string input = "This Hello World program using Spirit counts the number of	\
 Hello World occurrences in the input";

int main_boost ()
{
	int count = 0;
	parse (input.c_str(),
		*(str_p("Hello World") [ increment_a(count) ]
	|
		anychar_p)
		);
	cout << count << endl;
	return 0;
}


//#include <boost/regex.hpp>
//#include <boost/spirit.hpp>
//#include <boost/tokenizer.hpp>

//#include <boost/spirit/actor.hpp>
//#include <boost/spirit/actor/increment_actor.hpp>
//#include <boost/spirit/core/primitives/primitives.hpp>


   //string s = "This is,  a test";
   //tokenizer<> tok(s);
   //for(tokenizer<>::iterator beg=tok.begin(); beg!=tok.end();++beg){
   //    cout << *beg << "\n";
   //}

   //string s = "Field 1,\"putting quotes around fields, allows commas\",Field 3";
   //tokenizer<escaped_list_separator<char> > tok(s);
   //for(tokenizer<escaped_list_separator<char> >::iterator beg=tok.begin(); beg!=tok.end();++beg){
   //    cout << *beg << "\n";
   //}

   //std::string str = "This is,  a test";
   //typedef boost::tokenizer<boost::char_separator<char> > Tok;
   //boost::char_separator<char> sep; // »± °ππ‘Ï
   //Tok tok(str, sep);
   //for(Tok::iterator tok_iter = tok.begin(); tok_iter != tok.end(); ++tok_iter)
   //  std::cout << "<" << *tok_iter << "> ";
   //std::cout << "\n";

   //using namespace std;
   //using namespace boost;
   //string s = "Field 1,\"putting quotes around fields, allows commas\",Field 3";
   //tokenizer<escaped_list_separator<char> > tok(s);
   //for(tokenizer<escaped_list_separator<char> >::iterator beg=tok.begin(); beg!=tok.end();++beg){
   //    cout << *beg << "\n";
   //}

/*	==========================================================
   using namespace std;
   using namespace boost;
   string s = "12252001";
   int offsets[] = {2,2,4};
   offset_separator f(offsets, offsets+3);
   tokenizer<offset_separator> tok(s,f);
   for(tokenizer<offset_separator>::iterator beg=tok.begin(); beg!=tok.end();++beg){
     cout << *beg << "\n";
   }



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

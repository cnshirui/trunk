#include "stdafx.h"

#include <boost/regex.hpp>
#include <iostream>
#include <string>


int main()
{
    std::string line = "shirui is good!";
    boost::regex pat( "shirui" );

    boost::smatch matches;
	if (boost::regex_match(line, matches, pat)) {
        std::cout << matches[2] << std::endl;
	}
}
#include "stdafx.h"

#include <iostream>
#include <string>
#include <boost/regex.hpp>
using namespace std;
using namespace boost;

int main_sample() 
{
    string s = "This is my simple sample text, really.";
    regex re(",|:|-|\s+");
    sregex_token_iterator my_iter(s.begin( ), s.end( ), re, -1);
    sregex_token_iterator my_end;
    while (my_iter != my_end)
        cout << *my_iter++ << 'n';
    return (EXIT_SUCCESS);
}



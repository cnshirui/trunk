#include <iostream>;
#include <stdio.h>;
using namespace std;


void main_file_line()
{
	printf("this is line %d\n",__LINE__);
	printf("this is filename %s\n",__FILE__);
	cout<<"linenum is "<<__LINE__<<endl;
	cout<<"filenum"<<__FILE__<<endl;; 
}

#include "stdafx.h"

#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<int>::iterator partition( vector<int>::iterator iter1, vector<int>::iterator iter2 )
{
	while( iter1 != iter2 )
	{
		while( *iter1 <= *iter2 )
		{
			--iter2;
		}
		vector<int>::iterator iterTemp;
		// swap( iter1, iter2 );
		iterTemp = iter1;
		iter1 = iter2;
		iter2 = iterTemp;
		++iter1;
		while( *iter1 >= *iter2 )
		{
			++iter1;
		}
		// swap( iter1, iter2 );
		iterTemp = iter1;
		iter1 = iter2;
		iter2 = iterTemp;
		--iter2;
	}
	return iter1;
}

void quicksort( vector<int>::iterator first, vector<int>::iterator last )
{
	if( first != last )
	{
		vector<int>::iterator iterpivot = partition( first, last );
		quicksort( first, iterpivot - 1 );
		quicksort( iterpivot + 1, last );
	}
	return;
}

void print( vector<int>::iterator first, vector<int>::iterator last )
{
	for( vector<int>::iterator iter = first; iter != last; iter++ )
	{
		cout<< "    " << *iter;
	}
	cout<<endl;
	return;
}

int main_qsort()
{
	vector< int > ivec;
	ivec.push_back( 54 );
	ivec.push_back( 47 );
	ivec.push_back( 22 );
	ivec.push_back( 74 );
	ivec.push_back( 42 );
	ivec.push_back( 44 );
	ivec.push_back( 46 );

	print( ivec.begin(), ivec.end() );
	quicksort( ivec.begin(), ivec.end()-1 );
	print( ivec.begin(), ivec.end() );
	return 0;
} 
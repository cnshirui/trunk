#include "stdafx.h"
#include <string>
#include <iostream>

using namespace std;

void main() {
	std::wstring range = _T("'Top Customers'!$D$44:$O$44");
	//_T("Top Customers!$D$44:$O$44");
	// _T("'Top Customers'!$D$44:$O$44");
	size_t index_exclamation = range.rfind('!');
	if(range[0] != '\'' || range[index_exclamation-1] != '\'') {
		range.insert(index_exclamation, _T("'"));
		range.insert(0, _T("'"));
	}

	cout << range.c_str() << endl;
	cout << "shirui" << endl;
}
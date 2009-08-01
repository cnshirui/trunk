// VC8ConsoleTest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
//#include "afxwin.h"
#include "windows.h"
#include <iostream>
#include <string>
//#include <wchar.h>
using namespace std;

#define _AFXDLL



void main_test(int argc, _TCHAR* argv[])
{


}

/*=======================================================
bool VersionComapre(wstring v1, wstring v2)
{
	int mainVersion1, mainVersion2, subVersion1, subVersion2;

	size_t dotIndex = v1.find(_T("."));
	size_t np = wstring::npos;
	if(dotIndex != wstring::npos) {
		mainVersion1 = _wtoi(v1.substr(0, dotIndex).c_str());
		subVersion1 = _wtoi(v1.substr(dotIndex+1, v1.length()).c_str());
	}
	else {
		mainVersion1 = _wtoi(v1.c_str());
	}

	dotIndex = v2.find(_T("."));
	if(dotIndex != wstring::npos) {
		mainVersion2 = _wtoi(v2.substr(0, dotIndex).c_str());
		subVersion2 = _wtoi(v2.substr(dotIndex+1, v2.length()).c_str());
	}
	else {
		mainVersion2 = _wtoi(v2.c_str());
	}

	if(mainVersion1==0 || mainVersion2==0) {
#ifdef _DEBUG
		throw "one of the versions is illegal!";
#endif
	}

	if(mainVersion1 != mainVersion2) {
		return (mainVersion1 > mainVersion2);
	}
	else {
		return (subVersion1 > subVersion2);
	}
}

	if(VersionComapre(a, b)) {
		cout << "5.3 > 5.0" << endl;
	}
	// A simple message box, with only the OK button.
	AfxMessageBox("Simple message box.");

	// A message box that uses a string from a string table
	// with yes and no buttons and the stop icon.
	// NOTE: nStringID is an integer that contains a valid id of
	// a string in the current resource.
	AfxMessageBox("Simple message box.", MB_YESNO|MB_ICONSTOP);

==========
	wstring str_float = _T("33.3");
	string str = "3.4";
	// char* s = str_float.c_str();
	double f = _wtof(str_float.c_str());
	// cout << str << endl;
	if(5.4 <= 5)
	{
		cout << "5.4 <= 5" << endl;
	}
	cout << "shirui" << endl;


=================================================

#ifndef SHIRUI
	cout << "nothing" << endl;
#else
	cout << "shirui" << endl;
#endif
	int i;
	return i;

void add_hundred(HANDLE& h)
{
	h = CreateFile(TEXT("one.txt"),			// open file
							  GENERIC_READ,             // open for reading
							  0,                        // do not share
							  NULL,                     // no security
							  OPEN_EXISTING,            // existing file only
							  FILE_ATTRIBUTE_NORMAL,    // normal file
							  NULL);					// no attr. template
}

void main(int argc, _TCHAR* argv[])
{
	HANDLE h = NULL;
	add_hundred(h);
	cout << h << endl;
	cout << "" << endl;
}

void add_hundred(HANDLE* p=NULL)
{
	*p = CreateFile(TEXT("one.txt"),			// open file
							  GENERIC_READ,             // open for reading
							  0,                        // do not share
							  NULL,                     // no security
							  OPEN_EXISTING,            // existing file only
							  FILE_ATTRIBUTE_NORMAL,    // normal file
							  NULL);					// no attr. template
}

	HANDLE h = NULL;
	HANDLE* p = &h;
	cout << *p << endl;
	add_hundred(p);
	cout << *p << endl;
	cout << "" << endl;

*/
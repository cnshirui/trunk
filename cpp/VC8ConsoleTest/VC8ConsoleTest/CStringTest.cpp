// VC8ConsoleTest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "windows.h"
#include <iostream>
#include <atlstr.h>  
using namespace std;



void main_string(int argc, _TCHAR* argv[])
{
	// C:\Documents and Settings\rshi\Desktop\1/.air
	// C:\\Documents and Settings\\rshi\\Desktop
	CString path("\\1/.air");
	CString ch;
	for(int i=0; i<path.GetLength(); i++)
	{
		// ch.Format("%s", path[i]);
		ch = path.Left(i);
		cout << i << ": " << ch.GetBuffer() << endl;
	}
	int length = path.GetLength();	// 45
	int is = path.ReverseFind('\\');	// 38
	CString name = path.Right(path.GetLength()- (is+1));
	cout << "shirui" << endl;

/*
	str.Format(_T("Floating point: %.2f\n"), 12345.12345);
	_tprintf(_T("%s"), (LPCTSTR) str);

	str.Format(_T("Left-justified integer: %.6d\n"), 35);
	_tprintf(_T("%s"), (LPCTSTR) str);
*/
}




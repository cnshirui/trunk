// VC8ConsoleTest.cpp : Defines the entry point for the console application.
//

#include "windows.h"
#include "unicode.h"
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <atlstr.h>  
using namespace std;



void main_cstring(int argc, _TCHAR* argv[])
{
	CString title = "中国";
	CString path = "亚洲.txt";
	


	FILE * f = _wfopen(path, L"wt+,ccs=UTF-8");
	wstring str = _T("中华人民共和国");			//LPCTSTR(title);
	int length = str.length();
	int size = sizeof(wchar_t);
	fwrite(str.c_str(), 2, str.length(), f);
	fclose(f);

	cout << "shirui" << endl;

	//std::wofstream ofs((LPCTSTR)path);	// , ios::binary
	//ofs << _T("<TITLE>") << ) << _T("</TITLE>") << endl;
	//ofs.close();

	//std::stringstream ofs;
	//wostringstream ofs;
	//  | ios::boolalpha
	//bo::Wide2Ascii(str.c_str(), lname, 1024);


	//ofs << "新加坡" << endl;
	//wchar_t *name = _T("马来西亚");
	//wstring str = _T("马来西亚");
	//LPCTSTR
	//CString	name = _T("马来西亚");
	//ofs << name << endl;
	//std::wstring name = _T("马来西亚");
	//ofs.write(name.c_str(), name.length());

	//wstring str = LPCTSTR(title);
	// LPCTSTR(title)
	//int length = title.GetLength();
	//title.GetAt(1)
	//char lname[1024];


	//if(!ofs.good()) {
	//	cout << "bad" << endl;
	//}
	//title.ReleaseBuffer();
//	ofs.close();
	
	//std::wofstream ofs_(path);
	//ofs_ << ofs.str().c_str();
	//ofs_.close();



		//wstring str = _T("日本");
		//char   buf[4096];
  //      size_t len = 0;
		//wcstombs_s(&len, buf, 4096, str.c_str(), _TRUNCATE);

		//FILE* f = _wfopen_s((LPCTSTR)path, _T("wb")); //write and binary
		//fwrite(buf, len-1, sizeof(char), f);
		//fclose(f);



	//std::string s = "shirui";
	//int i = s.compare("shirui");
	//cout << i << endl;

	//CString s;	//  = "cstring"
	////s += "CString\n";
	//s.Format(_T("shirui\n%i"), 5);

	////cout << s.c_str() << endl;
	////s.Append(_T("shirui"));
	//cout << s.GetBuffer() << endl;
	//
	//cout << s.GetString() << endl;
}
/*
/*====================================================
/*====================================================
/*====================================================
/*====================================================
/*====================================================
/*====================================================
/*====================================================
/*====================================================
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
/*====================================================
	str.Format(_T("Floating point: %.2f\n"), 12345.12345);
	_tprintf(_T("%s"), (LPCTSTR) str);

	str.Format(_T("Left-justified integer: %.6d\n"), 35);
	_tprintf(_T("%s"), (LPCTSTR) str);
*/





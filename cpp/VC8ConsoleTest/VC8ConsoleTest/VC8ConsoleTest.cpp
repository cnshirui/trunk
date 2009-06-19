// VC8ConsoleTest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
//#include "winbase.h"
#include "windows.h"
#include <iostream>
using namespace std;

//#define PRINT_IN cout<<__DATE__<<" "<<__TIME__<<": in "<<__FUNCDNAME__<<endl
//#define PRINT_OUT cout<<__DATE__<<" "__TIME__<<": out "<<__FUNCDNAME__<<endl

#define PRINT_IN TCHAR __szBuff[1024]; 	wsprintf(__szBuff, _T("---> %s %s: into %s\n"), _T(__DATE__), _T(__TIME__), _T(__FUNCDNAME__)); OutputDebugString(__szBuff);
#define PRINT_OUT wsprintf(__szBuff, _T("---> %s %s: out %s\n"), _T(__DATE__), _T(__TIME__), _T(__FUNCDNAME__)); OutputDebugString(__szBuff);
/*
        TCHAR szBuff[1024];
        wsprintf( szBuff, _T("==== Time: %d\n"), (dwStop - dwStart ) );
        OutputDebugString( szBuff );
		strSQL.Format("insert   into   DEPARTMENT(ID,NAME,MANAGE,INTRO)   values('%d',   '%s',   '%s','%s\')",   code,   name,   department,description);

*/

void GetFunctionName();


int _t_main(int argc, _TCHAR* argv[])
{
	PRINT_IN
	GetFunctionName();
	PRINT_OUT
	return 0;
}

void GetFunctionName()
{
	PRINT_IN
	PRINT_OUT
	//OutputDebugString(_T("shirui---------\n"));
	//TCHAR szBuff[1024]; 	wsprintf(szBuff, _T("%s %s: out %s\n"), _T(__DATE__), _T(__TIME__), _T(__FUNCDNAME__)); OutputDebugString(szBuff);
}
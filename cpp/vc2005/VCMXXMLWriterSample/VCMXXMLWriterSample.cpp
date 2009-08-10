// VCMXXMLWriterSample.cpp : Defines the entry point for the application.
//

#include "stdafx.h"
#include "resource.h"
#include "maindlg.h"

CComModule _Module;

int APIENTRY _tWinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance,
	LPTSTR lpszCmdLine, int nCmdShow)
{
	::CoInitialize(NULL);

	_Module.Init(NULL, hInstance);

	CMainDlg dlg;
	int iRet = dlg.DoModal();

	_Module.Term();

	::CoUninitialize();

	return iRet;
}

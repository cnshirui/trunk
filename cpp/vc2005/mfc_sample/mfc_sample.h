// mfc_sample.h : main header file for the mfc_sample application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols


// Cmfc_sampleApp:
// See mfc_sample.cpp for the implementation of this class
//

class Cmfc_sampleApp : public CWinApp
{
public:
	Cmfc_sampleApp();


// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern Cmfc_sampleApp theApp;
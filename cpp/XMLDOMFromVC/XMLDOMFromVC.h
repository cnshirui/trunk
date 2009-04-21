// XMLDOMFromVC.h : main header file for the XMLDOMFROMVC application
//

#if !defined(AFX_XMLDOMFROMVC_H__DA4EAB41_6DF3_11D4_ABD3_000102378429__INCLUDED_)
#define AFX_XMLDOMFROMVC_H__DA4EAB41_6DF3_11D4_ABD3_000102378429__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CXMLDOMFromVCApp:
// See XMLDOMFromVC.cpp for the implementation of this class
//

class CXMLDOMFromVCApp : public CWinApp
{
public:
	CXMLDOMFromVCApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CXMLDOMFromVCApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CXMLDOMFromVCApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_XMLDOMFROMVC_H__DA4EAB41_6DF3_11D4_ABD3_000102378429__INCLUDED_)

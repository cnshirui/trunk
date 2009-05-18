// XMLDOMFromVCDlg.h : header file
//

#if !defined(AFX_XMLDOMFROMVCDLG_H__DA4EAB43_6DF3_11D4_ABD3_000102378429__INCLUDED_)
#define AFX_XMLDOMFROMVCDLG_H__DA4EAB43_6DF3_11D4_ABD3_000102378429__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

/////////////////////////////////////////////////////////////////////////////
// CXMLDOMFromVCDlg dialog

class CXMLDOMFromVCDlg : public CDialog
{
// Construction
public:
	CXMLDOMFromVCDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	//{{AFX_DATA(CXMLDOMFromVCDlg)
	enum { IDD = IDD_XMLDOMFROMVC_DIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CXMLDOMFromVCDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

protected:
	IXMLDOMDocumentPtr m_plDomDocument;
	IXMLDOMElementPtr m_pDocRoot;

protected:
	void DisplayChildren(IXMLDOMNodePtr pParent);
	void DisplayChild(IXMLDOMNodePtr pChild);

// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	//{{AFX_MSG(CXMLDOMFromVCDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	virtual void OnOK();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_XMLDOMFROMVCDLG_H__DA4EAB43_6DF3_11D4_ABD3_000102378429__INCLUDED_)

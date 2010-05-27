#pragma once
#include "atltypes.h"


// CToolTipForFlash

class CToolTipForFlash : public CWnd
{
	DECLARE_DYNAMIC(CToolTipForFlash)
	
private:
	// parent window
	CWnd* m_pParentWnd;
	// show Status
	BOOL m_bShowStatus;
	// current point where tooltip is displayed
	CPoint m_ptCurrent;
	// font
	CFont m_fontTitle, m_fontContent;
	// toolTip title
	CString m_strTitle;
	// toolTip Content
	CString m_strContent;
public:
	CToolTipForFlash();
	virtual ~CToolTipForFlash();
	
	void SetTitle(const CString&);
	void SetContent(const CString&);

	// create tootip
	BOOL Create(CWnd* pParentWnd);
	// show the tooltip
	BOOL Show(const CPoint& point);
	// close the tooltip
	void Close();
	
protected:
	DECLARE_MESSAGE_MAP()
	
public:
	afx_msg void OnPaint();
};

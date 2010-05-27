// ToolTipForFlash.cpp : implementation file
//

#include "stdafx.h"
#include "ToolTipForFlash.h"

#include <string>
#include <vector>
using namespace std;

  
#pragma   warning(disable:4706)   
#define   COMPILE_MULTIMON_STUBS   
#include   <multimon.h>   
#pragma   warning(default:4706)   

// CToolTipForFlash

IMPLEMENT_DYNAMIC(CToolTipForFlash, CWnd)

BEGIN_MESSAGE_MAP(CToolTipForFlash, CWnd)
	ON_WM_PAINT()
END_MESSAGE_MAP()

CToolTipForFlash::CToolTipForFlash()
: m_strTitle(_T(""))
, m_strContent(_T(""))
{
	m_pParentWnd = NULL;
	m_bShowStatus = FALSE;
}

CToolTipForFlash::~CToolTipForFlash()
{
}

BOOL CToolTipForFlash::Create(CWnd* pParentWnd)
{
	ASSERT(pParentWnd != NULL);
	m_pParentWnd = pParentWnd;

	// MS Sans Serif
	m_fontTitle.CreateFont(15, 0, 0, 0, FW_REGULAR, 0, 0, 0, 0, 0, 0, 0, 0, L"Verdana Bold");
	m_fontContent.CreateFont(15, 0, 0, 0, FW_REGULAR, 0, 0, 0, 0, 0, 0, 0, 0, L"Bold");
	CRect rectInitialSize(0,0,0,0); //Initial Window size. Will be dynamically changed later.
	return CreateEx(NULL, NULL, NULL, WS_POPUP | WS_CHILD | WS_CLIPSIBLINGS, rectInitialSize, pParentWnd, NULL, NULL);
}

// m_ptCurrent specifies current mouse position and it is in client 
// coordinates of parent window(Not in screen coordinates).
BOOL CToolTipForFlash::Show(const CPoint& point)
{
	// text empty or tool tip already displayed
	if ( (m_strTitle.IsEmpty() && m_strContent.IsEmpty())  || m_bShowStatus) {
		return FALSE;
	}

	ASSERT(m_hWnd != NULL);
	m_ptCurrent = point;
	m_bShowStatus = TRUE;
	OnPaint();
	return TRUE;
}

void CToolTipForFlash::Close()
{
	ASSERT(m_hWnd != NULL);
	ShowWindow(SW_HIDE); // hide tooltip
	m_bShowStatus = FALSE;
}

void CToolTipForFlash::OnPaint()
{
	// device context for painting
	CPaintDC dc(this);
	CDC* pDC = GetDC();

	// title size
	CFont *pOldFont = pDC->SelectObject(&m_fontTitle);
	CSize sizeTitle = pDC->GetTextExtent(m_strTitle);
	pDC->LPtoDP(&sizeTitle);
	int maxWidth = sizeTitle.cx;
	int sumHeight = sizeTitle.cy;

	// separate content to lines, get content size
	pDC->SelectObject(&m_fontContent);
	CString strLine, tempContent = m_strContent;
	tempContent.Replace (_T("\r\n"), _T("\n"));
	vector<pair<CString, CString>> strLines;
	int index = tempContent.Find(L"\n");
	int maxNameWidth = 0;
	CSize sizeContent;
	while(tempContent.GetLength()>0 || index != -1) {
		if(index > 0) {
			strLine = tempContent.Left(index);
		}
		else {
			strLine = tempContent;
			tempContent = L"";
		}
		sizeContent = pDC->GetTextExtent(strLine);
		pDC->LPtoDP(&sizeContent);
		maxWidth = max(maxWidth, sizeContent.cx);
		sumHeight += sizeContent.cy;

		int tabIndex = strLine.Find(L"\t");
		CString name, value;
		if(tabIndex > 0) {
			name = strLine.Left(tabIndex);
			value = strLine.Mid(tabIndex + 1);

			sizeContent = pDC->GetTextExtent(name);
			pDC->LPtoDP(&sizeContent);
			maxNameWidth = max(maxNameWidth, sizeContent.cx);
		}
		else {
			name = strLine;
		}

		pair<CString, CString> valuePair(name, value);
		strLines.push_back(valuePair);

		tempContent = tempContent.Mid(index + 1);
		index = tempContent.Find(L"\n");
	} 

	// get tooltip size
	CSize sizeToolTip(maxWidth + 16, sumHeight + 6);
	CRect rectToolTip(m_ptCurrent.x, m_ptCurrent.y, m_ptCurrent.x + sizeToolTip.cx, m_ptCurrent.y + sizeToolTip.cy);

	// draw tooltip rect and text
	pDC->SetBkMode(TRANSPARENT);
	CBrush brushToolTip(GetSysColor(COLOR_INFOBK));
	CBrush *pOldBrush = pDC->SelectObject(&brushToolTip);

	// create and select thick black pen
	CPen penBlack(PS_SOLID, 0, COLORREF(RGB(0, 0, 0)));
	CPen* pOldPen = pDC->SelectObject(&penBlack);

	// draw rectangle filled with COLOR_INFOBK
	pDC->Rectangle(0, 0, rectToolTip.Width(), rectToolTip.Height());

	// set text color and alignment
   	pDC->SetTextColor( GetSysColor(COLOR_INFOTEXT) ); // tooltip color set in control panel settings
	pDC->SetTextAlign(TA_LEFT);

	// draw tooltip title
	pDC->SelectObject(&m_fontTitle);
	pDC->TextOut(4, 2, m_strTitle);

	// draw tooltip content
	pDC->SelectObject(&m_fontContent);
	int heith = sizeTitle.cy + 2;
	for(vector<pair<CString, CString>>::iterator itor=strLines.begin(); itor!=strLines.end(); itor++) {
		pDC->TextOut(4, heith, itor->first);
		pDC->TextOut(12 + maxNameWidth, heith, itor->second);
		heith += sizeContent.cy;
	}

	// now display tooltip
	CRect rectWnd = rectToolTip;
	m_pParentWnd->ClientToScreen(rectWnd); // convert from client to screen coordinates
	CPoint ptToolTipLeft = rectWnd.TopLeft();
	SetWindowPos(&wndTop, ptToolTipLeft.x+1, ptToolTipLeft.y+1, rectWnd.Width(), rectWnd.Height(),SWP_SHOWWINDOW|SWP_NOOWNERZORDER|SWP_NOACTIVATE);

	// restore old objects
	pDC->SelectObject(pOldBrush);
	pDC->SelectObject(pOldPen);
	pDC->SelectObject(pOldFont);

	ReleaseDC(pDC);
}

void CToolTipForFlash::SetTitle(const CString& title)
{
	m_strTitle = title;
	m_strTitle.TrimRight();
}

void CToolTipForFlash::SetContent(const CString& content)
{
	m_strContent = content;
	m_strContent.TrimRight();
}

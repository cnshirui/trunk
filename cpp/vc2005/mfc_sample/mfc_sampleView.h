// mfc_sampleView.h : interface of the Cmfc_sampleView class
//


#pragma once


class Cmfc_sampleView : public CView
{
protected: // create from serialization only
	Cmfc_sampleView();
	DECLARE_DYNCREATE(Cmfc_sampleView)

// Attributes
public:
	Cmfc_sampleDoc* GetDocument() const;

// Operations
public:

// Overrides
public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

// Implementation
public:
	virtual ~Cmfc_sampleView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  // debug version in mfc_sampleView.cpp
inline Cmfc_sampleDoc* Cmfc_sampleView::GetDocument() const
   { return reinterpret_cast<Cmfc_sampleDoc*>(m_pDocument); }
#endif


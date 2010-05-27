// mfc_sampleView.cpp : implementation of the Cmfc_sampleView class
//

#include "stdafx.h"
#include "mfc_sample.h"

#include "mfc_sampleDoc.h"
#include "mfc_sampleView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// Cmfc_sampleView

IMPLEMENT_DYNCREATE(Cmfc_sampleView, CView)

BEGIN_MESSAGE_MAP(Cmfc_sampleView, CView)
	// Standard printing commands
	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CView::OnFilePrintPreview)
END_MESSAGE_MAP()

// Cmfc_sampleView construction/destruction

Cmfc_sampleView::Cmfc_sampleView()
{
	// TODO: add construction code here

}

Cmfc_sampleView::~Cmfc_sampleView()
{
}

BOOL Cmfc_sampleView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CView::PreCreateWindow(cs);
}

// Cmfc_sampleView drawing

void Cmfc_sampleView::OnDraw(CDC* /*pDC*/)
{
	Cmfc_sampleDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: add draw code for native data here
}


// Cmfc_sampleView printing

BOOL Cmfc_sampleView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// default preparation
	return DoPreparePrinting(pInfo);
}

void Cmfc_sampleView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add extra initialization before printing
}

void Cmfc_sampleView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add cleanup after printing
}


// Cmfc_sampleView diagnostics

#ifdef _DEBUG
void Cmfc_sampleView::AssertValid() const
{
	CView::AssertValid();
}

void Cmfc_sampleView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

Cmfc_sampleDoc* Cmfc_sampleView::GetDocument() const // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(Cmfc_sampleDoc)));
	return (Cmfc_sampleDoc*)m_pDocument;
}
#endif //_DEBUG


// Cmfc_sampleView message handlers

// mfc_sampleDoc.cpp : implementation of the Cmfc_sampleDoc class
//

#include "stdafx.h"
#include "mfc_sample.h"

#include "mfc_sampleDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// Cmfc_sampleDoc

IMPLEMENT_DYNCREATE(Cmfc_sampleDoc, CDocument)

BEGIN_MESSAGE_MAP(Cmfc_sampleDoc, CDocument)
END_MESSAGE_MAP()


// Cmfc_sampleDoc construction/destruction

Cmfc_sampleDoc::Cmfc_sampleDoc()
{
	// TODO: add one-time construction code here

}

Cmfc_sampleDoc::~Cmfc_sampleDoc()
{
}

BOOL Cmfc_sampleDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}




// Cmfc_sampleDoc serialization

void Cmfc_sampleDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}


// Cmfc_sampleDoc diagnostics

#ifdef _DEBUG
void Cmfc_sampleDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void Cmfc_sampleDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG


// Cmfc_sampleDoc commands

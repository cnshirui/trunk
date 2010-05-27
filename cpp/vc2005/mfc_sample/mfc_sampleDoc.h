// mfc_sampleDoc.h : interface of the Cmfc_sampleDoc class
//


#pragma once


class Cmfc_sampleDoc : public CDocument
{
protected: // create from serialization only
	Cmfc_sampleDoc();
	DECLARE_DYNCREATE(Cmfc_sampleDoc)

// Attributes
public:

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// Implementation
public:
	virtual ~Cmfc_sampleDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
};



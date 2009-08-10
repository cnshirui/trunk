#include "stdafx.h"
#include "resource.h"
#include "maindlg.h"

#import "msxml3.dll" raw_interfaces_only
using namespace MSXML2;

// quick and dirty wait cursor class
class CWaitCursor
{
public:
	CWaitCursor() : m_hCursorOld(::SetCursor(::LoadCursor(NULL, IDC_WAIT)))
	{
	}
	~CWaitCursor()
	{
		::SetCursor(m_hCursorOld);
	}

private:
	HCURSOR m_hCursorOld;
};

CMainDlg::CMainDlg() : m_bInit(FALSE)
{
	m_csMinSize.cx = m_csMinSize.cy = 0;
}


LRESULT CMainDlg::OnInitDialog(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	Init();

	// I don't like Window logo as icons
	SetIcon(::LoadIcon(NULL, IDI_APPLICATION), TRUE);
	SetIcon(::LoadIcon(NULL, IDI_APPLICATION), FALSE);

	// init text in edit box
	SetDlgItemText(IDC_FILENAME, _T("test.xml"));

	CenterWindow();

	return TRUE;
}

LRESULT CMainDlg::OnClose(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	EndDialog(IDCANCEL);
	return 0;
}

LRESULT CMainDlg::OnGetMinMaxInfo(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	if (m_bInit)
	{
		LPMINMAXINFO pmmi = (LPMINMAXINFO)lParam;
		pmmi->ptMinTrackSize.x = m_csMinSize.cx;
		pmmi->ptMinTrackSize.y = m_csMinSize.cy;
	}

	return 0;
}

LRESULT CMainDlg::OnSize(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
	if (m_bInit)
	{
		// set up client rect
		RECT rcClient;
		::SetRect(&rcClient, 0, 0, (int)(short)LOWORD(lParam), (int)(short)HIWORD(lParam));
		int iWidth = rcClient.right - rcClient.left;
		int iHeight = rcClient.bottom - rcClient.top;

		HDWP hdwp = ::BeginDeferWindowPos(9);

		// resize "File Name" edit box
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_FILENAME), NULL,
			0, 0, iWidth - m_csFilename.cx, m_csFilename.cy,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOMOVE
		);

		// resize "Try File" button
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_TRYFILE), NULL,
			rcClient.right - m_csTryFile.cx, rcClient.top + m_csTryFile.cy, 0, 0,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOSIZE
		);

		// resize "Try Demo" button
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_TRYDEMO), NULL,
			rcClient.right - m_csTryDemo.cx, rcClient.top + m_csTryDemo.cy, 0, 0,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOSIZE
		);

		// resize "Exit" button
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_EXIT), NULL,
			rcClient.right - m_csExit.cx, rcClient.top + m_csExit.cy, 0, 0,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOSIZE
		);

		// resize separator
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_SEPARATOR), NULL,
			0, 0, iWidth - m_csSeparator.cx, m_csSeparator.cy,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOMOVE
		);

		// resize "Events" label
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_EVENTS_LABEL), NULL,
			rcClient.left + m_csEventsLabel.cx, rcClient.top + m_csEventsLabel.cy, 0, 0,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOSIZE
		);

		// resize "Events" edit box
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_EVENTS), NULL,
			0, 0,
			m_csEvents.cx, iHeight - m_csEvents.cy,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOMOVE
		);

		// resize "XML" label
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_XML_LABEL), NULL,
			rcClient.left + m_csXmlLabel.cx, rcClient.top + m_csXmlLabel.cy, 0, 0,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOSIZE
		);

		// resize "XML" edit box
		::DeferWindowPos(
			hdwp, GetDlgItem(IDC_XML), NULL,
			0, 0,
			iWidth - m_csXml.cx, iHeight - m_csXml.cy,
			SWP_NOACTIVATE | SWP_NOZORDER | SWP_NOMOVE
		);

		::EndDeferWindowPos(hdwp);
	}

	return 0;
}

LRESULT CMainDlg::OnCancel(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
	// do nothing to eat up Esc key
	return 0;
}

LRESULT CMainDlg::OnExit(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
	EndDialog(IDOK);		// close dialog
	return 0;
}

LRESULT CMainDlg::OnTryFile(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
	CWaitCursor wc;

	// create writer
	CComPtr<IMXWriter> pWriter;
	HRESULT hr = pWriter.CoCreateInstance(__uuidof(MXXMLWriter), NULL);
	_ASSERT(SUCCEEDED(hr));

	// create reader
	CComPtr<ISAXXMLReader> pReader;
	hr = pReader.CoCreateInstance(__uuidof(SAXXMLReader), NULL);
	_ASSERT(SUCCEEDED(hr));

	// set up handlers
	CComQIPtr<ISAXContentHandler> pContentHandler(pWriter);
	CComQIPtr<ISAXDTDHandler> pDTDHandler(pWriter);
	CComQIPtr<ISAXErrorHandler> pErrorHandler(pWriter);
	hr = pReader->putContentHandler(pContentHandler);
	_ASSERT(SUCCEEDED(hr));
	hr = pReader->putDTDHandler(pDTDHandler);
	_ASSERT(SUCCEEDED(hr));
	hr = pReader->putErrorHandler(pErrorHandler);
	_ASSERT(SUCCEEDED(hr));

	// put properties
	CComVariant var;
	var = (LPUNKNOWN)pWriter;
	hr = pReader->putProperty(
		L"http://xml.org/sax/properties/declaration-handler",
		var
	);
	_ASSERT(SUCCEEDED(hr));
	hr = pReader->putProperty(
		L"http://xml.org/sax/properties/lexical-handler",
		var
	);
	_ASSERT(SUCCEEDED(hr));

	// set parameters, clean the scene
	m_wndEvents.SetWindowText(_T(""));

	var = L"";
	hr = pWriter->put_output(var);
	_ASSERT(SUCCEEDED(hr));
	hr = pWriter->put_omitXMLDeclaration(VARIANT_TRUE);
	_ASSERT(SUCCEEDED(hr));

	USES_CONVERSION;

	// parse
	TCHAR szTemp[MAX_PATH];
	m_wndFilename.GetWindowText(szTemp, MAX_PATH);
	hr = pReader->parseURL(T2W(szTemp));

	if (SUCCEEDED(hr))
	{
		// return output
		var.Clear();
		hr = pWriter->get_output(&var);
		_ASSERT(SUCCEEDED(hr));

		OutputToXmlWindow(&var);
	}
	else
	{
		wsprintf(szTemp, _T("Error: Check if file exists in the correct place\r\nResult Code = 0x%08X"), hr);
		m_wndXml.SetWindowText(szTemp);
	}

	return 0;
}

LRESULT CMainDlg::OnTryDemo(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
	CWaitCursor wc;

	// create writer
	CComPtr<IMXWriter> pWriter;
	HRESULT hr = pWriter.CoCreateInstance(__uuidof(MXXMLWriter), NULL);
	_ASSERT(SUCCEEDED(hr));

	// create attributes
	CComPtr<IMXAttributes> pMXAttrs;
	hr = pMXAttrs.CoCreateInstance(__uuidof(SAXAttributes), NULL);
	_ASSERT(SUCCEEDED(hr));

	CComQIPtr<ISAXAttributes> pSAXAttrs(pMXAttrs);

	// set them all to writer, writer implements all these interfaces
	CComQIPtr<ISAXContentHandler> pContentHandler(pWriter);
	CComQIPtr<ISAXDTDHandler> pDTDHandler(pWriter);
	CComQIPtr<ISAXLexicalHandler> pLexicalHandler(pWriter);
	CComQIPtr<ISAXDeclHandler> pDeclHandler(pWriter);
	CComQIPtr<ISAXErrorHandler> pErrorHandler(pWriter);

	// set parameters, clean the scene
	m_wndEvents.SetWindowText(_T(""));

	CComVariant var(L"");
	hr = pWriter->put_output(var);
	_ASSERT(SUCCEEDED(hr));

	// and manually call necessary events to generate XML file
	Log(_T("Content->startDocument"));
	pContentHandler->startDocument();
	Log(_T("Lexical->startDTD"));
	pLexicalHandler->startDTD(
		L"MyDTD", lstrlenW(L"MyDTD"),
		L"", 0,
		L"http://eureka.sample/mydtd.dtd", lstrlenW(L"http://eureka.sample/mydtd.dtd")
	);
		Log(_T("Decl->elementDecl"));
		pDeclHandler->elementDecl(
			L"book", lstrlenW(L"book"),
			L"title | descr", lstrlenW(L"title | descr")
		);
		Log(_T("Decl->attributeDecl"));
		pDeclHandler->attributeDecl(
			L"book", lstrlenW(L"book"),
			L"author", lstrlenW(L"author"),
			L"CDATA", lstrlenW(L"CDATA"),
			L"#IMPLIED", lstrlenW(L"#IMPLIED"),
			L"", lstrlenW(L"")
		);
		Log(_T("Decl->attributeDecl"));
		pDeclHandler->attributeDecl(
			L"book", lstrlenW(L"book"),
			L"ISBN", lstrlenW(L"ISBN"),
			L"CDATA", lstrlenW(L"CDATA"),
			L"#REQUIRED", lstrlenW(L"#REQUIRED"),
			L"000000000", lstrlenW(L"000000000")
		);
		Log(_T("Decl->attributeDecl"));
		pDeclHandler->attributeDecl(
			L"book", lstrlenW(L"book"),
			L"cover", lstrlenW(L"cover"),
			L"(hard|soft)", lstrlenW(L"(hard|soft)"),
			L"", lstrlenW(L""),
			L"soft", lstrlenW(L"soft")
		);
		Log(_T("Decl-elementDecl"));
		pDeclHandler->elementDecl(
			L"title", lstrlenW(L"title"),
			L"(#PCDATA)", lstrlenW(L"(#PCDATA)")
		);
		Log(_T("Decl-elementDecl"));
		pDeclHandler->elementDecl(
			L"descr", lstrlenW(L"descr"),
			L"(#PCDATA)", lstrlenW(L"(#PCDATA)")
		);
	Log(_T("Lexical->endDTD"));
	pLexicalHandler->endDTD();
	Log(_T("Content->startElement"));
	pMXAttrs->addAttribute(
		CComBSTR(""),
		CComBSTR(""),
		CComBSTR("cover"),
		CComBSTR(""),
		CComBSTR("hard")
	);
	pContentHandler->startElement(
		L"", lstrlenW(L""),
		L"", lstrlenW(L""),
		L"book", lstrlenW(L"book"),
		pSAXAttrs
	);
		Log(_T("Content->startElement"));
		pMXAttrs->clear();
		pContentHandler->startElement(
			L"", lstrlenW(L""),
			L"", lstrlenW(L""),
			L"title", lstrlenW(L"title"),
			pSAXAttrs
		);
		Log(_T("Content->characters"));
		pContentHandler->characters(
			L"On the Circular Problem of Quadratic Equations",
			lstrlenW(L"On the Circular Problem of Quadratic Equations")
		);
		Log(_T("Content->endElement"));
		pContentHandler->endElement(
			L"", lstrlenW(L""),
			L"", lstrlenW(L""),
			L"title", lstrlenW(L"title")
		);
	Log(_T("Content->endElement"));
	pContentHandler->endElement(
		L"", lstrlenW(L""),
		L"", lstrlenW(L""),
		L"book", lstrlenW(L"book")
	);

	// output XML
	var.Clear();
	hr = pWriter->get_output(&var);
	_ASSERT(SUCCEEDED(hr));

	OutputToXmlWindow(&var);

	return 0;
}

void CMainDlg::Init()
{
	// calculate minimum window size
	RECT rc;
	GetWindowRect(&rc);
	m_csMinSize.cx = rc.right - rc.left;
	m_csMinSize.cy = rc.bottom - rc.top;

	RECT rcCtrl;
	GetClientRect(&rc);

	// calculate "File Name" edit box resizing info
	::GetWindowRect(GetDlgItem(IDC_FILENAME), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csFilename.cx = (rc.right - rc.left) -
		(rcCtrl.right - rcCtrl.left);				// store width difference
	m_csFilename.cy = rcCtrl.bottom - rcCtrl.top;	// store control height

	// calculate "Try File" button resizing info
	::GetWindowRect(GetDlgItem(IDC_TRYFILE), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csTryFile.cx = rc.right - rcCtrl.left;		// store right offset
	m_csTryFile.cy = rcCtrl.top - rc.top;			// store top offset

	// calculate "Try Demo" button resizing info
	::GetWindowRect(GetDlgItem(IDC_TRYDEMO), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csTryDemo.cx = rc.right - rcCtrl.left;		// store right offset
	m_csTryDemo.cy = rcCtrl.top - rc.top;			// store top offset

	// calculate "Exit" button resizing info
	::GetWindowRect(GetDlgItem(IDC_EXIT), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csExit.cx = rc.right - rcCtrl.left;			// store right offset
	m_csExit.cy = rcCtrl.top - rc.top;				// store top offset

	// calculate separator resizing info
	::GetWindowRect(GetDlgItem(IDC_SEPARATOR), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csSeparator.cx = (rc.right - rc.left) -
		(rcCtrl.right - rcCtrl.left);				// store width difference
	m_csSeparator.cy = rcCtrl.bottom - rcCtrl.top;	// store control height

	// calculate "Events" label resizing info
	::GetWindowRect(GetDlgItem(IDC_EVENTS_LABEL), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csEventsLabel.cx = rcCtrl.left - rc.left;		// store left offset
	m_csEventsLabel.cy = rcCtrl.top - rc.top;		// store top offset

	// calculate "Events" edit box resizing info
	::GetWindowRect(GetDlgItem(IDC_EVENTS), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csEvents.cx = rcCtrl.right - rcCtrl.left;		// store width
	m_csEvents.cy = (rc.bottom - rc.top) -
		(rcCtrl.bottom - rcCtrl.top);				// store height difference

	// calculate "XML" label resizing info
	::GetWindowRect(GetDlgItem(IDC_XML_LABEL), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csXmlLabel.cx = rcCtrl.left - rc.left;		// store left offset
	m_csXmlLabel.cy = rcCtrl.top - rc.top;			// store top offset

	// calculate "XML" edit box resizing info
	::GetWindowRect(GetDlgItem(IDC_XML), &rcCtrl);
	ScreenToClient(&rcCtrl);
	m_csXml.cx = (rc.right - rc.left) -
		(rcCtrl.right - rcCtrl.left);				// store width difference
	m_csXml.cy = (rc.bottom - rc.top) -
		(rcCtrl.bottom - rcCtrl.top);				// store height difference

	// attach window class to controls (I am lazy to use raw SendMessage)
	m_wndFilename.Attach(GetDlgItem(IDC_FILENAME));
	m_wndEvents.Attach(GetDlgItem(IDC_EVENTS));
	m_wndXml.Attach(GetDlgItem(IDC_XML));

	// limit text for safety
	m_wndFilename.SendMessage(EM_LIMITTEXT, MAX_PATH);

	// I like smaller tabstops
	UINT nTabStops = 16;
	m_wndEvents.SendMessage(EM_SETTABSTOPS, 1, (LPARAM)&nTabStops);
	m_wndXml.SendMessage(EM_SETTABSTOPS, 1, (LPARAM)&nTabStops);

	m_bInit = TRUE;
}

void CMainDlg::OutputToXmlWindow(VARIANT* pVar)
{
	LPTSTR pszOutput;

#ifndef UNICODE
	// since output can be quite large, ATL's internal conversion is not going make it
	int iSize = ::SysStringLen(V_BSTR(pVar)) + 1;
	pszOutput = new CHAR[iSize];
	_ASSERT(pszOutput);
	::WideCharToMultiByte(
		::GetACP(),
		0,
		(LPWSTR)V_BSTR(pVar),
		-1,
		pszOutput,
		iSize,
		NULL,
		NULL
	);
#else
	pszOutput = (LPWSTR)V_BSTR(pVar);
#endif

	m_wndXml.SetWindowText(pszOutput);

#ifndef UNICODE
	delete [] pszOutput;
#endif
}

void CMainDlg::Log(LPCTSTR pszMsg)
{
	// set selection to the end and append
	int iLen = m_wndEvents.GetWindowTextLength();
	m_wndEvents.SendMessage(EM_SETSEL, (WPARAM)iLen, (LPARAM)iLen);
	m_wndEvents.SendMessage(EM_REPLACESEL, (WPARAM)FALSE, (LPARAM)pszMsg);

	// put a new line
	m_wndEvents.SendMessage(EM_REPLACESEL, (WPARAM)FALSE, (LPARAM)_T("\r\n"));
}

// maindlg.h : main dialog
//

#ifndef __MAINDLG_H__
#define __MAINDLG_H__

class CMainDlg : public CDialogImpl<CMainDlg>
{
public:
	enum { IDD = IDD_MAIN };

	CMainDlg();

	BEGIN_MSG_MAP(CMainDlg)
		MESSAGE_HANDLER(WM_INITDIALOG, OnInitDialog)
		MESSAGE_HANDLER(WM_CLOSE, OnClose);
		MESSAGE_HANDLER(WM_GETMINMAXINFO, OnGetMinMaxInfo)
		MESSAGE_HANDLER(WM_SIZE, OnSize)
		COMMAND_ID_HANDLER(IDOK, OnCancel)
		COMMAND_ID_HANDLER(IDCANCEL, OnCancel)
		COMMAND_ID_HANDLER(IDC_EXIT, OnExit)
		COMMAND_ID_HANDLER(IDC_TRYFILE, OnTryFile)
		COMMAND_ID_HANDLER(IDC_TRYDEMO, OnTryDemo)
	END_MSG_MAP()

	// message handlers
	LRESULT OnInitDialog(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	LRESULT OnClose(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	LRESULT OnGetMinMaxInfo(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	LRESULT OnSize(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnCancel(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);
	LRESULT OnExit(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);
	LRESULT OnTryFile(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);
	LRESULT OnTryDemo(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);

protected:
	// other functions
	void Init();
	void OutputToXmlWindow(VARIANT* pVar);
	void Log(LPCTSTR pszMsg);

private:
	BOOL m_bInit;

	// for resizing
	SIZE m_csMinSize;
	SIZE m_csFilename, m_csTryFile, m_csTryDemo, m_csExit, m_csSeparator;
	SIZE m_csEventsLabel, m_csEvents, m_csXmlLabel, m_csXml;

	// easier manipulation of controls
	CWindow m_wndFilename, m_wndEvents, m_wndXml;
};


#endif	// __MAINDLG_H__
// Win32ConsoleXML.cpp : Defines the entry point for the console application.
//

#include <stdio.h>
#include <atlstr.h>  
#import <msxml4.dll>
using namespace MSXML2;


int main_pretty(int argc, char* argv[])
{
	CoInitialize(NULL);
	try{
		IXMLDOMDocument2Ptr pXMLDoc = NULL;
		HRESULT hr = pXMLDoc.CreateInstance(__uuidof(DOMDocument30));

		// Set parser property settings
		pXMLDoc->async =  VARIANT_FALSE;

		// Load the sample XML file
		hr = pXMLDoc->load("compact.xml");

		// If document does not load report the parse error 
		if(hr != VARIANT_TRUE)
		{
			MSXML2::IXMLDOMParseErrorPtr  pError;
			pError = pXMLDoc->parseError;
			// _bstr_t parseError =_bstr_t("At line ")+ _bstr_t(pError->Getline()) + _bstr_t("\n")+ _bstr_t(pError->Getreason());
			// MessageBox(NULL,parseError, "Parse Error",MB_OK);
			// printf("At line %s\n%s", (LPCSTR)pError->Getline(), (LPCSTR)pError->Getreason());
			return 0;
		}
	
		// cannot convert from '_bstr_t' to 'ATL::CStringT<BaseType,StringTraits>'
		CString xml = (LPCTSTR )pXMLDoc->Getxml();
		printf("-----------------------\n%s-----------------------------\n", (LPCSTR)pXMLDoc->Getxml());

		// create writer
		CComPtr<IMXWriter> pWriter;
		hr = pWriter.CoCreateInstance(__uuidof(MXXMLWriter), NULL);
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
		//hr = pReader->putProperty(("http://xml.org/sax/properties/declaration-handler"), var);
		//_ASSERT(SUCCEEDED(hr));
		//hr = pReader->putProperty(("http://xml.org/sax/properties/lexical-handler"), var);
		//_ASSERT(SUCCEEDED(hr));

		// set parameters, clean the scene
		//m_wndEvents.SetWindowText(_T(""));

		var = L"";
		hr = pWriter->put_output(var);
		_ASSERT(SUCCEEDED(hr));
		hr = pWriter->put_omitXMLDeclaration(VARIANT_TRUE);
		_ASSERT(SUCCEEDED(hr));
		hr = pWriter->put_indent(VARIANT_TRUE);
		_ASSERT(SUCCEEDED(hr));

		USES_CONVERSION;

		// parse
		TCHAR szTemp[MAX_PATH];
		//m_wndFilename.GetWindowText(szTemp, MAX_PATH);
		hr = pReader->parse(pXMLDoc->Getxml());
		//->parseURL(T2W(szTemp));

		LPTSTR pszOutput;
		if (SUCCEEDED(hr))
		{
			// return output
			var.Clear();
			hr = pWriter->get_output(&var);
			_ASSERT(SUCCEEDED(hr));

			// OutputToXmlWindow(&var);

			VARIANT* pVar = &var;
			
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

			//m_wndXml.SetWindowText(pszOutput);

#ifndef UNICODE
			delete [] pszOutput;
#endif
		}

		xml = (LPCTSTR )pXMLDoc->Getxml();
		printf("-----------------------\n%s-----------------------------\n", pszOutput);

		int note=0; note++;
	}
	catch(_com_error &e)
	{
		printf("Error\n");
		printf("\a\tCode = %08lx\n", e.Error());
		printf("\a\tCode meaning = %s", e.ErrorMessage());
		_bstr_t bstrSource(e.Source());
		_bstr_t bstrDescription(e.Description());
		printf("\a\tSource = %s\n", (LPCSTR) bstrSource);
		printf("\a\tDescription = %s\n", (LPCSTR) bstrDescription);
	}

	return 0;
}

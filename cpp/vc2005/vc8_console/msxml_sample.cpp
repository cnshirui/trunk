// Win32ConsoleXML.cpp : Defines the entry point for the console application.
//

#include <stdio.h>
#include <atlstr.h>  
#import <msxml4.dll>
using namespace MSXML2;

void dump_com_error(_com_error &e);


int main_msxml(int argc, char* argv[])
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
			printf("At line %s\n%s", (LPCSTR)pError->Getline(), (LPCSTR)pError->Getreason());
			return 0;
		}
	
		// cannot convert from '_bstr_t' to 'ATL::CStringT<BaseType,StringTraits>'
		CString xml = (LPCTSTR )pXMLDoc->Getxml();
		printf("-----------------------\n%s-----------------------------\n", (LPCSTR)pXMLDoc->Getxml());

		MSXML2::IMXWriterPtr pXMLWriter;
		pXMLWriter.CreateInstance(__uuidof(MSXML2::MXXMLWriter));

		CComVariant var((LPUNKNOWN)pXMLDoc) ;
		pXMLWriter->put_output(var);
		pXMLWriter->put_indent(VARIANT_TRUE) ;

		pXMLDoc->put_preserveWhiteSpace(VARIANT_FALSE);

		//pXMLWriter->indent = true;
		//pXMLWriter->put_output(pXMLDoc->va);
		//ISAXContentHandlerPtr pContentHandler = pXMLWriter;
		//pContentHandler->startDocument();
		//pContentHandler->endDocument();

		printf("-----------------------\n%s-----------------------------\n", (LPCSTR)pXMLDoc->Getxml());



		int note=0; note++;
	}
	catch(_com_error &e)
	{
		dump_com_error(e);
	}

	return 0;
}

void dump_com_error(_com_error &e)
{
	printf("Error\n");
	printf("\a\tCode = %08lx\n", e.Error());
	printf("\a\tCode meaning = %s", e.ErrorMessage());
	_bstr_t bstrSource(e.Source());
	_bstr_t bstrDescription(e.Description());
	printf("\a\tSource = %s\n", (LPCSTR) bstrSource);
	printf("\a\tDescription = %s\n", (LPCSTR) bstrDescription);
}

/*
    // Otherwise, build node list using SelectNodes 
    // and returns its length as console output
    else
      pXMLDoc->setProperty("SelectionLanguage", "XPath");
      // Set the selection namespace URI if the nodes
      // you wish to select later use a namespace prefix
      pXMLDoc->setProperty("SelectionNamespaces",
      "xmlns:xsl='http://www.w3.org/1999/XSL/Transform'");
      MSXML2::IXMLDOMElementPtr pXMLDocElement = NULL;
	  
      pXMLDocElement = pXMLDoc->documentElement;
      MSXML2::IXMLDOMNodeListPtr pXMLDomNodeList = NULL;
      pXMLDomNodeList = pXMLDocElement->selectNodes("//xsl:template");
      int count = 0;
      count = pXMLDomNodeList->length;
      printf("The number of <xsl:template> nodes is %i.\n", count);
*/
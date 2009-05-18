// Win32ConsoleXML.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <stdio.h>
#import <msxml4.dll>
using namespace MSXML2;

void dump_com_error(_com_error &e);

int main(int argc, char* argv[])
{
  CoInitialize(NULL);
  try{
    IXMLDOMDocument2Ptr pXMLDoc = NULL;
    HRESULT hr = pXMLDoc.CreateInstance(__uuidof(DOMDocument30));

    // Set parser property settings
    pXMLDoc->async =  VARIANT_FALSE;

    // Load the sample XML file
    hr = pXMLDoc->load("hello.xsl");

    // If document does not load report the parse error 
    if(hr!=VARIANT_TRUE)
    {
      MSXML2::IXMLDOMParseErrorPtr  pError;
      pError = pXMLDoc->parseError;
      _bstr_t parseError =_bstr_t("At line ")+ _bstr_t(pError->Getline())
      + _bstr_t("\n")+ _bstr_t(pError->Getreason());
      MessageBox(NULL,parseError, "Parse Error",MB_OK);
      return 0;
    }
    // Otherwise, build node list using SelectNodes 
    // and returns its length as console output
    else
      pXMLDoc->setProperty("SelectionLanguage", "XPath");
      // Set the selection namespace URI if the nodes
      // you wish to select later use a namespace prefix
      pXMLDoc->setProperty("SelectionNamespaces",
      "xmlns:xsl='http://www.w3.org/1999/XSL/Transform'");
      IXMLDOMElementPtr pXMLDocElement = NULL;
      pXMLDocElement = pXMLDoc->documentElement;
      IXMLDOMNodeListPtr pXMLDomNodeList = NULL;
      pXMLDomNodeList = pXMLDocElement->selectNodes("//xsl:template");
      int count = 0;
      count = pXMLDomNodeList->length;
      printf("The number of <xsl:template> nodes is %i.\n", count);
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


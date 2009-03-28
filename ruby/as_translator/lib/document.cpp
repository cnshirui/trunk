#include "stdafx.h"
#include "document.h"
#include "argument.h"
#include "util/list.h"
#include "util/util.h"
#include "util/uget.h"
#include "component/component.h"
#include "component/binding.h"
#include "uilib/library.h"
#include "complib/fontlib.h"
#include "comutil.h"
#include <atlimage.h>

#include <sstream>
//XML refs
#include <comdef.h>
#include <msxml4/msxml4.tlh>
//#import "msxml4.dll" named_guids
using namespace MSXML2; 
using MSXML2::IXMLDOMDocument2Ptr;
using MSXML2::DOMDocument40;
//DEBUG ONLY REMOVE
//#include "../util/output.h"
//#include "stdafx.h"
//REMOVE

#pragma warning (disable:4800)	// forcing a char to bool (performance warning)

cmpDocument::cmpDocument()
{
	//Create a xml document
	IXMLDOMDocument2Ptr pDOM;
	pDOM.CreateInstance(__uuidof(DOMDocument40));

	//Set xml doc
	pXmlDOM = pDOM;

	pAssetDoc=NULL;
	pAssetProp=NULL;
	//Initialize the xml doc
	InitDocument();
	warningImage=_T("");
}

cmpDocument::~cmpDocument()
{
	
}

void cmpDocument::InitDocument(){
	using MSXML2::IXMLDOMNodeList;
	using MSXML2::IXMLDOMNode;

	IXMLDOMDocument2Ptr pDom = pXmlDOM;

	uget uid;
	DocGUID = uid.getUUID(); 

	//"<?xml version="1.0" encoding="utf-8"?><CXCanvas></CXCanvas>
	//VARIANT_BOOL loadSuccess = pDom->loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas><HSlider id=\"growthRate\"><x>21</x><y>207</y><minimum>0</minimum><maximum>1</maximum><liveDragging>true</liveDragging><themeColor>haloOrange</themeColor><snapInterval>.01</snapInterval></HSlider></CXCanvas>");
	pDom->PutpreserveWhiteSpace(VARIANT_FALSE);

	//add docxml as attribute
	std::wstring docxml = _T("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas guid=\"");
	docxml += DocGUID.c_str();
	docxml += _T("\"></CXCanvas>");

	//Add default font

//	/* VARIANT_BOOL loadSuccess = */ pDom->loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas></CXCanvas>");
	/* VARIANT_BOOL loadSuccess = */ pDom->loadXML(docxml.c_str());

	SetExportSettings(true, _T(""));

}


bool cmpDocument::RemoveFont()
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		MSXML2::IXMLDOMElementPtr fontNode = pDom->selectSingleNode("/CXCanvas/font");

		if(fontNode!=NULL)
		{
			MSXML2::IXMLDOMNodePtr compParent = fontNode->GetparentNode();		
			compParent->removeChild(fontNode);
			return true;
		}
		return false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


bool cmpDocument::AddFont(const TCHAR *fontName,unsigned int charSet, unsigned int charSel,bool embed )
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get CXCanvas node
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");

		// Check and remove if font node already exists
		RemoveFont();
		
		//Create font node
		MSXML2::IXMLDOMElementPtr compNode = pDom->createElement("font");

		compNode->setAttribute("fontName",fontName);

		canvasNode->appendChild(compNode);

		MSXML2::IXMLDOMNodePtr setChild;
		MSXML2::IXMLDOMNodePtr selChild;
		MSXML2::IXMLDOMNodePtr emChild;

		int b_embed=0;

		if(embed)
		{
			setChild = pDom->createElement("charSet");
			setChild->text = _bstr_t(charSet);
			compNode->appendChild(setChild);

			selChild = pDom->createElement("charSel");
			selChild->text = _bstr_t(charSel);
			compNode->appendChild(selChild);

			b_embed=1;
		}

		emChild = pDom->createElement("embed");
		emChild->text = _bstr_t(b_embed);
		compNode->appendChild(emChild);

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
	

	
}

bool cmpDocument::GetFont()
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		cmpFontInfo *cmpFont=cmpFontInfo::GetInstance();

		/*const TCHAR* ff=pDom->Getxml();*/

		//Get node list
		MSXML2::IXMLDOMNodeListPtr nodelist;

		MSXML2::IXMLDOMElementPtr fontNode = pDom->selectSingleNode("/CXCanvas/font");

		if(fontNode==NULL)
			return false;

		MSXML2::IXMLDOMNamedNodeMapPtr map = fontNode->Getattributes();
		_bstr_t b=_T("fontName");
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
		_bstr_t c=atrb->Getvalue();
		cmpFontInfo::GetInstance()->SetFontName((TCHAR*)(LPCTSTR)c);//imutil::SetFont( (TCHAR*)(LPCTSTR)c);

		//embed or not

		MSXML2::IXMLDOMElementPtr selNode = fontNode->selectSingleNode("./embed");
		char* selText=_com_util::ConvertBSTRToString(selNode->text);
		
		int embed=(int)atoi(selText);
		delete [] selText;

		if(embed)
		{
			//Get font data
			
			MSXML2::IXMLDOMElementPtr selNode = fontNode->selectSingleNode("./charSel");
			char* selText=_com_util::ConvertBSTRToString(selNode->text);
			
			cmpFont->SetCharSel((unsigned int)atoi(selText));

			delete [] selText;

			MSXML2::IXMLDOMElementPtr setNode = fontNode->selectSingleNode("./charSet");	
			char* setText=_com_util::ConvertBSTRToString(setNode->text);
			cmpFont->SetCharSet((unsigned int)atoi(setText));

			delete [] setText;
		}

		//Return true if non embedded otherwise embed;
		if(embed)
			return false;
		else
			return true;
		}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::AddConnection(const TCHAR *className, const TCHAR *instanceID, const TCHAR *displayName)
{
	try
	{

		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get CXCanvas node
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");

		//Create component node
		MSXML2::IXMLDOMElementPtr compNode = pDom->createElement("connection");

		//Set id attribute
		compNode->setAttribute("id",instanceID);

		//set className attribute
		compNode->setAttribute("className",className);

		//Figure out display name count based on number of components
		MSXML2::IXMLDOMNodeListPtr nodelist;
		nodelist = pDom->selectNodes("//connection");
		int len = 1;
		if(nodelist != NULL){
			len = nodelist->length;
			len++;
		}
		_bstr_t dispName = _bstr_t(GenerateName(displayName));

		//set displayName attribute
		compNode->setAttribute("displayName",dispName);

		//Append compnent node to cxcanvas
		canvasNode->appendChild(compNode);

		//Get default properties string from uiComp
		//Get the uiLibrary pointer
		uiLibrary* uiLib=uiLibrary::GetInstance();
		uiLibraryComponent* uicomp = uiLib->GetComponent(className);

		//Did we get a uiComp?
		if(uicomp != NULL){
			//Copy default xml from uiComponent to componentXML string
			TCHAR *tmp = uicomp->GetDefault();

			//Load default props into a dom
			//Create a xml document
			IXMLDOMDocument2Ptr pDOM2;
			pDOM2.CreateInstance(__uuidof(DOMDocument40));
			pDOM2->PutpreserveWhiteSpace(VARIANT_FALSE);
			/*VARIANT_BOOL loadSuccess = */ pDOM2->loadXML(tmp);

			//Get properties node
			MSXML2::IXMLDOMNodePtr uiCompProps;
			uiCompProps = pDOM2->selectSingleNode("//component[@className='" + _bstr_t(className) + "']/properties");

			//Are there properties to clone?
			if(uiCompProps != NULL){
				//clone the uiComp properties node
				MSXML2::IXMLDOMNodePtr clone = uiCompProps->cloneNode(VARIANT_TRUE);

				//Add properties node to new comp
				compNode->appendChild(clone);
			}
		}
		//Append properties node to component node?
		MSXML2::IXMLDOMNodePtr propertiesNode;
		propertiesNode = compNode->selectSingleNode("properties");
		if (propertiesNode == NULL)
		{
			propertiesNode = pDom->createElement("properties");
			compNode->appendChild(propertiesNode);
		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::AddComponent(const TCHAR *className, const TCHAR *instanceID, const TCHAR *displayName,const TCHAR *styleName,const TCHAR* colorSchemeName, bool rename)
{
	try
	{

		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get CXCanvas node
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		if(!m_undoMap[instanceID].IsEmpty())
		{
			CString xmlStr=m_undoMap[instanceID];
			IXMLDOMDocument2Ptr pDom;
			pDom.CreateInstance(__uuidof(DOMDocument40));
			VARIANT_BOOL loadSuccess = pDom->loadXML(_bstr_t(xmlStr));
			if(loadSuccess)
			{
				MSXML2::IXMLDOMNodePtr pCompNode=pDom->GetdocumentElement();
				canvasNode->appendChild(pCompNode->cloneNode(VARIANT_TRUE));
				return true;
			}

		}

		//Create component node
		MSXML2::IXMLDOMElementPtr compNode = pDom->createElement("component");

		//Set id attribute
		compNode->setAttribute("id",instanceID);

		//set className attribute
		compNode->setAttribute("className",className);

		//set className attribute
		compNode->setAttribute("styleName",styleName);

		//Figure out display name count based on number of components
		MSXML2::IXMLDOMNodeListPtr nodelist;
		nodelist = pDom->selectNodes("//component[@className='" + _bstr_t(className) + "']");
		int len = 1;
		if(nodelist != NULL){
			len = nodelist->length;
			len++;
		}
		_bstr_t dispName = rename?_bstr_t(GenerateName(displayName)):displayName;

		//set displayName attribute
		compNode->setAttribute("displayName",dispName);

		//Append compnent node to cxcanvas
		canvasNode->appendChild(compNode);
		MSXML2::IXMLDOMNodePtr colorSchemeNode = pDom->createElement("colorScheme");
		colorSchemeNode->text = colorSchemeName;
		compNode->appendChild(colorSchemeNode);

		//Get default properties string from uiComp
		//Get the uiLibrary pointer
		uiLibrary* uiLib=uiLibrary::GetInstance();
		uiLibraryComponent* uicomp = uiLib->GetComponent(className);

		//Did we get a uiComp?
		if(uicomp != NULL){
			//Copy default xml from uiComponent to componentXML string
			TCHAR *tmp = uicomp->GetDefault();

			//Load default props into a dom
			//Create a xml document
			IXMLDOMDocument2Ptr pDOM2;
			pDOM2.CreateInstance(__uuidof(DOMDocument40));
			pDOM2->PutpreserveWhiteSpace(VARIANT_FALSE);
			/*VARIANT_BOOL loadSuccess = */ pDOM2->loadXML(tmp);

			//Get properties node
			MSXML2::IXMLDOMNodePtr uiCompProps;
			uiCompProps = pDOM2->selectSingleNode("//component[@className='" + _bstr_t(className) + "']/properties");

			//Are there properties to clone?
			if(uiCompProps != NULL){
				//clone the uiComp properties node
				MSXML2::IXMLDOMNodePtr clone = uiCompProps->cloneNode(VARIANT_TRUE);

				//Add properties node to new comp
				compNode->appendChild(clone);
			}
		}

		//Append properties node to component node?
		MSXML2::IXMLDOMNodePtr propertiesNode;
		propertiesNode = compNode->selectSingleNode("properties");
		if (propertiesNode == NULL)
		{
			propertiesNode = pDom->createElement("properties");
			compNode->appendChild(propertiesNode);
		}

	/*	//Add displayName node to properties
		MSXML2::IXMLDOMNodePtr propertyNode;
		MSXML2::IXMLDOMNodePtr valNode;
		MSXML2::IXMLDOMNodePtr valChild;
		MSXML2::IXMLDOMNodePtr nameNode;
		MSXML2::IXMLDOMNodePtr nameChild;
		//Create property node
		propertyNode = pDom->createElement("property");
		//Create property name nodes
		nameNode = pDom->createElement("name");
		nameChild = pDom->createElement("string");
		nameChild->text = "displayName";
		nameNode->appendChild(nameChild);
		propertyNode->appendChild(nameNode);
		//Create property value nodes
		valNode = pDom->createElement("value");
		valChild = pDom->createElement("string");
		valChild->text = displayName;
		valNode->appendChild(valChild);
		propertyNode->appendChild(valNode);
		//Append property node
		propertiesNode->appendChild(propertyNode);*/

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
	
}


bool cmpDocument::GetComponent(const TCHAR *instanceID, cmpComponent *comp,bool bCompIsConnection)
{
	try
	{

		//Returns pointer to List object that has cmpComponents
		//return Comps[instanceID];
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode;
		if(bCompIsConnection)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		else
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode == NULL){
			//Return
			return false;
		}else{

			//Set component xml string
			comp->SetCompXML(compNode->Getxml());

			//Set bindings xml?
			MSXML2::IXMLDOMNodePtr bindingsNode;
			bindingsNode = compNode->selectSingleNode("./bindings");
			if(bindingsNode != NULL){
				comp->SetBindingXML(bindingsNode->Getxml());
			}

			//Set asset xml
			MSXML2::IXMLDOMNodePtr assetsNode;
			assetsNode = compNode->selectSingleNode("./assets");
			if(assetsNode != NULL){
				comp->SetAssetXML(assetsNode->Getxml());
			}

			//Set metadata xml
			MSXML2::IXMLDOMNodePtr metadataNode;
			metadataNode = compNode->selectSingleNode("./metaData");
			if(metadataNode != NULL){
				comp->SetMetadataXML(metadataNode->Getxml());
			}

			//Set instanceID
			comp->SetInstanceID(instanceID);

			//Get className attribute and set property
			MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
			_bstr_t b=_T("className");
			MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
			_bstr_t classNameString=atrb->Getvalue();
			comp->SetClassName(classNameString);

			//Get styleName attribute and set property
			_bstr_t b2=_T("styleName");
			atrb = map->getNamedItem(b2);
			if(atrb != NULL){
				_bstr_t styleNameString=atrb->Getvalue();
				comp->SetStyleName(styleNameString);
			}

			//Get displayName attribute and set property
			_bstr_t c=_T("displayName");
			atrb = map->getNamedItem(c);
			if(atrb != NULL){
				_bstr_t displayNameString=atrb->Getvalue();
				comp->DisplayName = (displayNameString);
			}

			//Get the uiLibrary pointer
			uiLibrary* uiLib=uiLibrary::GetInstance();
			uiLibraryComponent* uicomp = uiLib->GetComponent(classNameString);
			//Set the uiComponent pointer
			comp->UIComp = uicomp;

			//Set component properties
			MSXML2::IXMLDOMNodePtr propNode;
			MSXML2::IXMLDOMNodePtr valNode;
			//x
			propNode = compNode->selectSingleNode("./properties/property//name[./string = 'x']/following-sibling::*");
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				comp->x = atoi(valNode->text);
			}
			//y
			propNode = compNode->selectSingleNode("./properties/property//name[./string = 'y']/following-sibling::*");
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				comp->y = atoi(valNode->text);
			}
			//width
			propNode = compNode->selectSingleNode("./properties/property//name[./string = 'width']/following-sibling::*");
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				comp->width = atoi(valNode->text);
			}
			//height
			propNode = compNode->selectSingleNode("./properties/property//name[./string = 'height']/following-sibling::*");
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				comp->height = atoi(valNode->text);
			}
	/*		//displayName
			propNode = compNode->selectSingleNode("./properties/property//name[./string = 'displayName']/following-sibling::*");
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./string");
				comp->DisplayName = valNode->text;
			}*/

			//properties array
			std::map<std::wstring, std::wstring> *props = comp->GetProperties();
			props->clear();
			MSXML2::IXMLDOMNodeListPtr nodelist;
			nodelist = compNode->selectNodes("./properties/property");

			if(nodelist!=NULL)
			{
				int propCount = nodelist->Getlength();
				for( int i=0; i<propCount; i++ )
				{
					propNode = nodelist->Getitem(i);

					_bstr_t nameXML;
					_bstr_t valueXML;

					MSXML2::IXMLDOMNodePtr nameNode = propNode->selectSingleNode( _T("./name/string") );
					MSXML2::IXMLDOMNodePtr valueNode = propNode->selectSingleNode( _T("./value/*") );

					nameNode->get_text( nameXML.GetAddress() );
					valueNode->get_xml( valueXML.GetAddress() );

					(*props)[ nameXML.GetBSTR() ] = valueXML.GetBSTR();
				}
			}

			//bindings array
			std::vector<std::wstring> *bindings = comp->GetBindings();
			bindings->clear();
			nodelist = compNode->selectNodes("./bindings/property");

			if(nodelist!=NULL)
			{
				int propCount = nodelist->Getlength();
				for( int i=0; i<propCount; i++ )
				{
					propNode = nodelist->Getitem(i);

					BSTR nodeXML;
					propNode->get_xml(&nodeXML);
					bindings->push_back( nodeXML );
				}
			}

			// style map
			std::map<std::wstring, std::wstring> *styles = comp->GetStyles();
			styles->clear();
			
			nodelist = compNode->selectNodes("./styles/property");

			if(nodelist!=NULL)
			{
				int propCount = nodelist->Getlength();
				for( int i=0; i<propCount; i++ )
				{
					propNode = nodelist->Getitem(i);

					MSXML2::IXMLDOMNodePtr nameNode = propNode->selectSingleNode( _T("./name/string") );
					MSXML2::IXMLDOMNodePtr valueNode = propNode->selectSingleNode( _T("./value/*") );

					std::wstring nameXML = nameNode->Gettext();
					std::wstring valueXML = valueNode->Getxml();

					(*styles)[ nameXML.c_str() ] = valueXML.c_str();
				}
			}

			// sub components
			std::vector<std::wstring> *comps = comp->GetSubComponents();
			comps->clear();
			nodelist = compNode->selectNodes("./component");
			if(nodelist!=NULL)
			{
				int compCount = nodelist->Getlength();
				for( int i=0; i<compCount; i++ )
				{
					compNode = nodelist->Getitem(i);

					//Get instanceID attribute
					MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
					_bstr_t b=_T("id");
					MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
					_bstr_t c=atrb->Getvalue();
					std::wstring instanceID=c;

					comps->push_back( instanceID );
				}
			}

			//Return
			return true;
		}
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

int cmpDocument::GetComponentCount()
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		/*const TCHAR *compXML= */ pDom->Getxml();
		//Get node list
		MSXML2::IXMLDOMNodeListPtr nodelist;
		nodelist = pDom->selectNodes("/CXCanvas/component");
		//Set return val
		int ret = nodelist->Getlength();
		//Return
		return ret;
	}
	catch(...)
	{
		assert(false);
		return 0;
	}
}
int cmpDocument::GetConnectionCount()
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		//Get node list
		MSXML2::IXMLDOMNodeListPtr nodelist;
		nodelist = pDom->selectNodes("/CXCanvas/connection");
		//Set return val
		int ret = nodelist->Getlength();
		//Return
		return ret;
	}
	catch(...)
	{
		assert(false);
		return 0;
	}
}
std::wstring cmpDocument::GetComponentID(int idx)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get node list
		MSXML2::IXMLDOMNodeListPtr nodelist;
		nodelist = pDom->selectNodes("/CXCanvas/component");

		//Get component node based on idx
		MSXML2::IXMLDOMElementPtr compNode = nodelist->Getitem(idx);

		//REMOVE
		_bstr_t zzz=compNode->Getxml();

		//Get instanceID attribute
		MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
		_bstr_t b=_T("id");
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
		_bstr_t c=atrb->Getvalue();
		std::wstring instanceID=c;

		//Return
		return instanceID;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}
std::wstring cmpDocument::GetConnectionID(int idx)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get node list
		MSXML2::IXMLDOMNodeListPtr nodelist;
		nodelist = pDom->selectNodes("/CXCanvas/connection");

		//Get component node based on idx
		MSXML2::IXMLDOMElementPtr compNode = nodelist->Getitem(idx);

		//Get instanceID attribute
		MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
		_bstr_t b=_T("id");
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
		_bstr_t c=atrb->Getvalue();
		std::wstring instanceID=c;

		//Return
		return instanceID;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}

bool cmpDocument::SaveComponent(cmpComponent *comp, int* pChanged)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get properties node
		MSXML2::IXMLDOMNodePtr propertiesNode;
		if(pChanged)
		{
			*pChanged=0;
		}

		propertiesNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(comp->GetInstanceID()) + "']/properties");
		if(!propertiesNode)
		{
			propertiesNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(comp->GetInstanceID()) + "']/properties");
		}
		if(propertiesNode == NULL){
			//Return
			return false;
		}else{
			//Get component node
			MSXML2::IXMLDOMNodePtr compNode;
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(comp->GetInstanceID()) + "']");
			if(!compNode)
			{
				compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(comp->GetInstanceID()) + "']");
			}
			//Set displayName attribute
			MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
			_bstr_t b=_T("displayName");
			MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
			atrb->text = comp->DisplayName.c_str();

			//Set component properties
			MSXML2::IXMLDOMNodePtr propNode;
			//New nodes for new xpath
			MSXML2::IXMLDOMNodePtr propertyNode;
			MSXML2::IXMLDOMNodePtr valNode;
			MSXML2::IXMLDOMNodePtr valChild;
			MSXML2::IXMLDOMNodePtr nameNode;
			MSXML2::IXMLDOMNodePtr nameChild;
			//x
			propNode = propertiesNode->selectSingleNode("./property//name[./string = 'x']/following-sibling::*");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom->createElement("property");
				//Create property name nodes
				nameNode = pDom->createElement("name");
				nameChild = pDom->createElement("string");
				nameChild->text = "x";
				nameNode->appendChild(nameChild);
				propertyNode->appendChild(nameNode);
				//Create property value nodes
				valNode = pDom->createElement("value");
				valChild = pDom->createElement("number");
				valChild->text = "";
				valNode->appendChild(valChild);
				propertyNode->appendChild(valNode);
				//Append property node
				propertiesNode->appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				if(pChanged && valNode->text != (_bstr_t)comp->x)
				{
					*pChanged=1;
				}

				valNode->text = (_bstr_t)comp->x;
			}
			//y
			propNode = propertiesNode->selectSingleNode("./property//name[./string = 'y']/following-sibling::*");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom->createElement("property");
				//Create property name nodes
				nameNode = pDom->createElement("name");
				nameChild = pDom->createElement("string");
				nameChild->text = "y";
				nameNode->appendChild(nameChild);
				propertyNode->appendChild(nameNode);
				//Create property value nodes
				valNode = pDom->createElement("value");
				valChild = pDom->createElement("number");
				valChild->text = "";
				valNode->appendChild(valChild);
				propertyNode->appendChild(valNode);
				//Append property node
				propertiesNode->appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				if(pChanged && valNode->text != (_bstr_t)comp->y)
				{
					*pChanged=1;
				}
				valNode->text = (_bstr_t)comp->y;
			}

			//width
			propNode = propertiesNode->selectSingleNode("./property//name[./string = 'width']/following-sibling::*");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom->createElement("property");
				//Create property name nodes
				nameNode = pDom->createElement("name");
				nameChild = pDom->createElement("string");
				nameChild->text = "width";
				nameNode->appendChild(nameChild);
				propertyNode->appendChild(nameNode);
				//Create property value nodes
				valNode = pDom->createElement("value");
				valChild = pDom->createElement("number");
				valChild->text = "";
				valNode->appendChild(valChild);
				propertyNode->appendChild(valNode);
				//Append property node
				propertiesNode->appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				if(pChanged && valNode->text != (_bstr_t)comp->width)
				{
					*pChanged=1;
				}
				valNode->text = (_bstr_t)comp->width;
			}
			//height
			propNode = propertiesNode->selectSingleNode("./property//name[./string = 'height']/following-sibling::*");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom->createElement("property");
				//Create property name nodes
				nameNode = pDom->createElement("name");
				nameChild = pDom->createElement("string");
				nameChild->text = "height";
				nameNode->appendChild(nameChild);
				propertyNode->appendChild(nameNode);
				//Create property value nodes
				valNode = pDom->createElement("value");
				valChild = pDom->createElement("number");
				valChild->text = "";
				valNode->appendChild(valChild);
				propertyNode->appendChild(valNode);
				//Append property node
				propertiesNode->appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode->selectSingleNode("./number");
				if(pChanged && valNode->text != (_bstr_t)comp->height)
				{
					*pChanged=1;
				}
				valNode->text = (_bstr_t)comp->height;
			}
			//Display Name - we don't set it as a property any more
		
			//Update the component xml string
			comp->SetCompXML(propertiesNode->Getxml());



			//Return
			return true;
		}
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::UpdateComponentXML(const TCHAR* instanceID,const TCHAR *compXML)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get properties node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMNodePtr oldPropertiesNode, newPropertiesNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode)
		{
			oldPropertiesNode = compNode->selectSingleNode("properties");

			IXMLDOMDocument2Ptr pValueDom;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/*VARIANT_BOOL loadSuccess = */ pValueDom->loadXML(compXML);

			MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();
			newPropertiesNode = newValueOrigNode->cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode->replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode->appendChild(newPropertiesNode);

		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


bool cmpDocument::UpdateBindingXML(const TCHAR* instanceID,const TCHAR *bindXML)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get properties node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMNodePtr oldPropertiesNode, newPropertiesNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode)
		{
			oldPropertiesNode = compNode->selectSingleNode("bindings");

			IXMLDOMDocument2Ptr pValueDom;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/*VARIANT_BOOL loadSuccess = */ pValueDom->loadXML(bindXML);

			MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode->cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode->replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode->appendChild(newPropertiesNode);
			

		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::UpdateStyleXML(const TCHAR* instanceID,const TCHAR *styleXML)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get properties node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMNodePtr oldPropertiesNode, newPropertiesNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode->selectSingleNode("styles");

			IXMLDOMDocument2Ptr pValueDom;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/*VARIANT_BOOL loadSuccess = */ pValueDom->loadXML(styleXML);

			MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode->cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode->replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode->appendChild(newPropertiesNode);
		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::UpdateAssetXML(const TCHAR* instanceID,const TCHAR *assetXML)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get properties node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMNodePtr oldPropertiesNode, newPropertiesNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode->selectSingleNode("assets");

			IXMLDOMDocument2Ptr pValueDom;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/*VARIANT_BOOL loadSuccess = */ pValueDom->loadXML(assetXML);

			MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode->cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode->replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode->appendChild(newPropertiesNode);
		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::UpdatePersistXML(const TCHAR* instanceID,const TCHAR *persistXML)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get properties node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMNodePtr oldPropertiesNode, newPropertiesNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode->selectSingleNode("Persist");

			IXMLDOMDocument2Ptr pValueDom;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/*VARIANT_BOOL loadSuccess = */ pValueDom->loadXML(persistXML);

			MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode->cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode->replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode->appendChild(newPropertiesNode);
		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


bool cmpDocument::Save(const TCHAR *fname) 
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		HRESULT hr = pDom->save( fname );
		return (hr==S_OK)? true: false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::Load(const TCHAR *fname)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		VARIANT_BOOL varBool;
		varBool = pDom->load( fname);

		if (varBool) 
		{
			pXmlDOM = pDom;

			MSXML2::IXMLDOMNodePtr nptr = pDom->selectSingleNode("CXCanvas/@guid");
			if(nptr)
			{
				BSTR b;	nptr->get_text(&b);
				DocGUID = b;
			}

			//create components here?
		}

		return varBool==VARIANT_TRUE?true:false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::DeleteComponent(const TCHAR* instanceID, bool bCompIsConnection)
{ 
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode;
		if(bCompIsConnection)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		else
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode == NULL){
			//Return
			return false;
		}else
		{
			CString csXML=(TCHAR*)compNode->Getxml();
			m_undoMap[instanceID]=csXML;
			//Set pointer to parent of comp node
			MSXML2::IXMLDOMNodePtr compParent = compNode->GetparentNode();

			//Remove the component node
			compParent->removeChild(compNode);

		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::ShowComponent(const TCHAR* instanceID, bool bShow)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMElementPtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
	if(!compNode)
		return false;
	compNode->setAttribute("visible",(BOOL)bShow);
	return true;
}
bool cmpDocument::LockComponent(const TCHAR* instanceID, bool bLock)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMElementPtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
	if(!compNode)
		return false;
	compNode->setAttribute("locked",(BOOL)bLock);
	return true;
}
bool cmpDocument::ShowAllComponents(bool bShow)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
	MSXML2::IXMLDOMNodeListPtr nodelist = canvasNode->selectNodes("component");
	int n = nodelist->Getlength();
	for(int i=0; i<n; i++ )
	{
		MSXML2::IXMLDOMElementPtr compNode = nodelist->Getitem(i);
		compNode->setAttribute("visible",(BOOL)bShow);
	}
	return true;
}
bool cmpDocument::LockAllComponents(bool bLock)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
	MSXML2::IXMLDOMNodeListPtr nodelist = canvasNode->selectNodes("component");
	int n = nodelist->Getlength();
	for(int i=0; i<n; i++ )
	{
		MSXML2::IXMLDOMElementPtr compNode = nodelist->Getitem(i);
		compNode->setAttribute("locked",(BOOL)bLock);
	}
	return true;
}
bool cmpDocument::IsComponentLocked(CString compID)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
		return false;
	MSXML2::IXMLDOMNamedNodeMapPtr map=compNode->Getattributes();
	MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("locked");
	if(!atrb)
		return false;
	_bstr_t bs=atrb->Getvalue();
	CString str=(TCHAR*)bs;
	int val=_ttoi(str);
	return (val!=0);

}
bool cmpDocument::IsComponentVisible(CString compID)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMElementPtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
		return true;
	MSXML2::IXMLDOMNamedNodeMapPtr map=compNode->Getattributes();
	MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("visible");
	if(!atrb)
		return true;
	_bstr_t bs=atrb->Getvalue();
	CString str=(TCHAR*)bs;
	int val=_ttoi(str);
	return (val!=0);
}
bool cmpDocument::ModifyComponentProperty(const TCHAR *compID, const TCHAR *propName, const TCHAR *valueXML,CString& oldValXML,bool bCompIsConn)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get component node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMNodePtr propertiesNode;
		if(bCompIsConn)
		{
			compNode=pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		else
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode ==NULL)
		{
			// component not found
			return false;
		}
		propertiesNode = compNode->selectSingleNode("properties");
		if ( propertiesNode == NULL )
		{
			propertiesNode = pDom->createElement("properties");
			compNode->appendChild( propertiesNode );
		}
		assert( propertiesNode != NULL );
		if(propertiesNode == NULL){
			//Return
			return false;
		}else{
			//Set component properties
			MSXML2::IXMLDOMNodePtr propNode;
			//New nodes for new xpath
			MSXML2::IXMLDOMNodePtr propertyNode;
			MSXML2::IXMLDOMNodePtr valNode;
			MSXML2::IXMLDOMNodePtr valChild;
			MSXML2::IXMLDOMNodePtr nameNode;
			MSXML2::IXMLDOMNodePtr nameChild;

			propNode = propertiesNode->selectSingleNode("./property//name[./string = '" + _bstr_t(propName) + "']/following-sibling::*");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom->createElement("property");
				//Create property name nodes
				nameNode = pDom->createElement("name");
				nameChild = pDom->createElement("string");
				nameChild->text = propName;
				nameNode->appendChild(nameChild);
				propertyNode->appendChild(nameNode);
				//Create property value nodes
				valNode = pDom->createElement("value");
				propertyNode->appendChild(valNode);
				//Append property node
				propertiesNode->appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				IXMLDOMDocument2Ptr pValueDom;
				pValueDom.CreateInstance(__uuidof(DOMDocument40));
				/*VARIANT_BOOL loadSuccess = */ pValueDom->loadXML(valueXML);

				MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();
				MSXML2::IXMLDOMNodePtr newValue = newValueOrigNode->cloneNode(VARIANT_TRUE);
			
				MSXML2::IXMLDOMNodePtr oldValue = propNode->GetfirstChild();
				if ( oldValue != NULL )
				{
					propNode->replaceChild( newValue, oldValue );
					oldValXML=(LPCTSTR)(oldValue->Getxml());
				}
				else
				{
					propNode->appendChild( newValue );
				}
			}
				return true;
		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::GetComponentProperty(const TCHAR *compID, const TCHAR *propName,CString& valueXML)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get component node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMNodePtr propertiesNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		
		if(!compNode)
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");

		if(compNode ==NULL)
		{
			// component not found
			return false;
		}
		propertiesNode = compNode->selectSingleNode("properties");
		if(propertiesNode == NULL)
		{
			return false;//properties node not found
		}
		else
		{
			MSXML2::IXMLDOMNodePtr propNode=NULL;
			propNode = propertiesNode->selectSingleNode("./property//name[./string = '" + _bstr_t(propName) + "']/following-sibling::*");
			if(propNode == NULL)
			{
				return false;
			}		
			if(propNode != NULL)
			{
				MSXML2::IXMLDOMNodePtr oldValue = propNode->GetfirstChild();
				if ( oldValue != NULL )
				{
					valueXML=(LPCTSTR)(oldValue->Getxml());
					return true;
				}
			}
		}
		return false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::ModifyCanvasStyle(const TCHAR *styleName, const TCHAR *valueXML)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr root = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr canvasNode = root->selectSingleNode("canvas");
		if(canvasNode==NULL)
		{
			canvasNode = pDom->createElement("canvas");
			root->appendChild(canvasNode);
		}
		MSXML2::IXMLDOMNodePtr stylesNode = canvasNode->selectSingleNode("styles");
		if(stylesNode == NULL)
		{
			stylesNode = pDom->createElement("styles");
			canvasNode->appendChild(stylesNode);
		}
		
		return modifyStyle(stylesNode, styleName, valueXML);
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::ModifyComponentStyle(const TCHAR *compID, const TCHAR *styleName, const TCHAR *valueXML, bool bGlobal)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get component node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMElementPtr stylesNode;
		if(!bGlobal)
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
			if(!compNode)
			{
				compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
			}

			if ( compNode == NULL )
			{
				// component not found
				return false;
			}

			stylesNode = compNode->selectSingleNode("./styles");
			if ( stylesNode == NULL )
			{
				// create the node
				stylesNode = pDom->createElement( "styles" );
				compNode->appendChild( stylesNode );
			}
		}
		else
		{
			MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
			MSXML2::IXMLDOMNodePtr globalStylesNode= canvasNode->selectSingleNode("Styles");
			if(globalStylesNode==NULL)
			{
				CreateCSSStyleDeclaration(compID);
				globalStylesNode= canvasNode->selectSingleNode("Styles");
			}
			stylesNode = globalStylesNode->selectSingleNode("//style[@id='" + _bstr_t(compID) + "']");
			if(!stylesNode)
			{
				stylesNode = pDom->createElement("style");
				_bstr_t bs=compID;
				stylesNode->setAttribute("id",bs);
				globalStylesNode->appendChild(stylesNode);
			}
		}

		assert( stylesNode != NULL );
		if(stylesNode == NULL)
		{
			return false;
		}
		else
		{
			//Set component properties
			MSXML2::IXMLDOMNodePtr propNode;
			//New nodes for new xpath
			MSXML2::IXMLDOMNodePtr propertyNode;
			MSXML2::IXMLDOMNodePtr valNode;
			MSXML2::IXMLDOMNodePtr valChild;
			MSXML2::IXMLDOMNodePtr nameNode;
			MSXML2::IXMLDOMNodePtr nameChild;

			propNode = stylesNode->selectSingleNode("./property//name[./string = '" + _bstr_t(styleName) + "']/following-sibling::*");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom->createElement("property");
				//Create property name nodes
				nameNode = pDom->createElement("name");
				nameChild = pDom->createElement("string");
				nameChild->text = styleName;
				nameNode->appendChild(nameChild);
				propertyNode->appendChild(nameNode);
				//Create property value nodes
				valNode = pDom->createElement("value");
				propertyNode->appendChild(valNode);
				//Append property node
				stylesNode->appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				IXMLDOMDocument2Ptr pValueDom;
				pValueDom.CreateInstance(__uuidof(DOMDocument40));
				/*VARIANT_BOOL loadSuccess = */ pValueDom->loadXML(valueXML);

				MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();
				MSXML2::IXMLDOMNodePtr newValue = newValueOrigNode->cloneNode(VARIANT_TRUE);
			
				MSXML2::IXMLDOMNodePtr oldValue = propNode->GetfirstChild();
				if ( oldValue != NULL )
				{
					propNode->replaceChild( newValue, oldValue );
				}
				else
				{
					propNode->appendChild( newValue );
				}
			}
				return true;

		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::modifyStyle(const MSXML2::IXMLDOMNodePtr stylesNode, const TCHAR *styleName, const TCHAR *valueXML)
{
	assert(stylesNode != NULL);
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr valueNode = stylesNode->selectSingleNode("//property//name[./string='" + _bstr_t(styleName) + "']/following-sibling::*");
	
	try
	{
		if(valueNode == NULL)
		{
			MSXML2::IXMLDOMNodePtr styleNode = pDom->createElement("property");
			stylesNode->appendChild(styleNode);
			MSXML2::IXMLDOMNodePtr nameNode = pDom->createElement("name");
			MSXML2::IXMLDOMNodePtr nameChild = pDom->createElement("string");
			nameChild->text = styleName;
			nameNode->appendChild(nameChild);
			styleNode->appendChild(nameNode);
			valueNode = pDom->createElement("value");
			styleNode->appendChild(valueNode);
		}
		
		//create the 'value' node
		IXMLDOMDocument2Ptr pValueDom;
		pValueDom.CreateInstance(__uuidof(DOMDocument40));
		pValueDom->loadXML(valueXML);

		MSXML2::IXMLDOMNodePtr newValueOrigNode = pValueDom->GetfirstChild();
		MSXML2::IXMLDOMNodePtr newValue = newValueOrigNode->cloneNode(VARIANT_TRUE);
		
		MSXML2::IXMLDOMNodePtr oldValue = valueNode->GetfirstChild();
		if(oldValue == NULL)
		{
			valueNode->appendChild(newValue);
		}
		else
		{
			valueNode->replaceChild(newValue, oldValue);
		}
	}
	catch(...)
	{
		assert(false);
		return false;
	}

	return true;
}
bool cmpDocument::ClearCanvasStyles()
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas/canvas");
		if(canvasNode)
		{
			canvasNode->GetparentNode()->removeChild(canvasNode);
		}
		return true;
	}
	catch(...)
	{
		return false;
	}
}
bool cmpDocument::IsStyleProtected(CString styleName)
{
	CString names[10];
	names[0]=_T("fontFamily");
	names[1]=_T("fontStyle");
	names[2]=_T("fontSize");
	names[3]=_T("fontWeight");
	names[4]=_T("textAlign");
	names[5]=_T("textDecoration");
	names[6]=_T("kerning");
	names[7]=_T("leading");
	names[8]=_T("letterSpacing");
	names[9]=_T("textIndent");
	for(int i=0;i<10;i++)
	{
		if(styleName.Find(names[i])!=-1)
		{
			return true;
		}
	}
	return false;
}
bool cmpDocument::IsColorStyle(CString styleName)
{
	int n=styleName.ReverseFind('.');
	CString str=styleName;
	if(n!=-1)
	{
		str=styleName.Right(styleName.GetLength()-n);
	}
	str.MakeLower();
	int nColor=str.Find(_T("color"));
	if(nColor==-1)
	{
		return false;
	}
	else
	{
		return true;
	}
}

void cmpDocument::ClearAllComponentStyles()
{
	std::vector<CString> res;
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodeListPtr compNodes = pDom->selectNodes("//component");
		int n = compNodes->Getlength();
		for(int i=0;i<n;i++)
		{
			MSXML2::IXMLDOMNodePtr compNode= compNodes->Getitem(i);
			MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
			MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("id");
			_bstr_t compID=atrb->Getvalue();
			MSXML2::IXMLDOMNodePtr stylesNode=compNode->selectSingleNode("styles");
			if(stylesNode)
			{
				MSXML2::IXMLDOMNodeListPtr styleNames=stylesNode->selectNodes("property/name");
				int nStyles=styleNames->Getlength();
				for(int j=0;j<nStyles;j++)
				{
					MSXML2::IXMLDOMNodePtr styleNameNode= styleNames->Getitem(j);
					CString t=(TCHAR*)styleNameNode->Getxml();
					CString styleName=(TCHAR*)styleNameNode->Gettext();
					MSXML2::IXMLDOMNodePtr valueNode=styleNameNode->selectSingleNode("../value");
					_bstr_t valueXML=valueNode->GetfirstChild()->Getxml();
					//Justin Cox confirmed that we clear only color styles
					if(IsColorStyle(styleName))
					{
						MSXML2::IXMLDOMNodePtr styleNode=styleNameNode->GetparentNode();
						t=(TCHAR*)styleNode->Getxml();
						styleNode->GetparentNode()->removeChild(styleNode);
					}
				
				}
			}
		}
	}

	catch(...)
	{
		return;
	}
}
bool cmpDocument::ClearComponentStyle(CString compID, CString style)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get component node
		MSXML2::IXMLDOMNodePtr compNode;
		MSXML2::IXMLDOMElementPtr stylesNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if ( compNode == NULL )
		{
			// component not found
			return false;
		}
		stylesNode = compNode->selectSingleNode("./styles");
		if ( stylesNode == NULL )
		{
			return false;
		}
		MSXML2::IXMLDOMNodePtr	propNode = stylesNode->selectSingleNode("./property//name[./string = '" + _bstr_t(style) + "']/following-sibling::*");
		if(propNode==NULL)
		{
			return false;
		}
		stylesNode->removeChild(propNode->GetparentNode());
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


bool cmpDocument::MoveComponentsUp(std::vector <_bstr_t>& comps)
{
	try
	{
		int n=comps.size();
		for(int i=0;i<n;i++)
		{
			MoveComponentUp(comps[i]);
		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::MoveComponentsDown(std::vector <_bstr_t>& comps)
{
	try
	{
		int n=comps.size();
		for(int i=0;i<n;i++)
		{
			MoveComponentDown(comps[i]);
		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::ReparentComponent(const TCHAR* compID, const TCHAR* newParentID)
{
	try
	{
		//Need to move the component from the current location to be a child of the parent comp
		//for now we are going to tack it on to the end and make it the last child component

		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		//Get parent node
		MSXML2::IXMLDOMNodePtr parentNode;
		if ( _tcscmp( _T(""), newParentID ) == 0 ) {
			// the new parent is the canvas
			parentNode = pDom->selectSingleNode("/CXCanvas");
		}
		else {
			parentNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(newParentID) + "']");
		}

		if(compNode == NULL || parentNode == NULL){
			//Return
			return false;
		}else{

			//Clone the comp node
			MSXML2::IXMLDOMNodePtr clone = compNode->cloneNode(VARIANT_TRUE);

			//Add the cloned node to the parent
			parentNode->appendChild(clone);

			//Set pointer to parent of comp node
			MSXML2::IXMLDOMNodePtr compParent = compNode->GetparentNode();

			//Remove the component node
			compParent->removeChild(compNode);

		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::MoveComponentUp(_bstr_t compID)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		MSXML2::IXMLDOMNodePtr parentNode=compNode->GetparentNode();
		
		MSXML2::IXMLDOMNodePtr clone=compNode->cloneNode(VARIANT_TRUE);
		MSXML2::IXMLDOMNodePtr prevNode=compNode->GetpreviousSibling();
		CString nodeType=(TCHAR*)compNode->GetbaseName();
		CString siblingNodeType=(TCHAR*)prevNode->GetbaseName();
		//we need to move above node of the same type, 
		//i.e. component above previous component or connection above previous connection
		while(prevNode && nodeType!=siblingNodeType)
		{
			prevNode=prevNode->GetpreviousSibling();
			if(prevNode)
			siblingNodeType=(TCHAR*)prevNode->GetbaseName();
		}
		if(!prevNode)
		{
			return true;
		}
		parentNode->insertBefore(clone,prevNode.GetInterfacePtr());
		parentNode->removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::MoveComponentDown(_bstr_t compID)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		MSXML2::IXMLDOMNodePtr clone=compNode->cloneNode(VARIANT_TRUE);

		MSXML2::IXMLDOMNodePtr parentNode=compNode->GetparentNode();
		MSXML2::IXMLDOMNodePtr nextNode=compNode->GetnextSibling();
		if(!nextNode)
			return true;
		CString nodeType=(TCHAR*)compNode->GetbaseName();
		CString siblingNodeType=(TCHAR*)nextNode->GetbaseName();
		//we need to move below the node of the same type, 
		//i.e. component below next component or connection below next connection
		while(nextNode && nodeType!=siblingNodeType)
		{
			nextNode=nextNode->GetnextSibling();
			if(nextNode)
			siblingNodeType=(TCHAR*)nextNode->GetbaseName();
		}

		if(!nextNode)
		{
			return true;
		}
		MSXML2::IXMLDOMNodePtr insertBeforeNode=nextNode->GetnextSibling();
		parentNode->insertBefore(clone,insertBeforeNode.GetInterfacePtr());
		parentNode->removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::ChangeComponentDepth(const TCHAR* compID, int nLevelsUp)
{
	try
	{
		if(nLevelsUp==0)
			return true;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return false;
		}
		MSXML2::IXMLDOMNodePtr clone=compNode->cloneNode(VARIANT_TRUE);
		MSXML2::IXMLDOMNodePtr parent=compNode->GetparentNode();
		if(!parent)
			return false;

		MSXML2::IXMLDOMNodePtr beforeNode=compNode;
		if(nLevelsUp>0)
		{
			for(int i=0;i<nLevelsUp && beforeNode!=0; i++)
			{
				beforeNode=beforeNode->GetpreviousSibling();
			}
			if(beforeNode==0)
			{
				beforeNode=parent->GetfirstChild();
			}
		}
		if(nLevelsUp<0)
		{
			int nLev=-nLevelsUp;
			for(int i=0;i<nLev+1 && beforeNode!=0; i++)
			{
				beforeNode=beforeNode->GetnextSibling();
			}
		}
		if(beforeNode)
		{
			parent->insertBefore(clone,beforeNode.GetInterfacePtr());
		}
		else
		{
			parent->appendChild(clone);
		}
		parent->removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::BringComponentToFront(_bstr_t compID)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return false;
		}

		MSXML2::IXMLDOMNodePtr parentNode=compNode->GetparentNode();
		MSXML2::IXMLDOMNodePtr nextNode=compNode->GetnextSibling();
		if(!nextNode)
		{
			return true;
		}
		MSXML2::IXMLDOMNodePtr clone=compNode->cloneNode(VARIANT_TRUE);
		parentNode->appendChild(clone);
		parentNode->removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::SendComponentToBack(_bstr_t compID)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return false;
		}
		MSXML2::IXMLDOMNodePtr prevNode=compNode->GetpreviousSibling();
		if(!prevNode)
		{
			return true;//it's already first
		}

		MSXML2::IXMLDOMNodePtr parentNode=compNode->GetparentNode();
		
		MSXML2::IXMLDOMNodePtr clone=compNode->cloneNode(VARIANT_TRUE);
		MSXML2::IXMLDOMNodePtr firstSiblingNode=parentNode->GetfirstChild();
		parentNode->insertBefore(clone,firstSiblingNode.GetInterfacePtr());
		parentNode->removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

void cmpDocument::GroupComponents(std::vector <_bstr_t>& /*comps*/,_bstr_t /*newGroupID*/,_bstr_t /*newGroupName*/)
{
	
}
void cmpDocument::UngroupComponents(std::vector <_bstr_t>& /*groups*/)
{

}
_bstr_t cmpDocument::ConvertTransXML(const TCHAR *transXML)
{
	try
	{
		//Load transport xml into a dom
		//Create a xml document
		IXMLDOMDocument2Ptr pDom;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/* VARIANT_BOOL loadSuccess = */ pDom->loadXML(transXML);

		//Get instance id
		MSXML2::IXMLDOMNodeListPtr nodelist;
		nodelist = pDom->selectNodes("//component");

		//Get component node based on idx
		MSXML2::IXMLDOMElementPtr compNode = nodelist->Getitem(0);

		//REMOVE
		_bstr_t zzz=compNode->Getxml();

		//Get instanceID attribute
		MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
		_bstr_t b=_T("id");
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
		_bstr_t instanceID=atrb->Getvalue();

		//Build component node
		_bstr_t temp = "<component id=\"" + instanceID + "\">";

		//Loop through all transport xml props and copy them to compXML string
		nodelist = pDom->selectNodes("//property");

		//Did we find properties?
		if(nodelist != NULL){
			//Loop
			int numProps = nodelist->Getlength();
			int i = 0;
			for(i=0;i<numProps;i++){
				//Get property node
				MSXML2::IXMLDOMNodePtr propNode = nodelist->Getitem(i);

				//REMOVE
				_bstr_t aaa=propNode->text;

				//Get name node
				MSXML2::IXMLDOMNodePtr nameNode = propNode->selectSingleNode("//name//string");
				MSXML2::IXMLDOMNodePtr valueNode = propNode->selectSingleNode("//value//string");;

				//Add property to string
				temp = temp + "<" + nameNode->text + ">" + valueNode->text + "</" + nameNode->text + ">";
			}
		}

		//Append close component
		temp = temp + "</component>";

	//	//Copy temp to compXML
	//	compXML = temp.copy(true);
		
		return temp;

	/*
	<component id ="E3422102-7DFD-D555-4631-82372B890576">
		<onproperties>
			<property>
				<name>
					<string>title</string>
				</name>
				<value>
					<string>Slider Titlesdfsdf</string>
				</value>
			</property>	</onproperties></component>
	*/


	/*  <component className="xcelsius.controls.List" id="E3422102-7DFD-D555-4631-82372B890576">
		<width>125</width>
		<height>200</height>
		<x>0</x>
		<y>0</y>
		<title>"List Box Title"</title>
	  </component>*/
	}
	catch(...)
	{
		assert(false);
		return "";
	}
}

bool cmpDocument::CreateCanvasBinding(const TCHAR *transXML, cmpBinding *binding)
{
	try
	{

		IXMLDOMDocument2Ptr pDom;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/* VARIANT_BOOL loadSuccess = */ pDom->loadXML(transXML);

		//Get type node
		MSXML2::IXMLDOMNodePtr pNode;
		MSXML2::IXMLDOMNodeListPtr pNodeList;
		pNode = pDom->selectSingleNode("//inputtype");
		//Get first child node
		pNodeList = pNode->GetchildNodes();
		pNode = pNodeList->Getitem(0);
		//Set binding property
		binding->inputtype = pNode->Getxml();

		pNode = pDom->selectSingleNode("//outputtype");
		//Get first child node
		pNodeList = pNode->GetchildNodes();
		pNode = pNodeList->Getitem(0);
		//Set binding property
		binding->outputtype = pNode->Getxml();

		//Get component node
		pNode = pDom->selectSingleNode("//component");
		//Get id attribute
		MSXML2::IXMLDOMNamedNodeMapPtr map = pNode->Getattributes();
		_bstr_t b=_T("id");
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
		_bstr_t instanceID=atrb->Getvalue();
		//Set binding property
		binding->instanceID = _T("<string>") + instanceID + _T("</string>");

		//Get cells node
		pNode = pDom->selectSingleNode("//cells");
		//Get first child node
		pNodeList = pNode->GetchildNodes();
		pNode = pNodeList->Getitem(0);
		//Set binding property
		binding->map = pNode->Getxml();

		//Get property name node
		pNode = pDom->selectSingleNode("//name");
		//Get first child node
		pNodeList = pNode->GetchildNodes();
		pNode = pNodeList->Getitem(0);
		//Set binding property
		binding->propertyName = pNode->Getxml();

		MSXML2::IXMLDOMNodePtr pEndpoint = pDom->selectSingleNode("//endpoint");
		map = pEndpoint->Getattributes();
		atrb = map->getNamedItem(_T("id"));
		CComVariant v = atrb->Getvalue();
		binding->bindingID = _T("<string>");
		binding->bindingID += (LPCTSTR)v.bstrVal;
		binding->bindingID += _T("</string>");

		// Get direction
		pNode = pEndpoint->selectSingleNode("./direction");
		pNode = pNode->GetfirstChild();
		binding->direction = pNode->Getxml();

		// Get chain
		pNode = pEndpoint->selectSingleNode("./chain");
		if (pNode->hasChildNodes()) {
			pNode = pNode->GetfirstChild();
			binding->chain = pNode->Getxml();
		} else {
			binding->chain = _T("<null/>");
		}

		// Get inputmapproperties
		pNode = pEndpoint->selectSingleNode("./inputmapproperties");
		if (pNode->hasChildNodes()) {
			pNode = pNode->GetfirstChild();
			binding->inputmapproperties = pNode->Getxml();
		} else {
			binding->inputmapproperties = _T("<null/>");
		}

		// Get outputmapproperties
		pNode = pEndpoint->selectSingleNode("./outputmapproperties");
		if (pNode->hasChildNodes()) {
			pNode = pNode->GetfirstChild();
			binding->outputmapproperties = pNode->Getxml();
		} else {
			binding->outputmapproperties = _T("<null/>");
		}

		return true;
	/*<?xml version="1.0" encoding="UTF-8"?>
	<component id="CC69BF92-53E5-6249-89BB-9B48A91FF477">
	   <onrequestbindings>
		  <property>
			 <name>
				<string>title</string>
			 </name>
			 <value>
				<binding>
				   <endpoint type="excelbinding" displayname="'Sheet1'!$C$5:$D$5">
					  <workbook>[Object 2]</workbook>
					  <type>
						 <string>series1D</string>
					  </type>
					  <range>'Sheet1'!$C$5:$D$5</range>
					  <cells>
						 <array>
							<property id="0">
							   <array>
								  <property id="0">
									 <string>'Sheet1'!$C$5</string>
								  </property>
								  <property id="1">
									 <string>'Sheet1'!$D$5</string>
								  </property>
							   </array>
							</property>
						 </array>
					  </cells>
				   </endpoint>
				</binding>
			 </value>
		  </property>
	   </onrequestbindings>
	</component>*/
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::UpdateCanvasBinding(const TCHAR *transXML, cmpBinding *binding)
{
	try
	{
		IXMLDOMDocument2Ptr pDom;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/* VARIANT_BOOL loadSuccess = */ pDom->loadXML(transXML);

		// get the binding id
		MSXML2::IXMLDOMNodePtr pNode;
		pNode = pDom->selectSingleNode("/binding/endpoint/@id");
		CString bindingId = (LPCTSTR)pNode->text;
		//binding->bindingID = bindingId;
		binding->bindingID = _T("<string>") + bindingId + _T("</string>");
		pNode = pNode->selectSingleNode("../cells");
		//MSXML2::IXMLDOMNodePtr clone = pNode->cloneNode(VARIANT_TRUE);

		IXMLDOMDocument2Ptr pDom2 = pXmlDOM;
			// find the node
		CString endpointPath = "//binding/endpoint[@id='";
		endpointPath += (LPCTSTR)bindingId;
		endpointPath += "']";
		MSXML2::IXMLDOMNodePtr endpointNode = pDom2->selectSingleNode((LPCTSTR)endpointPath);
		MSXML2::IXMLDOMNodePtr cellsNode = endpointNode->selectSingleNode("./cells");
		MSXML2::IXMLDOMNodePtr displayNode = endpointNode->selectSingleNode("./@displayname");
		MSXML2::IXMLDOMNodePtr rangeNode = endpointNode->selectSingleNode("./range");
		if(pNode)
		{
			cellsNode->replaceChild(pNode->GetfirstChild(), cellsNode->GetfirstChild());
			pNode = pNode->selectSingleNode("../@displayname");
			displayNode->replaceChild(pNode->GetfirstChild(), displayNode->GetfirstChild());
			pNode = pNode->selectSingleNode("../range");
			rangeNode->replaceChild(pNode->GetfirstChild(), rangeNode->GetfirstChild());
		}
		else //the case when binding came empty- for example, all cells have been deleted - so we need to remove it
		{
			MSXML2::IXMLDOMNodePtr bindingNode=endpointNode->GetparentNode();
			MSXML2::IXMLDOMNodePtr valueNode=bindingNode->GetparentNode();
			MSXML2::IXMLDOMNodePtr propertyNode=valueNode->GetparentNode();
			MSXML2::IXMLDOMNodePtr bindingsNode=propertyNode->GetparentNode();
			bindingsNode->removeChild(propertyNode);
			return false;
		}		

		MSXML2::IXMLDOMNodePtr guidNode = endpointNode->selectSingleNode("../../../../../@id");
		CString guidId = (LPCTSTR)guidNode->text;
		binding->instanceID = _T("<string>") + guidId + _T("</string>");

		MSXML2::IXMLDOMNodePtr propNode = endpointNode->selectSingleNode("../../../name/string");
		binding->propertyName = propNode->Getxml();

		//Get type node
		binding->inputtype = endpointNode->selectSingleNode("./inputtype")->GetfirstChild()->Getxml();
		binding->outputtype = endpointNode->selectSingleNode("./outputtype")->GetfirstChild()->Getxml();
		binding->direction = endpointNode->selectSingleNode("./direction")->GetfirstChild()->Getxml();
		binding->chain = endpointNode->selectSingleNode("./chain")->GetfirstChild()->Getxml();
		binding->inputmapproperties = endpointNode->selectSingleNode("./inputmapproperties")->GetfirstChild()->Getxml();
		binding->outputmapproperties = endpointNode->selectSingleNode("./outputmapproperties")->GetfirstChild()->Getxml();
		binding->map = endpointNode->selectSingleNode("./cells")->GetfirstChild()->Getxml();

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::GetBinding(const TCHAR *compID,
														 const TCHAR *bindingId,
														 CString& valueXML
														)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get component node
		MSXML2::IXMLDOMNodePtr compNode;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");

		}
		if(compNode ==NULL)
		{
			// component not found
			return false;
		}

		//Select bindings node
		MSXML2::IXMLDOMNodePtr bindingsNode;
		bindingsNode = compNode->selectSingleNode("./bindings");

		if (bindingsNode) {
			// find the node
			CString propPath = "./property/value/binding[endpoint/@id='";
			propPath += bindingId;
			propPath += "']";

			MSXML2::IXMLDOMNodePtr propNode;
			propNode = bindingsNode->selectSingleNode(_bstr_t(propPath));
			if (propNode) {
				valueXML = (LPCTSTR)propNode->Getxml();
			}
		}

		return false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::SetBinding(const TCHAR *transXML,bool bRemoveThisBinding)
{
	try
	{

		IXMLDOMDocument2Ptr pDom;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/* VARIANT_BOOL loadSuccess = */ pDom->loadXML(transXML);

		//Get component id from trans xml
		//Get component node
		MSXML2::IXMLDOMNodePtr cNode;
		cNode = pDom->selectSingleNode("//component");
		//Get id attribute
		MSXML2::IXMLDOMNamedNodeMapPtr map = cNode->Getattributes();
		_bstr_t b=_T("id");
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
		_bstr_t instanceID=atrb->Getvalue();

		//Select property node in trans xml
		MSXML2::IXMLDOMNodePtr pNode;
		pNode = cNode->selectSingleNode("./onrequestbindings//property");

		_bstr_t aaa = pNode->Getxml();

		//Get the property name
		MSXML2::IXMLDOMNodePtr propNameNode;
		propNameNode = pNode->selectSingleNode("./name//string");
		_bstr_t propName = propNameNode->text;

		// get the binding id
		MSXML2::IXMLDOMNodePtr bindingIdNode;
		bindingIdNode = pNode->selectSingleNode("./value/binding/endpoint/@id");
		_bstr_t bindingId = bindingIdNode->text;

		//clone the property node in trans xml
		MSXML2::IXMLDOMNodePtr clone = pNode->cloneNode(VARIANT_TRUE);

		//Select the component node in document xml
		IXMLDOMDocument2Ptr pDom2 = pXmlDOM;

		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode;
		compNode = pDom2->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		compNode = pDom2->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");

		//Select bindings node
		MSXML2::IXMLDOMNodePtr bindingsNode;
		bindingsNode = compNode->selectSingleNode("./bindings");

		//If no bidings node then add it
		if(bindingsNode == NULL){
			//Create bindings node
			bindingsNode = pDom2->createElement("bindings");
			compNode->appendChild(bindingsNode);
		}

		//If binding exists then delete it
		if(bRemoveThisBinding){
			// find the node
			CString propPath = "./property[name/string='";
			propPath += (LPCTSTR)propName;
			propPath += "' and value/binding/endpoint/@id='";
			propPath += (LPCTSTR)bindingId;
			propPath += "']";

			MSXML2::IXMLDOMNodePtr propNode;
			propNode = bindingsNode->selectSingleNode(_bstr_t(propPath));
			if (propNode)
				propNode->parentNode->removeChild(propNode);
		}
		else
		{
			//Add cloned property node to bindings node if necessary
			CString propPath = "./property/value/binding/endpoint[@id='";
			propPath += (LPCTSTR)bindingId;
			propPath += "']";

			MSXML2::IXMLDOMNodePtr oldBinding = bindingsNode->selectSingleNode(_bstr_t(propPath));

			if(!oldBinding)  //don't append the clone if it's already there
			{
				bindingsNode->appendChild(clone);
			}
		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

std::vector<bstr_t> cmpDocument::RemoveBinding(std::vector<_bstr_t> guids, const CString& id)
{
	std::vector<bstr_t> bindingIDs;//result

	try
	{
		//Select the component node in document xml
		IXMLDOMDocument2Ptr pDom2 = pXmlDOM;
		_bstr_t guid=guids[0];
		bool bMultipleComps=(guids.size()>1);
		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode;
		compNode = pDom2->selectSingleNode("//component[@id='" + guid + "']");
		if(!compNode)
		{
			compNode = pDom2->selectSingleNode("//connection[@id='" + guid + "']");
		}

		//Select bindings node
		MSXML2::IXMLDOMNodePtr bindingsNode;
		bindingsNode = compNode->selectSingleNode("./bindings");

		if(bindingsNode == NULL){
			return bindingIDs;
		}

		// find the node
		CString propPath = "./property[value/binding/endpoint/@id='";
		propPath += (LPCTSTR)id;
		propPath += "']";

		MSXML2::IXMLDOMNodePtr propNode;
		propNode = bindingsNode->selectSingleNode(_bstr_t(propPath));
		if (propNode) 
		{
			bstr_t t=propNode->Getxml();
			propNode->parentNode->removeChild(propNode);
			bindingIDs.push_back(_bstr_t(id));
		}
		if(bMultipleComps && propNode)
		{
			//if it's series, find the index
			MSXML2::IXMLDOMNodePtr nameNode=propNode->selectSingleNode("name");
			int seriesIndex=-1;
			_bstr_t name=nameNode->Gettext();
			MSXML2::IXMLDOMNodePtr chainNode=propNode->selectSingleNode("value/binding/endpoint/chain");
			if(chainNode)
			{
				
				MSXML2::IXMLDOMNodePtr propIndexNode =chainNode->selectSingleNode("array/property/object/property[@id='properties']/array/property/object/property[@id='name'][./string='index']");
				if(propIndexNode)
				{
					_bstr_t bs=propIndexNode->Getxml();
					MSXML2::IXMLDOMNodePtr indexValNode=propIndexNode->GetparentNode()->selectSingleNode("property[@id='value']");
					bs=indexValNode->Gettext();
					seriesIndex=_ttoi((TCHAR*)bs);
				}
			}
			for(UINT i=1;i<guids.size();i++)
			{
				MSXML2::IXMLDOMNodePtr nextCompNode = pDom2->selectSingleNode("//component[@id='" + guids[i] + "']");
				if(!nextCompNode)
					continue;
				MSXML2::IXMLDOMNodeListPtr nextNameNodes = nextCompNode->selectNodes("./bindings/property/name[./string='"+name+"']");
				if(!nextNameNodes || nextNameNodes->Getlength()==0)
					continue;
				if(nextNameNodes->Getlength()==1)
				{
					MSXML2::IXMLDOMNodePtr nextNameNode=nextNameNodes->Getitem(0);
					_bstr_t t=nextNameNode->Getxml();
					MSXML2::IXMLDOMNodePtr endpointNode=nextNameNode->selectSingleNode("../value/binding/endpoint");
					if(endpointNode)
					{
						MSXML2::IXMLDOMNamedNodeMapPtr map=endpointNode->Getattributes();
						MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("id");
						if(atrb!=0)
						{
							_bstr_t ID=atrb->Getvalue();
							bindingIDs.push_back(ID);
							MSXML2::IXMLDOMNodePtr delNode=nextNameNode->GetparentNode();
							delNode->GetparentNode()->removeChild(delNode);
						}
					}
				}
				else if (nextNameNodes->Getlength()>1)//subelement binding - so we need to remove the node with the same series number
				{
					for(int j=0;j<nextNameNodes->Getlength();j++)
					{
						MSXML2::IXMLDOMNodePtr nextNameNode=nextNameNodes->Getitem(j);
						_bstr_t bs=nextNameNode->Getxml();
						MSXML2::IXMLDOMNodePtr propIndexNode =nextNameNode->selectSingleNode("../value/binding/endpoint/chain/array/property/object/property[@id='properties']/array/property/object/property[@id='name'][./string='index']");
						if(propIndexNode)
						{
							_bstr_t bs=propIndexNode->Getxml();
							MSXML2::IXMLDOMNodePtr indexValNode=propIndexNode->GetparentNode()->selectSingleNode("property[@id='value']");
							bs=indexValNode->Gettext();
							int index=_ttoi((TCHAR*)bs);
							if(index==seriesIndex)
							{
								MSXML2::IXMLDOMNodePtr endpointNode=nextNameNode->selectSingleNode("../value/binding/endpoint");
								if(endpointNode)
								{
									MSXML2::IXMLDOMNamedNodeMapPtr map=endpointNode->Getattributes();
									MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("id");
									if(atrb!=0)
									{
										_bstr_t ID=atrb->Getvalue();
										bindingIDs.push_back(ID);
										MSXML2::IXMLDOMNodePtr delNode=nextNameNode->GetparentNode();
										delNode->GetparentNode()->removeChild(delNode);
									}
								}
							}
						}
					}

				}
			}
		}
		

		return bindingIDs;
	}
	catch(...)
	{
		assert(false);
		return bindingIDs;
	}
}

void cmpDocument::GetCanvasSize(int& nWidth,int& nHeight)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get CXCanvas node
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		if ( canvasNode != NULL )
		{
			MSXML2::IXMLDOMNamedNodeMapPtr map = canvasNode->Getattributes();
			if (map != NULL)
			{
				MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("width");
				if (atrb != NULL)
				{
					nWidth=atrb->Getvalue();
				}
				atrb = map->getNamedItem("height");
				if (atrb != NULL)
				{
					nHeight=atrb->Getvalue();
				}
			}
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
void cmpDocument::SetCanvasSize(int nWidth, int nHeight)
{
	try
	{
		//Set doc pointer;
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		
		//Get CXCanvas node
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNamedNodeMapPtr map = canvasNode->Getattributes();
		MSXML2::IXMLDOMAttributePtr pXMLAttribute=pDom->createAttribute("width");
		pXMLAttribute->nodeValue=nWidth;
		map->setNamedItem(pXMLAttribute);
		pXMLAttribute=pDom->createAttribute("height");
		pXMLAttribute->nodeValue=nHeight;
		map->setNamedItem(pXMLAttribute);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
std::vector<CString> cmpDocument::GetBindingsForProperty(CString instanceID,CString propName)
{
	std::vector<CString> result;
	try
	{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr compNode=NULL;
	compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
	if(!compNode)
	{	
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
	}
	if(!compNode)
	{
		return result;
	}
	_bstr_t tmp=compNode->Gettext();
	
		MSXML2::IXMLDOMNodePtr bindingsNode=compNode->selectSingleNode("bindings");
		if(!bindingsNode)
			return result;
		MSXML2::IXMLDOMNodePtr propNode=NULL;
		CString propPath = "./property/name[./string = '"+propName+"']";
		propNode = bindingsNode->selectSingleNode(_bstr_t(propPath));
		MSXML2::IXMLDOMNodeListPtr nodeList=NULL;
		if(propNode)
		{
			MSXML2::IXMLDOMNodePtr valueNode=propNode->GetparentNode()->selectSingleNode("value");
			if(valueNode)
			{
				nodeList=valueNode->selectNodes("./binding/endpoint");
					
			int n = nodeList->Getlength();
			for(int i=0;i<n;i++)
			{
				MSXML2::IXMLDOMNodePtr pEndPointNode= nodeList->item[i];
				MSXML2::IXMLDOMNamedNodeMapPtr map = pEndPointNode->Getattributes();
				MSXML2::IXMLDOMAttributePtr atrbID = map->getNamedItem("id");
				CString ID=atrbID->Getvalue();
				result.push_back(ID);
			}
		}
		
	}
	}
	catch(...)
	{
		assert(false);
	}
	return result;
}
void cmpDocument::CreateCSSStyleDeclaration(CString guid)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr stylesNode= canvasNode->selectSingleNode("Styles");
		if(stylesNode==NULL)
		{
			stylesNode = pDom->createElement("Styles");
			canvasNode->appendChild(stylesNode);
		}
		MSXML2::IXMLDOMElementPtr styleNode = pDom->createElement("style");
		_bstr_t bs=guid;
		styleNode->setAttribute("id",bs);
		stylesNode->appendChild(styleNode);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
bool cmpDocument::SetPersist(CString compID,CString xmlString)
{
	try
	{
		if(xmlString.IsEmpty())
		{
			return true;//there is nothing to set - component does not have the functionality yet
		}
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode=NULL;
		compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{	
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		MSXML2::IXMLDOMNodePtr persistNode= compNode->selectSingleNode("Persist");
		IXMLDOMDocument2Ptr pDOM2;
		pDOM2.CreateInstance(__uuidof(DOMDocument40));
		pDOM2->PutpreserveWhiteSpace(VARIANT_FALSE);
		//if we come the first time we just uppend the whole persist string
		if(persistNode==NULL)
		{
			persistNode = pDom->createElement("Persist");
			compNode->appendChild(persistNode);
			VARIANT_BOOL loadSuccess =  pDOM2->loadXML(_bstr_t(xmlString));
			if(loadSuccess==VARIANT_TRUE)
			{
				MSXML2::IXMLDOMNodePtr root = pDOM2->documentElement;
				persistNode->appendChild(root->cloneNode(VARIANT_TRUE));
			}
			return true;
		}
		//if we got here we have to merge persist
		MSXML2::IXMLDOMNodePtr child=persistNode->GetfirstChild();
		CArrayArgument oldArr;
		oldArr.loadString(child->Getxml());
		int nOldNames=oldArr.size();
		//the key in the map is the property name; it contains indexes of the arguments in oldArr
		std::map <CString,int> oldNameNodes;
		for(int i=0;i<nOldNames;i++)
		{
			CArgument* a=oldArr.get(i);
			if(a->getType()==CArgument::object)
			{
				CObjectArgument* pObj=(CObjectArgument*)a;
				CStringArgument* pNameArg=(CStringArgument*)(pObj->get("name"));
				CString propName=pNameArg->getValue();
				oldNameNodes[propName]=i;
			}
		}
				
		CArrayArgument arrArg;
		arrArg.loadString(xmlString);
		int n = arrArg.size();
		for(int i=0;i<n;i++)
		{
			CArgument *pArg = arrArg.get(i);
			if( pArg->getType() == CArgument::object)
			{
				CObjectArgument *pObjArg = (CObjectArgument*) pArg;
				CStringArgument* pArName=(CStringArgument*)(pObjArg->get(_T("name")));
				CString propName=pArName->getValue();
				CArgument* pArValue=pObjArg->get(_T("value"));
				if(oldNameNodes.find(propName)==oldNameNodes.end())
				{
					//the property is new - append it to array
					oldArr.add(pObjArg->clone());
				}
				else
				{
					int index=oldNameNodes[propName];
					((CObjectArgument*)oldArr.get(index))->set(_T("value"),pArValue->clone());
				}
			}
		}
		CString strRes;
		oldArr.saveString(&strRes);
		VARIANT_BOOL loadSuccess =  pDOM2->loadXML(_bstr_t(strRes));
		if(loadSuccess==VARIANT_TRUE)
		{
			MSXML2::IXMLDOMNodePtr root = pDOM2->documentElement;
			persistNode->replaceChild(root->cloneNode(VARIANT_TRUE),child);
		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
CString cmpDocument::GetPersist(CString compID)
{
	CString res;
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode=NULL;
		compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{	
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return res;
		}
		MSXML2::IXMLDOMNodePtr persistNode= compNode->selectSingleNode("Persist");
		if(persistNode==NULL)
		{
			return res;
		}
		MSXML2::IXMLDOMNodePtr child=persistNode->GetfirstChild();
		if(child==NULL)
		{
			return res;
		}
		_bstr_t bs=child->Getxml();
		res=CString((TCHAR*)bs);
	}
	catch(...)
	{
		assert(false);
	}
	return res;
}
void cmpDocument::SetExportSettings(bool bUseCurrent,CString path)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMElementPtr settNode=NULL;
		MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
		settNode = canvasNode->selectSingleNode("ExportSettings");
		if(!settNode)
		{
			settNode = pDom->createElement(_T("ExportSettings"));
			canvasNode->appendChild(settNode);
		}
			settNode->setAttribute(_T("useCurrent"),bUseCurrent);
			_bstr_t bs=path;
			settNode->setAttribute(_T("path"),bs);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
bool cmpDocument::GetExportSettings(bool& bUseCurrent,CString& path)
{
	bUseCurrent = true;
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMElementPtr settNode=NULL;
		MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
		settNode = canvasNode->selectSingleNode("ExportSettings");
		if(!settNode)
		{
			return false;
		}
		MSXML2::IXMLDOMNamedNodeMapPtr map = settNode->Getattributes();
		if(!map)
			return false;
		bstr_t b=_T("useCurrent");
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
		if(!atrb)
			return false;
		bUseCurrent=atrb->Getvalue();
		b=_T("path");
		MSXML2::IXMLDOMAttributePtr atrb1 = map->getNamedItem(b);
		if(!atrb1)
			return false;
		_bstr_t bspath=atrb1->Getvalue();
		path=(LPCTSTR)bspath;
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
void cmpDocument::SetAirSettings(const AirSettings& settings) //GV
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
	MSXML2::IXMLDOMElementPtr airNode = canvasNode->selectSingleNode("AirSettings");
	if(!airNode) {
		airNode = pDom->createElement(_T("AirSettings"));
		canvasNode->appendChild(airNode);
	}
	_bstr_t bs = settings.name;
	airNode->setAttribute(_T("name"),bs);
	bs = settings.id;
	airNode->setAttribute(_T("id"),bs);
	bs = settings.version;
	airNode->setAttribute(_T("version"),bs);
	bs = settings.desc;
	airNode->setAttribute(_T("description"),bs);
	bs = settings.copyright;
	airNode->setAttribute(_T("copyright"),bs);
	bs = settings.img16;
	airNode->setAttribute(_T("img16"),bs);
	bs = settings.img32;
	airNode->setAttribute(_T("img32"),bs);
	bs = settings.img48;
	airNode->setAttribute(_T("img48"),bs);
	bs = settings.img128;
	airNode->setAttribute(_T("img128"),bs);

	airNode->setAttribute(_T("custom"),settings.custom);
	airNode->setAttribute(_T("transparent"),settings.transparent);
	bs = settings.path;
	airNode->setAttribute(_T("path"),bs);
	airNode->setAttribute("useCustomSize",settings.bCustomWindowSize);
	airNode->setAttribute("windowWidth",settings.nWinWidth);
	airNode->setAttribute("windowHeight", settings.nWinHeight);
}
bool cmpDocument::GetAirSettings(AirSettings& settings) //GV
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
	MSXML2::IXMLDOMElementPtr airNode = canvasNode->selectSingleNode("AirSettings");
	if(!airNode) return false;

	MSXML2::IXMLDOMNamedNodeMapPtr map = airNode->Getattributes();
	if(!map) return false;

	bstr_t b =_T("name");
	MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.name = (LPCTSTR)b;

	b =_T("id");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.id = (LPCTSTR)b;

	b =_T("version");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.version = (LPCTSTR)b;

	b =_T("description");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.desc = (LPCTSTR)b;

	b =_T("copyright");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.copyright = (LPCTSTR)b;

	b =_T("img16");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.img16 = (LPCTSTR)b;

	b =_T("img32");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.img32 = (LPCTSTR)b;

	b =_T("img48");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.img48 = (LPCTSTR)b;

	b =_T("img128");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.img128 = (LPCTSTR)b;

	b =_T("custom");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	settings.custom = atrb->Getvalue();

	b =_T("transparent");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	settings.transparent = atrb->Getvalue();

	b =_T("path");
	atrb = map->getNamedItem(b);
	if(!atrb) return false;
	b = atrb->Getvalue();
	settings.path = (LPCTSTR)b;

	atrb = map->getNamedItem("useCustomSize");
	if(!atrb)
	{
		settings.bCustomWindowSize=false;
	}
	else
	{
		settings.bCustomWindowSize=atrb->Getvalue();
	}
	int nCanvasWidth, nCanvasHeight;
	GetCanvasSize(nCanvasWidth, nCanvasHeight);
	atrb = map->getNamedItem("windowWidth");
	if(!atrb)
	{
		settings.nWinWidth=nCanvasWidth;
	}
	else
	{
		settings.nWinWidth=atrb->Getvalue();
	}
	atrb = map->getNamedItem("windowHeight");
	if(!atrb)
	{
		settings.nWinHeight=nCanvasHeight;
	}
	else
	{
		settings.nWinHeight=atrb->Getvalue();
	}
	return true;

}
void cmpDocument::GetAirWindowSize(int& nWidth, int& nHeight)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
	MSXML2::IXMLDOMElementPtr airNode = canvasNode->selectSingleNode("AirSettings");
	if(!airNode) 
		return GetCanvasSize(nWidth, nHeight);

	MSXML2::IXMLDOMNamedNodeMapPtr map = airNode->Getattributes();
	if(!map) 
		return GetCanvasSize(nWidth, nHeight);
	MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("useCustomSize");
	if(!atrb)
	{
		return GetCanvasSize(nWidth, nHeight);
	}
	else
	{
		_bstr_t t=_bstr_t(atrb->Getvalue());
		if(t==_bstr_t("0"))
		{
			return GetCanvasSize(nWidth, nHeight);
		}
	}
	MSXML2::IXMLDOMAttributePtr atrb1 = map->getNamedItem("windowWidth");
	MSXML2::IXMLDOMAttributePtr atrb2 = map->getNamedItem("windowHeight");
	if(!atrb1 || !atrb2)
	{
		return GetCanvasSize(nWidth, nHeight);
	}
	nWidth=atrb1->Getvalue();
	nHeight=atrb2->Getvalue();
}

bool cmpDocument::FindComponentByProperty(CString className,CString propertyName,CString propertyValueXML,bool bCompIsConn)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get node list
		MSXML2::IXMLDOMNodeListPtr nodelist;
		if(bCompIsConn)
		{
			nodelist = pDom->selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
		}
		else
		{
			nodelist = pDom->selectNodes("/CXCanvas/component[@className='" + _bstr_t(className) + "']");
		}
		
		int n = nodelist->Getlength();
		for(int i=0;i<n;i++)
		{
			MSXML2::IXMLDOMNodePtr compNode=nodelist->Getitem(i);
			MSXML2::IXMLDOMNodePtr propertiesNode = compNode->selectSingleNode("properties");
			if(propertiesNode != NULL)
			{
				MSXML2::IXMLDOMNodePtr propNode=NULL;
				propNode = propertiesNode->selectSingleNode("./property//name[./string = '" + _bstr_t(propertyName) + "']/following-sibling::*");
				if(propNode != NULL)
				{
					MSXML2::IXMLDOMNodePtr oldValue = propNode->GetfirstChild();
					if ( oldValue != NULL )
					{
						CString valueXML=(LPCTSTR)(oldValue->Getxml());
						if(valueXML==propertyValueXML)
						return true;
					}
				}
			}

		}
		return false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


bool cmpDocument::GetSize(const TCHAR* fn,std::wstring warningImage)
{
	FILE *f = _tfopen(fn,_T("rb"));
	if(f)
	{
	fpos_t flen;
	fseek(f, 0, SEEK_END);
	fgetpos(f, &flen); 
	fclose(f);
	unsigned long rlen=(unsigned long)flen;

	if(rlen>1000000)
	{
		if(IDOK==MessageBox(NULL, warningImage.c_str(),m_csWarningTitle, MB_OKCANCEL))
			return true;
		else
			return false;
	}
	else
	return true;
	}
	else
		return true;
	
}

bool cmpDocument::ConvertFiles(const TCHAR *filename, CString &tempFile,bool embed)
{
	try
	{
	//we should probably move this function up to XcelsiusDoc
	std::wstring fileExt;
	fileExt = ::PathFindExtension( filename);

	//convert other files here later.
	//we need to support other file formats
	//if( _tcsicmp(_T(".swf"), fileExt.c_str()) != 0 && _tcsicmp(_T(".jpg"), fileExt.c_str()) != 0)
//	if( fileExt != _T(".swf") && fileExt != _T(".jpg") )
	{
//#ifdef _DEBUG
	//	MessageBox(NULL, _T("Only SWF and JPG support.  Remind Ryan to create converter and remove this dialog."), _T("Warning"), MB_OK);
//#endif
		//return false;
	}

	//pass thru SWF and jpeg
	//also if it's an XC swf, we'll need to probably disallow embedding

	//create a tempfile
	tempFile = _T("\\");
	tempFile += DocGUID.c_str();
	tempFile += _T("\\im");

	std::wstring uniqExt;

	if((_tcsicmp(fileExt.c_str(),_T(".gif"))==0)||(_tcsicmp(fileExt.c_str(),_T(".bmp"))==0)||(_tcsicmp(fileExt.c_str(),_T(".png"))==0))
		uniqExt=_T(".jpg");
	else
		uniqExt=fileExt;

	std::wstring tmpname = imutil::GetUnique(tempFile, uniqExt.c_str());

	//make sure the path exists
	TCHAR *buff = _tcsdup(tmpname.c_str());
	::PathRemoveFileSpec(buff);
	_tmkdir(buff);
	delete [] buff;

	tempFile = tmpname.c_str();

	std::wstring name=tmpname.substr(0,tmpname.find_last_of(_T(".")));
	name=name+_T(".jpg");

	//TODO: Handle uppercase

	if((_tcsicmp(fileExt.c_str(),_T(".gif"))==0)||(_tcsicmp(fileExt.c_str(),_T(".bmp"))==0)||(_tcsicmp(fileExt.c_str(),_T(".png"))==0))
	{

		//Calculate the size
		if(embed) {
		if(!GetSize(filename,warningImage))
			return false;
		}

		CImage myimage;
		myimage.Load(filename);		
		myimage.Save(name.c_str());		
		tempFile=name.c_str();
		return true;
	}
	else if( _tcsicmp(_T(".swf"), fileExt.c_str()) != 0 && _tcsicmp(_T(".jpg"), fileExt.c_str()) != 0)
		return false;

	if(_tcsicmp(fileExt.c_str(),_T(".jpg"))==0)
	{
		if(embed) {
		if(!GetSize(filename,warningImage))
			return false;
		}
	}


	return CopyFile(filename, tempFile, false) == TRUE?true:false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}



std::wstring cmpDocument::CreateAssetProperty(const TCHAR *assetNode,MSXML2::IXMLDOMElementPtr lastNode)
{
	//std::wstring result = _T("<array>");
	
	MSXML2::IXMLDOMElementPtr arrNode = pAssetProp->createElement("array");	
	if(lastNode==NULL)
		pAssetProp->appendChild(arrNode);
	else
		lastNode->appendChild(arrNode);

	//we need to parse the assets node and create the equivalent property value
	IXMLDOMDocument2Ptr ptemp;
	ptemp.CreateInstance(__uuidof(DOMDocument40));
	ptemp->loadXML( _bstr_t(assetNode) );
	MSXML2::IXMLDOMNodePtr parentNode = ptemp->GetfirstChild();

	MSXML2::IXMLDOMNodeListPtr assets = parentNode->selectNodes("asset");
	if(assets && assets->Getlength())
	{
		for(int i=0; i<assets->Getlength(); i++)
		{
			//CString prop;
			//prop.Format(_T("<property id=\"%d\">"), i);
			//result += prop;

			MSXML2::IXMLDOMElementPtr propNode = pAssetProp->createElement("property");
			std::wostringstream oss;oss<<i;
			std::wstring num=oss.str();
			propNode->setAttribute("id",num.c_str());
			arrNode->appendChild(propNode);

			MSXML2::IXMLDOMNodePtr anode = assets->Getitem(i);
			MSXML2::IXMLDOMNodePtr anodeList = anode->selectSingleNode("array");
			if( anodeList )
			{
				//Lazy way, This should be redone before release
				CreateAssetProperty(anodeList->Getxml(),propNode);				

			}
			else
			{
				MSXML2::IXMLDOMNodePtr valueNode = anode->selectSingleNode("temporaryFile");
				if(valueNode)
				{
					//std::wstring v = valueNode->Gettext();
					//prop.Format(_T("<string>%s</string>"), v.c_str());
					//result += prop;

					MSXML2::IXMLDOMElementPtr strNode = pAssetDoc->createElement("string");
					strNode->text =  valueNode->Gettext();
					propNode->appendChild(strNode);


				}
			}
			//result += _T("</property>");
		}
	}

	//result += _T("</array>");
	std::wstring result = pAssetProp->xml;
	return result;
}

void cmpDocument::CreateAssetValue(const TCHAR *tempFile, const TCHAR *origFile, const TCHAR *embed,MSXML2::IXMLDOMElementPtr assetENode)
{
	//std::wstring result;
	MSXML2::IXMLDOMElementPtr tempNode = pAssetDoc->createElement("temporaryFile");
	tempNode->text = tempFile;
	MSXML2::IXMLDOMElementPtr origNode = pAssetDoc->createElement("originalFile");
	origNode->text = origFile;
	MSXML2::IXMLDOMElementPtr embedNode = pAssetDoc->createElement("embed");
	embedNode->text = embed;

	assetENode->appendChild(tempNode);
	assetENode->appendChild(origNode);
	assetENode->appendChild(embedNode);
	

	/*
	std::wstring temp = _T("<temporaryFile>"); temp+= tempFile; temp += _T("</temporaryFile>");
	std::wstring orig = _T("<originalFile>"); orig+= origFile; orig += _T("</originalFile>");
	std::wstring embedMethod = _T("<embed>"); embedMethod+= embed; embedMethod += _T("</embed>");

	result = temp;
	result += orig;
	result += embedMethod;

	return result;*/
}

bool cmpDocument::CreateAssetArray(CArrayArgument *filenames, CArrayArgument *embed,MSXML2::IXMLDOMElementPtr lastNode)
{



	MSXML2::IXMLDOMElementPtr arrNode = pAssetDoc->createElement("array");	

	if(lastNode==NULL)
		pAssetDoc->appendChild(arrNode);
	else
		lastNode->appendChild(arrNode);	


	//*

	//only return false if one of the images failed to convert
	//result = _T("");

	//loop through each 
	
	int count = filenames->size();

	if( count != embed->size() || count == 0)
		return true;

	//result = _T("<array>");

	for( int i=0; i<count; i++)
	{
		CArgument *file = filenames->get(i);


		MSXML2::IXMLDOMElementPtr assetNode = pAssetDoc->createElement("asset");
		std::wostringstream oss;oss<<i;
		std::wstring num=oss.str();
		assetNode->setAttribute("id",num.c_str());
		arrNode->appendChild(assetNode);

		if( file->getType() == CArgument::string )
		{	
			CStringArgument *embedType= (CStringArgument*)embed->get(i);
			CStringArgument *argString = (CStringArgument*)file;

			CString tempFile = _T("");
			if( _tcscmp(embedType->getValue(),_T("url")) )//convert anything but URL 
			{
				bool emb=false;
				if((_tcscmp(embedType->getValue(),_T("embed"))==0)||(_tcscmp(embedType->getValue(),_T("true"))==0))
					emb=true;

				
				bool res = ConvertFiles((LPCTSTR)argString->getValue(), tempFile,emb);
				if(res==false)
				{
					if(assetsFromXlf.find((LPCTSTR)argString->getValue())==assetsFromXlf.end())
					{
						return false; //one of the images failed.
					}
					else
					tempFile=(assetsFromXlf[(LPCTSTR)argString->getValue()]).c_str();
				}
				
			}
			else
				tempFile = argString->getValue();//just duplicate actual path

		
			CreateAssetValue(tempFile, argString->getValue(), embedType->getValue(),assetNode);
		}
		else
		if( file->getType() == CArgument::array )
		{
			CArrayArgument *argArray =(CArrayArgument*)file;
			CArrayArgument *embedArray= (CArrayArgument*)embed->get(i);
			bool res = CreateAssetArray(argArray, embedArray,assetNode);
			if(res==false) return false;
		}
		/*

		CString nodeString;
		nodeString.Format(_T("<asset id=\"%d\">"), i);
		nodeString += assetNode.c_str();
		nodeString += _T("</asset>");

		result += nodeString;*/
	}

	//result += _T("</array>");

	return true;
}

bool cmpDocument::SetAssets(const TCHAR *compID, const TCHAR *propName, CArrayArgument *fileName, CArrayArgument *embed, CString &valueXML)
{

	//find the component node
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodePtr compNode=NULL;
	compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
	{
		compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
	}
	if(!compNode) return false;

	//check that the arrays are same dimension
	if( fileName->size() != embed->size())
		return false;

	//does the meat of everything
	std::wstring asset;

	pAssetDoc = NULL;
	pAssetDoc.CreateInstance("Msxml2.DOMDocument.4.0");

	bool result = CreateAssetArray( fileName, embed );
	if(!result) return false;


	//need the assets node to set the asset
	MSXML2::IXMLDOMNodePtr assetsNode = compNode->selectSingleNode("./assets");

	//if no assets node, then make one
	if(!assetsNode) 
	{
		assetsNode = pDom->createElement("assets");
		if(compNode) compNode->appendChild(assetsNode);
	}

	//find asset
	CString pname = propName;
	CString assetPath = "./asset/name[./string = '"+pname+"']";
	MSXML2::IXMLDOMNodePtr oldAssetNode;
	if( assetsNode->selectSingleNode(_bstr_t(assetPath)) )
		oldAssetNode = assetsNode->selectSingleNode(_bstr_t(assetPath))->GetparentNode();

	MSXML2::IXMLDOMNodePtr newAssetNode = pDom->createElement("asset");
	MSXML2::IXMLDOMNodePtr nameNode = pDom->createElement("name");
	MSXML2::IXMLDOMNodePtr nameChild = pDom->createElement("string");
	nameChild->text = propName;
	MSXML2::IXMLDOMNodePtr valueNode = pDom->createElement("value");

	newAssetNode->appendChild(nameNode);
	newAssetNode->appendChild(valueNode);
	nameNode->appendChild(nameChild);

	//begin lame way to create a node from markup in MSXML2
	IXMLDOMDocument2Ptr ptemp;
	ptemp.CreateInstance(__uuidof(DOMDocument40));
	ptemp->loadXML( pAssetDoc->xml );
	MSXML2::IXMLDOMNodePtr bindTemp = ptemp->GetfirstChild();
	MSXML2::IXMLDOMNodePtr bindings = bindTemp->cloneNode(VARIANT_TRUE);

	valueNode->appendChild(bindings);
	//end lameness

	if(oldAssetNode)
		assetsNode->replaceChild(newAssetNode, oldAssetNode);
	else
		assetsNode->appendChild(newAssetNode);

	pAssetProp = NULL;
	pAssetProp.CreateInstance("Msxml2.DOMDocument.4.0");
	//need the properties node to set the property..this is going to be an array of stuff
	valueXML = CreateAssetProperty(pAssetDoc->xml).c_str();

	return true;
}

bool cmpDocument::SetCanvasAsset(const TCHAR *propName, const TCHAR *fileName, bool embed, CString &valueXML)
{
	try
	{
		CString temp;
		if( ConvertFiles(fileName, temp, embed) == false ) return false;

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode=NULL;
		compNode = pDom->selectSingleNode("//canvas");

		if(!compNode)
		{
			compNode = pDom->createElement("canvas");
			MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
			canvasNode->appendChild(compNode);
		}


		//need the assets node to set the asset
		MSXML2::IXMLDOMNodePtr assetsNode = compNode->selectSingleNode("./assets");

		//if no assets node, then make one
		if(!assetsNode) 
		{
			assetsNode = pDom->createElement("assets");
			if(compNode) compNode->appendChild(assetsNode);
		}

		setAsset(assetsNode, propName, fileName, temp, embed);

		//need the properties node to set the property
		valueXML = _T("<string>");
		valueXML += temp;
		valueXML += _T("</string>");

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

bool cmpDocument::SetAsset(const TCHAR *compID, const TCHAR *propName, const TCHAR *fileName, bool embed, CString &valueXML)
{
	try
	{
		CString temp;
		if( ConvertFiles(fileName, temp,embed) == false ) return false;

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode=NULL;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}

		if(!compNode) return false;


		//need the assets node to set the asset
		MSXML2::IXMLDOMNodePtr assetsNode = compNode->selectSingleNode("./assets");

		//if no assets node, then make one
		if(!assetsNode) 
		{
			assetsNode = pDom->createElement("assets");
			if(compNode) compNode->appendChild(assetsNode);
		}

		setAsset(assetsNode, propName, fileName, temp, embed);

		//need the properties node to set the property
		valueXML = _T("<string>");
		valueXML += temp;
		valueXML += _T("</string>");

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

void cmpDocument::setAsset(const MSXML2::IXMLDOMNodePtr assetsNode, const TCHAR *propName, const TCHAR * originalFileName, const TCHAR * tmpFileName, bool embed)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	//find asset
	CString pname = propName;
	CString assetPath = "./asset/name[./string = '"+pname+"']";
	MSXML2::IXMLDOMNodePtr oldAssetNode;
	if( assetsNode->selectSingleNode(_bstr_t(assetPath)) )
		oldAssetNode = assetsNode->selectSingleNode(_bstr_t(assetPath))->GetparentNode();

	MSXML2::IXMLDOMNodePtr newAssetNode = pDom->createElement("asset");
	MSXML2::IXMLDOMNodePtr nameNode = pDom->createElement("name");
	MSXML2::IXMLDOMNodePtr valueNode = pDom->createElement("value");
	MSXML2::IXMLDOMNodePtr nameChild = pDom->createElement("string");
	nameChild->text = propName;

	newAssetNode->appendChild(nameNode);
	newAssetNode->appendChild(valueNode);
	nameNode->appendChild(nameChild);

	MSXML2::IXMLDOMNodePtr tempFileName = pDom->createElement("temporaryFile");
	tempFileName->text = _bstr_t(tmpFileName);
	valueNode->appendChild(tempFileName);

	MSXML2::IXMLDOMNodePtr origFileName = pDom->createElement("originalFile");
	origFileName->text = originalFileName;
	valueNode->appendChild(origFileName);

	MSXML2::IXMLDOMNodePtr embedTag = pDom->createElement("embed");
	embedTag->text = embed==true?"true":"false";
	valueNode->appendChild(embedTag);

	if(oldAssetNode)
		assetsNode->replaceChild(newAssetNode, oldAssetNode);
	else
		assetsNode->appendChild(newAssetNode);
}

CString cmpDocument::GetAsset(const TCHAR *compID)
{
	try
	{
		CString asset=_T("");

		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode=NULL;
		compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}

		if(compNode)
		{
			//need the assets node to set the asset
			MSXML2::IXMLDOMNodePtr assetsNode = compNode->selectSingleNode("./assets");
			TCHAR *buff = assetsNode->xml;

			asset = buff;
		}

		return asset;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}


bool cmpDocument::SetEmbed(const TCHAR *compID, const TCHAR *propName, bool embed)
{
	try
	{

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode=NULL;
		compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}

		if(!compNode) return false;


		//need the assets node to set the asset
		MSXML2::IXMLDOMNodePtr assetsNode = compNode->selectSingleNode("./assets/asset/name[string='" + _bstr_t(propName) + "']/parent::*");

		//if no assets node, return
		if(!assetsNode) return false;

		//we need the temporary file name and to change the embed
		MSXML2::IXMLDOMElementPtr embedNode = assetsNode->selectSingleNode("./value/embed");

		if(!embedNode) return false;

		//set the embed flag
		embedNode->text = embed?_T("true"):_T("false");	
	
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

void cmpDocument::DeleteAllConnections()
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get CXCanvas node
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodeListPtr nodelist = canvasNode->selectNodes("connection");
		int n = nodelist->Getlength();
		for(int i=0; i<n; i++ )
		{
			MSXML2::IXMLDOMNodePtr connNode = nodelist->Getitem(i);
			canvasNode->removeChild(connNode);
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
std::vector<CString> cmpDocument::ApplyColorSchemeToExistingComponents(CString csColorScheme)
{
	std::vector<CString> ids;
	try
	{
		//returns vector of component ids where it was applied
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodeListPtr nodelist = canvasNode->selectNodes("//component");
		int n = nodelist->Getlength();
		for(int i=0; i<n; i++ )
		{
			MSXML2::IXMLDOMNodePtr compNode = nodelist->Getitem(i);
			MSXML2::IXMLDOMNodePtr colorSchemeNode=compNode->selectSingleNode("colorScheme");
			if(!colorSchemeNode)
			{
				colorSchemeNode = pDom->createElement("colorScheme");
				compNode->appendChild(colorSchemeNode);
			}
			colorSchemeNode->text = _bstr_t(csColorScheme);
			//Get id
			MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
			_bstr_t b=_T("id");
			MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem(b);
			_bstr_t id=atrb->Getvalue();
			CString csId=(TCHAR*)id;
			ids.push_back(csId);
		}
	}
	catch(...)
	{
		assert(false);
	}
	return ids;
}
void cmpDocument::SetCurrentColorScheme(CString csColorScheme)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr colorSchemeNode=canvasNode->selectSingleNode("colorScheme");
		if(!colorSchemeNode)
		{
			colorSchemeNode = pDom->createElement("colorScheme");
			canvasNode->appendChild(colorSchemeNode);
		}
		colorSchemeNode->text = _bstr_t(csColorScheme);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}

const TCHAR* cmpDocument::GetColorScheme(const TCHAR* id)
{
	return colorSchemesXMLMap[id];
}


CString cmpDocument::GetCurrentColorSchemeID()
{
	CString res;
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr colorSchemeNode=canvasNode->selectSingleNode("colorScheme");
		if(!colorSchemeNode)
		{
			return res;
		}
		res=(TCHAR*)(colorSchemeNode->text);
	}
	catch(...)
	{
		assert(false);
	}
	return res;
}
std::set<CString> cmpDocument::GetColorSchemeNames()
{
	std::set<CString> colorSchemeSet;
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodeListPtr nodelist=canvasNode->selectNodes("component/colorScheme");
		int n=nodelist->Getlength();
		for(int i=0;i<n;i++)
		{
			_bstr_t bs=nodelist->Getitem(i)->Gettext();
			CString str=(TCHAR*)bs;
			colorSchemeSet.insert(str);
		}
		MSXML2::IXMLDOMNodePtr canvasSchemeNode=canvasNode->selectSingleNode("colorScheme");
		if(canvasSchemeNode)
		{
			CString str=(TCHAR*)canvasSchemeNode->Gettext();
			colorSchemeSet.insert(str);
		}
	}
	catch(...)
	{
		assert(false);
	}
	return colorSchemeSet;
}
void cmpDocument::SaveColorSchemes(std::map<CString,CString> xmlMap)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr oldSchemesNode= canvasNode->selectSingleNode("ColorSchemes");
		MSXML2::IXMLDOMNodePtr schemesNode=pDom->createElement("ColorSchemes");

		if(!oldSchemesNode)
		{
			canvasNode->appendChild(schemesNode);
		}
		else
		{
			canvasNode->replaceChild(schemesNode,oldSchemesNode);
		}
		std::set<CString> names=GetColorSchemeNames();
		for(std::map<CString,CString>::const_iterator itr = xmlMap.begin(); itr != xmlMap.end(); ++itr )
		{
			CString csName=itr->first;
			if(csName.IsEmpty())
				continue;//do nothing for default
			if(names.find(csName)==names.end())//don't save if it's not referenced
			{
				continue;
			}
			CString csXML=itr->second;
			IXMLDOMDocument2Ptr pTempDom;
			pTempDom.CreateInstance(__uuidof(DOMDocument40));
			/* VARIANT_BOOL loadSuccess = */ pTempDom->loadXML(_bstr_t(csXML));
			MSXML2::IXMLDOMNodePtr pRoot=pTempDom->selectSingleNode("Root");
			MSXML2::IXMLDOMNodePtr pClone=pRoot->cloneNode(VARIANT_TRUE);
			MSXML2::IXMLDOMElementPtr schemeNode=pDom->createElement("Scheme");
			schemesNode->appendChild(schemeNode);
			schemeNode->setAttribute("name",_bstr_t(csName));
			schemeNode->appendChild(pClone);
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
void cmpDocument::LoadColorSchemes()
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr schemesNode= canvasNode->selectSingleNode("ColorSchemes");
		if(!schemesNode)
		{
			return;
		}
		MSXML2::IXMLDOMNodeListPtr list=schemesNode->selectNodes("scheme/Root");
		int n = list->Getlength();
		for(int i=0;i<n;i++)
		{
			MSXML2::IXMLDOMNodePtr pRootNode= list->Getitem(i);
			MSXML2::IXMLDOMNamedNodeMapPtr map = pRootNode->Getattributes();
			MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("ID");
			CString ID=atrb->Getvalue();
			colorSchemesXMLMap[ID]=(TCHAR*)pRootNode->Getxml();
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
bool cmpDocument::ClearMetaData(CString compID,CString tag)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode;
		compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return false;
		}
		MSXML2::IXMLDOMElementPtr metaNode=compNode->selectSingleNode("metaData");
		if(!metaNode)
		{
			return false;
		}
		MSXML2::IXMLDOMNodeListPtr tagList = metaNode->selectNodes("tag");
		if( tagList )
		{
			int count = tagList->Getlength();
			for(int i=0; i<count; i++)
			{
				MSXML2::IXMLDOMNodePtr node = tagList->Getitem(i);
				if((TCHAR*)node->Gettext()==tag)
				{
					metaNode->removeChild(node);
					return true;
				}
			}
		}
		return false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
bool cmpDocument::SetMetaData(CString compID,CString tag, CString data)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode;
		compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return false;
		}
		MSXML2::IXMLDOMElementPtr metaNode=compNode->selectSingleNode("metaData");
		if(!metaNode)
		{
			metaNode=pDom->createElement("metaData");
			compNode->appendChild(metaNode);
		}
		MSXML2::IXMLDOMElementPtr tagNode=metaNode->selectSingleNode("tag");
		if(!tagNode)
		{
			tagNode=pDom->createElement("tag");
			metaNode->appendChild(tagNode);
		}
		tagNode->Puttext(_bstr_t(tag));
		MSXML2::IXMLDOMElementPtr dataNode=metaNode->selectSingleNode("data");
		if(!dataNode)
		{
			dataNode=pDom->createElement("data");
			metaNode->appendChild(dataNode);
		}
		dataNode->Puttext(_bstr_t(data));
	/*	removed as data is just a string
		MSXML2::IXMLDOMElementPtr oldDataNode=metaNode->selectSingleNode("data");
		MSXML2::IXMLDOMElementPtr dataNode;//=pDom->createElement("data");
		IXMLDOMDocument2Ptr pDOM2;
		pDOM2.CreateInstance(__uuidof(DOMDocument40));
		pDOM2->PutpreserveWhiteSpace(VARIANT_FALSE);
		VARIANT_BOOL loadSuccess = pDOM2->loadXML(_bstr_t(data));
		MSXML2::IXMLDOMNodePtr root = pDOM2->documentElement;
		//Set data node to clone of passed in xml
		dataNode=root->cloneNode(VARIANT_TRUE);
		if(oldDataNode)
		{
			metaNode->replaceChild(dataNode,oldDataNode);
		}
		else
		{
			metaNode->appendChild(dataNode);
		}
	*/
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
void cmpDocument::SetComponentColorScheme(CString compID,CString csColorSchemeID)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		//Get compnent node
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return;
		}
		MSXML2::IXMLDOMElementPtr colorSchemeNode=compNode->selectSingleNode("colorScheme");
		if(!colorSchemeNode)
		{
			colorSchemeNode=pDom->createElement("colorScheme");
			compNode->appendChild(colorSchemeNode);
		}
		colorSchemeNode->Puttext(_bstr_t(csColorSchemeID));
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
void cmpDocument::SetCurrentSkin(CString csSkin)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr skinNode=canvasNode->selectSingleNode("colorTheme");
		if(!skinNode)
		{
			skinNode = pDom->createElement("colorTheme");
			canvasNode->appendChild(skinNode);
		}
		skinNode->text = _bstr_t(csSkin);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
CString cmpDocument::GetCurrentSkin()
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr canvasNode;
		CString res;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodePtr skinNode=canvasNode->selectSingleNode("colorTheme");
		if(skinNode)
		{
			res=(TCHAR*)(skinNode->text);
		}
		return res;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}
void cmpDocument::SetDisplayName(const TCHAR *compID,const TCHAR *displayName)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		//Get component node
		MSXML2::IXMLDOMElementPtr compNode;
		compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return;
		}
		_bstr_t dispName = _bstr_t(displayName);
		compNode->setAttribute("displayName",displayName);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}

int cmpDocument::GetAssets(std::vector<std::wstring> &assets,bool save)
{
	try
	{
		//empty list
		assets.clear();

		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		MSXML2::IXMLDOMNodeListPtr assetList = pDom->selectNodes("//temporaryFile");
		if( assetList )
		{
			int count = assetList->Getlength();
			for(int i=0; i<count; i++)
			{
				BSTR b;
				MSXML2::IXMLDOMNodePtr node = assetList->Getitem(i);

				MSXML2::IXMLDOMNodePtr parent = node->GetparentNode();

				//don't archive if it's a URL
				if(parent)
				{
					MSXML2::IXMLDOMNodePtr embed = parent->selectSingleNode("./embed");
					if(embed)
					{
						if (_tcscmp(embed->Gettext(), _T("url")) == 0)
							continue; //don't copy this file
					}
				}

				node->get_text(&b);

				if(_tcslen( b ) )
					assets.push_back(b);
			}
		}		

		if(save)
		assetsXLF=assets;

		return assets.size();
	}
	catch(...)
	{
		assert(false);
		return 0;
	}
}



void cmpDocument::LoadAssetMap()
{
	try
	{
		
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		int acount=0;
		MSXML2::IXMLDOMNodeListPtr assetList = pDom->selectNodes("//originalFile");
		if( assetList )
		{
			int count = assetList->Getlength();
			for(int i=0; i<count; i++)
			{
				BSTR b;
				MSXML2::IXMLDOMNodePtr node = assetList->Getitem(i);
				MSXML2::IXMLDOMNodePtr parent = node->GetparentNode();

				//don't archive if it's a URL
				if(parent)
				{
					MSXML2::IXMLDOMNodePtr embed = parent->selectSingleNode("./embed");
					if(embed)
					{
						if (_tcscmp(embed->Gettext(), _T("url")) == 0)
							continue; //don't copy this file
					}
				}

				node->get_text(&b);

				if(_tcslen( b ) )
				{
					//get the fixed asset path

					
					assetsFromXlf[b]=assetMapped[assetsXLF[acount]];

					acount++;
				}
			}
		}		

	}
	catch(...)
	{
		assert(false);
//		return 0;
	}
}

int
cmpDocument::GetExternalAssetCount()
{
	int rv = 0;
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	if (pDom) {
		CString txt = (LPCTSTR)pDom->Getxml();
		MSXML2::IXMLDOMNodeListPtr list = pDom->selectNodes("//assets/asset/value[embed=\"false\"]");
		if (list) {
			rv = list->Getlength();
		}
	}
	return rv;
}

void cmpDocument::FixAssetPaths(std::map<std::wstring, std::wstring> assetMap)
{
	try
	{
		//this function will fix the "temporaryFile" reference in the nodes to
		//this gets out of sync in case the temp directory changes (xlf migration)

		if(assetMap.size() == 0) return;

		assetMapped=assetMap;

		IXMLDOMDocument2Ptr pDOM = pXmlDOM;

		MSXML2::IXMLDOMNodeListPtr nodeList = pDOM->selectNodes("//temporaryFile");
		if( nodeList->Getlength() )
		{
			MSXML2::IXMLDOMNodePtr tempFileNode;
			BSTR b;
			std::wstring key;
			for(int i=0; i<nodeList->Getlength(); i++)
			{
				tempFileNode = nodeList->Getitem(i);
				tempFileNode->get_text(&b);

				key = b;
				if ( assetMap.find(key) != assetMap.end() ) 
				{
					tempFileNode->text = assetMap[key].c_str();

					//now find the property strings
					MSXML2::IXMLDOMNodeListPtr propNodeList = pDOM->selectNodes("//value[./string = '" + _bstr_t(key.c_str()) + "']");
					if( propNodeList )
					{
						int cnt = propNodeList->Getlength();
						for(int j=0; j<cnt; j++)
						{
							MSXML2::IXMLDOMNodePtr propNode = propNodeList->Getitem(j);
							MSXML2::IXMLDOMNodePtr stringNode = propNode->selectSingleNode("./string");
							stringNode->text = assetMap[key].c_str();
						}
					}
				}
			}
		}
		//now we need to reset properties of the components
		std::map<std::wstring,std::wstring>::iterator it = assetMap.begin();
		for (; it != assetMap.end(); ++it) 
		{
			std::wstring path = (*it).second;
			CString str1=(*it).first.c_str();
			CString str2=path.c_str();
			CString request=_T("//property[./string = '")+str1+_T("']");
			MSXML2::IXMLDOMNodeListPtr pathNodes=pDOM->selectNodes(_bstr_t(request));
			int n=pathNodes->Getlength();
			for(int i=0;i<n;i++)
			{
				MSXML2::IXMLDOMNodePtr pathNode=pathNodes->Getitem(i);
				MSXML2::IXMLDOMNodePtr stringNode=pathNode->GetfirstChild();
				if(stringNode)
				{
					stringNode->Puttext(_bstr_t(str2));
				}
			}
		}

	}
	catch(...)
	{
		assert(false);
		return;
	}
}
CString cmpDocument::GenerateName(CString templateStr)
{
	int n=1;
	CString tryStr;
	while(n<10000)//just to avoid infinite loop
	{
		tryStr.Format(_T(" %d"),n);
		tryStr=templateStr+tryStr;
		if(!IsNameUsedAlready(tryStr))
		{ 
			return tryStr;
		}
		else
		{
			n++;
		}
	}
	return tryStr;
}
bool cmpDocument::IsNameUsedAlready(CString csName)
{
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodeListPtr compList = pDom->selectNodes("//component");
		if(compList)
		{
			int count = compList->Getlength();
			for(int i=0; i<count; i++)
			{
				MSXML2::IXMLDOMNodePtr compNode = compList->Getitem(i);
				MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
				MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("displayName");
				_bstr_t bs=atrb->Getvalue();
				CString displayName=(TCHAR*)bs;
				if(displayName==csName)
					return true;
			}
		}
		compList = pDom->selectNodes("//connection");
		if(compList)
		{
			int count = compList->Getlength();
			for(int i=0; i<count; i++)
			{
				MSXML2::IXMLDOMNodePtr compNode = compList->Getitem(i);
				MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
				MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("displayName");
				_bstr_t bs=atrb->Getvalue();
				CString displayName=(TCHAR*)bs;
				if(displayName==csName)
					return true;
			}
		}
		return false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
CString cmpDocument::GetParentGroup(CString compID)
{
	CString res=_T("");
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
			return res;
		MSXML2::IXMLDOMNodePtr parentNode=compNode->GetparentNode();
		if(!parentNode)
			return res;
		MSXML2::IXMLDOMNamedNodeMapPtr map = parentNode->Getattributes();
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("className");
		_bstr_t bs=atrb->Getvalue();
		CString className=(TCHAR*)bs;
		if(className==_T("xcelsius.canvas.Group"))
		{
			atrb=map->getNamedItem("id");
			bs=atrb->Getvalue();
			res=(TCHAR*)bs;
		}
		return res;
	}
	catch(...)
	{
		return _T("");
	}
}
bool cmpDocument::IsLastMemberOfGroup(CString compID)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	try
	{
	MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
		return false;

	MSXML2::IXMLDOMNodePtr parentNode=compNode->GetparentNode();
	if(!parentNode)
		return false;
	MSXML2::IXMLDOMNamedNodeMapPtr map = parentNode->Getattributes();
	MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("className");
	_bstr_t bs=atrb->Getvalue();
	CString className=(TCHAR*)bs;
	if(className==_T("xcelsius.canvas.Group"))
	{
		MSXML2::IXMLDOMNodeListPtr nodeList=parentNode->selectNodes("component");
		if(nodeList->Getlength()==1)
		{
			return true;
		}
	}
	return false;
	}
	catch(...)
	{
		return false;
	}
}
std::map<CString,CString> cmpDocument::GetBindingInfo(CString compID,CString propName)
{
	std::map<CString,CString> res;
	try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom->selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
			return res;
		CString request=_T("bindings/property/name[./string = '")+propName+_T("']");
		MSXML2::IXMLDOMNodeListPtr nameNodes=compNode->selectNodes(_bstr_t(request));
		if(!nameNodes)
			return res;
		int n=nameNodes->Getlength();
		for(int i=0;i<n;i++)
		{
			MSXML2::IXMLDOMNodePtr bindingNode=nameNodes->Getitem(i)->GetparentNode()->selectSingleNode("value/binding");
			if(!bindingNode)
				continue;
			MSXML2::IXMLDOMNodePtr endpointNode=bindingNode->selectSingleNode("endpoint");
			if(!endpointNode)
				continue;
			MSXML2::IXMLDOMNamedNodeMapPtr map = endpointNode->Getattributes();
			MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("id");
			CString bindingID=(TCHAR*)(_bstr_t)atrb->Getvalue();
			MSXML2::IXMLDOMNodePtr chainNode=endpointNode->selectSingleNode("chain");
			if(!chainNode)
				continue;
			CString chainXML=(TCHAR*)chainNode->Getxml();
			res[bindingID]=chainXML;
		}
		return res;
	}
	catch(...)
	{
		return res;
	}
}
void cmpDocument::Reset()
{
	try
	{
		//honestly, I should reload Document completely and reselt all according to preferences
		//temporarity I just remove the components
		IXMLDOMDocument2Ptr pDom = pXmlDOM;

		//Get CXCanvas node
		MSXML2::IXMLDOMNodePtr canvasNode;
		canvasNode = pDom->selectSingleNode("CXCanvas");
		MSXML2::IXMLDOMNodeListPtr nodelist = canvasNode->selectNodes("component");
		int n = nodelist->Getlength();
		for(int i=0; i<n; i++ )
		{
			MSXML2::IXMLDOMNodePtr compNode = nodelist->Getitem(i);
			canvasNode->removeChild(compNode);
		}
		SetNote(_T(""));
	}
	catch(...)
	{
		assert(false);
		return;
	}
	DeleteAllConnections();
}
void cmpDocument::AddGroupMembersToList(std::vector<_bstr_t> list,std::vector<_bstr_t>& result)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	result.clear();
	for(UINT i=0;i<list.size();i++)
	{
		if(IsGroup(list[i]))
		{
			MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + list[i] + "']");
			MSXML2::IXMLDOMNodeListPtr nodelist=compNode->selectNodes("component");
			for(int j=0;j<nodelist->Getlength();j++)
			{
				MSXML2::IXMLDOMNodePtr childNode=nodelist->Getitem(j);
				MSXML2::IXMLDOMNamedNodeMapPtr childmap = childNode->Getattributes();
				MSXML2::IXMLDOMAttributePtr atrbID = childmap->getNamedItem("id");
				_bstr_t childID=atrbID->Getvalue();
				if(IsGroup(childID))
				{
					list.push_back(childID);
				}
				else
				{
					result.push_back(childID);
				}
			}
		}
		else
		{
			result.push_back(list[i]);
		}

	}

}
bool cmpDocument::IsGroup(_bstr_t compID)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	try
	{
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + compID + "']");
		if(!compNode)
			return false;
		MSXML2::IXMLDOMNamedNodeMapPtr map = compNode->Getattributes();
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("className");
		if(!atrb)
			return false;
		_bstr_t classNameString=atrb->Getvalue();
		CString csClassName=(TCHAR*)classNameString;
		if(csClassName==_T("xcelsius.canvas.Group"))
		{
			return true;
		}
		return false;
	}
	catch(...)
	{
		return false;
	}
}
CString cmpDocument::GetAssets(CString compID,CString propName)
{
	CString res=_T("");
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	try
	{
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
			return res;
		MSXML2::IXMLDOMNodePtr assetsNode=compNode->selectSingleNode("assets");
		if(!assetsNode)
			return res;
		CString request=_T("asset/name[./string = '")+propName+_T("']");
		MSXML2::IXMLDOMNodePtr nameNode=assetsNode->selectSingleNode(_bstr_t(request));
		if(!nameNode)
			return res;
		MSXML2::IXMLDOMNodePtr valueNode=nameNode->GetparentNode()->selectSingleNode("value");
		if(!valueNode)
			return res;
		MSXML2::IXMLDOMNodeListPtr list=valueNode->selectNodes("array/asset");
		CArrayArgument* arrArg=new CArrayArgument();
		int n=list->Getlength();
		for(int i=0;i<n;i++)
		{
			MSXML2::IXMLDOMNodePtr aNode = list->Getitem(i);
			//assets can be an 1D array or 2D array
			MSXML2::IXMLDOMNodePtr subArrNode=aNode->selectSingleNode("array");
			if(subArrNode)
			{
				//2D array
				CArrayArgument* arrSubArg=new CArrayArgument();
				MSXML2::IXMLDOMNodeListPtr subList = subArrNode->selectNodes("asset");
				int m=subList->Getlength();
				for(int j=0; j<m; j++)
				{
					MSXML2::IXMLDOMNodePtr aSubNode = subList->Getitem(j);
					arrSubArg->add(buildAssetsItem(aSubNode));
				}
				arrArg->add(arrSubArg);
			}
			//1D array
			else
			{
				arrArg->add(buildAssetsItem(aNode));
			}
		}
		arrArg->saveString(&res);
		delete arrArg;
		return res;

	}
	catch(...)
	{
		return res;
	}
}

CObjectArgument* cmpDocument::buildAssetsItem(MSXML2::IXMLDOMNodePtr item)
{
	CObjectArgument* objArg=new CObjectArgument();
	MSXML2::IXMLDOMNodePtr tempFileNode=item->selectSingleNode("temporaryFile");
	CString csTempFile=(TCHAR*)tempFileNode->Gettext();
	objArg->set("temporaryFile",csTempFile);
	MSXML2::IXMLDOMNodePtr origFileNode=item->selectSingleNode("originalFile");
	CString csOrigFile=(TCHAR*)origFileNode->Gettext();
	objArg->set("originalFile",csOrigFile);
	MSXML2::IXMLDOMNodePtr embedNode=item->selectSingleNode("embed");
	CString csEmbed=(TCHAR*)embedNode->Gettext();
	bool bEmbedded=false;
	if(csEmbed==_T("embed"))
		bEmbedded=true;
	objArg->set("embed",bEmbedded);
	return objArg;
}
int cmpDocument::RemoveConnectionsOfClass(CString className)
{
	MSXML2::IXMLDOMNodeListPtr nodelist;
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	nodelist = pDom->selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
	int n = nodelist->Getlength();
	for(int i=0;i<n;i++)
	{
		MSXML2::IXMLDOMNodePtr connNode=nodelist->Getitem(i);
		MSXML2::IXMLDOMNodePtr parent=connNode->GetparentNode();
		parent->removeChild(connNode);
	}
	return n;
}
bool cmpDocument::HasConnectionsOfClass(CString className)
{
	MSXML2::IXMLDOMNodeListPtr nodelist;
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	nodelist = pDom->selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
	int n = nodelist->Getlength();
	return (n!=0);
}
CString cmpDocument::GetNote()
{
	CString res;
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMElementPtr canvasNode = pDom->selectSingleNode("/CXCanvas");
	MSXML2::IXMLDOMElementPtr noteNode = canvasNode->selectSingleNode("Notes");
	if(noteNode)
	{
		MSXML2::IXMLDOMCDATASectionPtr cData=noteNode->GetfirstChild();
		res=(TCHAR*)cData->data;
	}
	return res;

}
void cmpDocument::SetNote(CString csNote)
{
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMElementPtr canvasNode = pDom->selectSingleNode("/CXCanvas");
	MSXML2::IXMLDOMElementPtr noteNode = canvasNode->selectSingleNode("Notes");
	if(!noteNode)
	{
		noteNode=pDom->createElement("Notes");
		canvasNode->appendChild(noteNode);
		MSXML2::IXMLDOMCDATASectionPtr cData=pDom->createCDATASection(_bstr_t(csNote));
		noteNode->appendChild(cData);
		return;
	}
	else
	{
		MSXML2::IXMLDOMCDATASectionPtr cData=noteNode->GetfirstChild();
		cData->data=_bstr_t(csNote);
	}
}

std::vector<std::wstring> cmpDocument::GetLiveOfficeCuids()
{
	std::vector<std::wstring> cuids;

	//get crystalPersistXML
	//get the value
	//parse the persistxml
	//grab the cuids
	//put it into the array and return it
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMElementPtr canvasNode = pDom->selectSingleNode("/CXCanvas");

	MSXML2::IXMLDOMNodePtr persistXMLNode = canvasNode->selectSingleNode("//properties/property/name[./string='crystalPersistXML']/following-sibling::*");
	if(persistXMLNode)
	{
		MSXML2::IXMLDOMNodePtr oldValue = persistXMLNode->GetfirstChild();
		_bstr_t persistXML = oldValue->Gettext();

		//we need to parse "cuids" out of b.
		IXMLDOMDocument2Ptr cuidDOM;
		cuidDOM.CreateInstance(__uuidof(DOMDocument40));
		
		VARIANT_BOOL loadSuccess = cuidDOM->loadXML(persistXML);

		if(loadSuccess)
		{
			MSXML2::IXMLDOMNodeListPtr reportList = cuidDOM->selectNodes("//Report");
			if(reportList)
			{
				int size = reportList->Getlength();
				for(int i=0; i<size; i++)
				{
					MSXML2::IXMLDOMElementPtr report = reportList->Getitem(i);
					_variant_t v = report->getAttribute("cuid");

					//we could check for duplicates here
					cuids.push_back(v.bstrVal);
				}
			}
		}
	}
	return cuids;
}
void cmpDocument::ChangeComponentLevel(CString compID, int newLevel)
{
try
	{
		IXMLDOMDocument2Ptr pDom = pXmlDOM;
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return;
		}
		MSXML2::IXMLDOMNodePtr parent=compNode->GetparentNode();
		if(!parent)
			return;
		MSXML2::IXMLDOMNodePtr clone=compNode->cloneNode(VARIANT_TRUE);
		parent->removeChild(compNode);

		MSXML2::IXMLDOMNodePtr nextSibling=parent->GetfirstChild();
		if(newLevel==0)
		{
			if(nextSibling)
			{
				parent->insertBefore(clone,nextSibling.GetInterfacePtr());
			}
			else
			{
				parent->appendChild(clone);

			}
			return;
		}
		while(nextSibling && (nextSibling->GetnodeName())!=_bstr_t("component"))
		{
			nextSibling=nextSibling->GetnextSibling();
		}
		for(int i=0;(i<newLevel) && nextSibling;i++)
		{
			nextSibling=nextSibling->GetnextSibling();
			while(nextSibling && (nextSibling->GetnodeName())!=_bstr_t("component"))
			{
				nextSibling=nextSibling->GetnextSibling();
			}
		}
		if(nextSibling)
		{
			parent->insertBefore(clone,nextSibling.GetInterfacePtr());
		}
		else
		{
			parent->appendChild(clone);

		}
	
		return;
	}
	catch(...)
	{
		assert(false);
		return ;
	}
}
void cmpDocument::SetWarningTitle(CString title)
{
	m_csWarningTitle=title;
}
std::vector<bstr_t> cmpDocument::UnbindProperty(std::vector<bstr_t> compIds,CString propName)
{
	//returns list of binding ids to unbind
	std::vector<bstr_t> bindingIDs;
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	int n=compIds.size();
	for(int i=1;i<n;i++)
	{
		MSXML2::IXMLDOMNodePtr compNode = pDom->selectSingleNode("//component[@id='" + _bstr_t(compIds[i]) + "']");		
		MSXML2::IXMLDOMNodeListPtr nameNodes=compNode->selectNodes("bindings/property/name[./string='"+_bstr_t(propName)+"']");
		for(int j=0;j<nameNodes->Getlength();j++)
		{
			MSXML2::IXMLDOMNodePtr nextNameNode=nameNodes->Getitem(j);
			MSXML2::IXMLDOMNodePtr endpointNode=nextNameNode->selectSingleNode("../value/binding/endpoint");
			if(endpointNode)
			{
				MSXML2::IXMLDOMNamedNodeMapPtr map=endpointNode->Getattributes();
				MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("id");
				if(atrb!=0)
				{
					_bstr_t ID=atrb->Getvalue();
					bindingIDs.push_back(ID);
					MSXML2::IXMLDOMNodePtr delNode=nextNameNode->GetparentNode();
					delNode->GetparentNode()->removeChild(delNode);
				}
			}

		}
	}
	return bindingIDs;
}
CString cmpDocument::GetConnectionsROL()
{
	CString res;
	CArrayArgument arrArg;
	CString valueXML;
	IXMLDOMDocument2Ptr pDom = pXmlDOM;
	MSXML2::IXMLDOMNodeListPtr nodelist = pDom->selectNodes("//connection");
	int n=nodelist->Getlength();
	for(int i=0;i<n;i++)
	{
		CObjectArgument* pObj=new CObjectArgument();
		MSXML2::IXMLDOMNodePtr connNode=nodelist->Getitem(i);
		MSXML2::IXMLDOMNamedNodeMapPtr map = connNode->Getattributes();
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("id");
		if(!atrb)
			continue;
		CString id=(TCHAR*)(_bstr_t)atrb->Getvalue();
		MSXML2::IXMLDOMNodePtr propertiesNode = connNode->selectSingleNode("properties");
		if(propertiesNode == NULL)
		{
			valueXML=_T("<null/>");
		}
		else
		{
			MSXML2::IXMLDOMNodePtr propNode=NULL;
			propNode = propertiesNode->selectSingleNode("./property//name[./string = 'refreshOnLoad']/following-sibling::*");
			if(propNode == NULL)
			{
				valueXML=_T("<null/>");
			}		
			if(propNode != NULL)
			{
				MSXML2::IXMLDOMNodePtr oldValue = propNode->GetfirstChild();
				if ( oldValue != NULL )
				{
					valueXML=(LPCTSTR)(oldValue->Getxml());
				}
			}
		}
		_bstr_t bs=valueXML;
		CArgument *pArgValue = CArgument::Load(bs);
		pObj->set(_T("ID"),id);
		pObj->set(_T("ROL"),pArgValue);
		arrArg.add(pObj);
	}
	arrArg.saveString(&res);
	return res;
}

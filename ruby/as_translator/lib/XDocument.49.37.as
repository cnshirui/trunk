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

#pragma warning (disable:4800)	// forcing a var to:String bool (performance warning)

cmpDocument::cmpDocument()
{
	//Create a xml document
	var pDOM:XML;
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

	var pDom:XML = pXmlDOM;

	uget uid;
	DocGUID = uid.getUUID(); 

	//"<?xml version="1.0" encoding="utf-8"?><CXCanvas></CXCanvas>
	//VARIANT_BOOL loadSuccess = pDom.loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas><HSlider id=\"growthRate\"><x>21</x><y>207</y><minimum>0</minimum><maximum>1</maximum><liveDragging>true</liveDragging><themeColor>haloOrange</themeColor><snapInterval>.01</snapInterval></HSlider></CXCanvas>");
	pDom.PutpreserveWhiteSpace(VARIANT_FALSE);

	//add docxml as attribute
	var docxml:String = _T("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas guid=\"");
	docxml += DocGUID.c_str();
	docxml += _T("\"></CXCanvas>");

	//Add default font

//	/ VARIANT_BOOL loadSuccess = / pDom.loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas></CXCanvas>");
	/ VARIANT_BOOL loadSuccess = / pDom.loadXML(docxml.c_str());

	SetExportSettings(true, _T(""));

}


var cmpDocument:Boolean::RemoveFont()
{
	try
	{
		var pDom:XML = pXmlDOM;

		var fontNode:XML = pDom.selectSingleNode("/CXCanvas/font");

		if(fontNode!=NULL)
		{
			var compParent:XML = fontNode.GetparentNode();		
			compParent.removeChild(fontNode);
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


var cmpDocument:Boolean::AddFont(const var fontName:String,uvar charSet:int, uvar charSel:int,var embed:Boolean )
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get CXCanvas node
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");

		// Check and remove if font node already exists
		RemoveFont();
		
		//Create font node
		var compNode:XML = pDom.createElement("font");

		compNode.setAttribute("fontName",fontName);

		canvasNode.appendChild(compNode);

		var setChild:XML;
		var selChild:XML;
		var emChild:XML;

		var b_embed:int=0;

		if(embed)
		{
			setChild = pDom.createElement("charSet");
			setChild.text = _bstr_t(charSet);
			compNode.appendChild(setChild);

			selChild = pDom.createElement("charSel");
			selChild.text = _bstr_t(charSel);
			compNode.appendChild(selChild);

			b_embed=1;
		}

		emChild = pDom.createElement("embed");
		emChild.text = _bstr_t(b_embed);
		compNode.appendChild(emChild);

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
	

	
}

var cmpDocument:Boolean::GetFont()
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		cmpFontInfo cmpFont=cmpFontInfo::GetInstance();

		/const var ff:String=pDom.Getxml();/

		//Get node list
		var nodelist:XMLList;

		var fontNode:XML = pDom.selectSingleNode("/CXCanvas/font");

		if(fontNode==NULL)
			return false;

		var map:Dictionary = fontNode.Getattributes();
		var b:String=_T("fontName");
		var atrb:String = map.getNamedItem(b);
		var c:String=atrb.Getvalue();
		cmpFontInfo::GetInstance().SetFontName((TCHAR)(LPCTSTR)c);//imutil::SetFont( (TCHAR)(LPCTSTR)c);

		//embed or not

		var selNode:XML = fontNode.selectSingleNode("./embed");
		var selText:String=_com_util::ConvertBSTRToString(selNode.text);
		
		var embed:int=(int)atoi(selText);
		delete [] selText;

		if(embed)
		{
			//Get font data
			
			var selNode:XML = fontNode.selectSingleNode("./charSel");
			var selText:String=_com_util::ConvertBSTRToString(selNode.text);
			
			cmpFont.SetCharSel((uint)atoi(selText));

			delete [] selText;

			var setNode:XML = fontNode.selectSingleNode("./charSet");	
			var setText:String=_com_util::ConvertBSTRToString(setNode.text);
			cmpFont.SetCharSet((uint)atoi(setText));

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

var cmpDocument:Boolean::AddConnection(const var className:String, const var instanceID:String, const var displayName:String)
{
	try
	{

		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get CXCanvas node
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");

		//Create component node
		var compNode:XML = pDom.createElement("connection");

		//Set id attribute
		compNode.setAttribute("id",instanceID);

		//set className attribute
		compNode.setAttribute("className",className);

		//Figure out display name count based on number of components
		var nodelist:XMLList;
		nodelist = pDom.selectNodes("//connection");
		var len:int = 1;
		if(nodelist != NULL){
			len = nodelist.length;
			len++;
		}
		var dispName:String = _bstr_t(GenerateName(displayName));

		//set displayName attribute
		compNode.setAttribute("displayName",dispName);

		//Append compnent node to cxcanvas
		canvasNode.appendChild(compNode);

		//Get default properties string from uiComp
		//Get the var pointer:uiLibrary
		var uiLib:uiLibrary=uiLibrary::GetInstance();
		var uicomp:uiLibraryComponent = uiLib.GetComponent(className);

		//Did we get a uiComp?
		if(uicomp != NULL){
			//Copy default xml from uiComponent to componentXML string
			var tmp:String = uicomp.GetDefault();

			//Load default props into a dom
			//Create a xml document
			var pDOM2:XML;
			pDOM2.CreateInstance(__uuidof(DOMDocument40));
			pDOM2.PutpreserveWhiteSpace(VARIANT_FALSE);
			/VARIANT_BOOL loadSuccess = / pDOM2.loadXML(tmp);

			//Get properties node
			var uiCompProps:XML;
			uiCompProps = pDOM2.selectSingleNode("//component[@className='" + _bstr_t(className) + "']/properties");

			//Are there properties to clone?
			if(uiCompProps != NULL){
				//clone the uiComp properties node
				var clone:XML = uiCompProps.cloneNode(VARIANT_TRUE);

				//Add properties node to new comp
				compNode.appendChild(clone);
			}
		}
		//Append properties node to component node?
		var propertiesNode:XML;
		propertiesNode = compNode.selectSingleNode("properties");
		if (propertiesNode == NULL)
		{
			propertiesNode = pDom.createElement("properties");
			compNode.appendChild(propertiesNode);
		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean::AddComponent(const var className:String, const var instanceID:String, const var displayName:String,const var styleName:String,const var colorSchemeName:String, var rename:Boolean)
{
	try
	{

		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get CXCanvas node
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		if(!m_undoMap[instanceID].IsEmpty())
		{
			var xmlStr:String=m_undoMap[instanceID];
			var pDom:XML;
			pDom.CreateInstance(__uuidof(DOMDocument40));
			VARIANT_BOOL loadSuccess = pDom.loadXML(_bstr_t(xmlStr));
			if(loadSuccess)
			{
				var pCompNode:XML=pDom.GetdocumentElement();
				canvasNode.appendChild(pCompNode.cloneNode(VARIANT_TRUE));
				return true;
			}

		}

		//Create component node
		var compNode:XML = pDom.createElement("component");

		//Set id attribute
		compNode.setAttribute("id",instanceID);

		//set className attribute
		compNode.setAttribute("className",className);

		//set className attribute
		compNode.setAttribute("styleName",styleName);

		//Figure out display name count based on number of components
		var nodelist:XMLList;
		nodelist = pDom.selectNodes("//component[@className='" + _bstr_t(className) + "']");
		var len:int = 1;
		if(nodelist != NULL){
			len = nodelist.length;
			len++;
		}
		var dispName:String = rename?_bstr_t(GenerateName(displayName)):displayName;

		//set displayName attribute
		compNode.setAttribute("displayName",dispName);

		//Append compnent node to cxcanvas
		canvasNode.appendChild(compNode);
		var colorSchemeNode:XML = pDom.createElement("colorScheme");
		colorSchemeNode.text = colorSchemeName;
		compNode.appendChild(colorSchemeNode);

		//Get default properties string from uiComp
		//Get the var pointer:uiLibrary
		var uiLib:uiLibrary=uiLibrary::GetInstance();
		var uicomp:uiLibraryComponent = uiLib.GetComponent(className);

		//Did we get a uiComp?
		if(uicomp != NULL){
			//Copy default xml from uiComponent to componentXML string
			var tmp:String = uicomp.GetDefault();

			//Load default props into a dom
			//Create a xml document
			var pDOM2:XML;
			pDOM2.CreateInstance(__uuidof(DOMDocument40));
			pDOM2.PutpreserveWhiteSpace(VARIANT_FALSE);
			/VARIANT_BOOL loadSuccess = / pDOM2.loadXML(tmp);

			//Get properties node
			var uiCompProps:XML;
			uiCompProps = pDOM2.selectSingleNode("//component[@className='" + _bstr_t(className) + "']/properties");

			//Are there properties to clone?
			if(uiCompProps != NULL){
				//clone the uiComp properties node
				var clone:XML = uiCompProps.cloneNode(VARIANT_TRUE);

				//Add properties node to new comp
				compNode.appendChild(clone);
			}
		}

		//Append properties node to component node?
		var propertiesNode:XML;
		propertiesNode = compNode.selectSingleNode("properties");
		if (propertiesNode == NULL)
		{
			propertiesNode = pDom.createElement("properties");
			compNode.appendChild(propertiesNode);
		}

	/	//Add displayName node to properties
		var propertyNode:XML;
		var valNode:XML;
		var valChild:XML;
		var nameNode:XML;
		var nameChild:XML;
		//Create property node
		propertyNode = pDom.createElement("property");
		//Create property name nodes
		nameNode = pDom.createElement("name");
		nameChild = pDom.createElement("string");
		nameChild.text = "displayName";
		nameNode.appendChild(nameChild);
		propertyNode.appendChild(nameNode);
		//Create property value nodes
		valNode = pDom.createElement("value");
		valChild = pDom.createElement("string");
		valChild.text = displayName;
		valNode.appendChild(valChild);
		propertyNode.appendChild(valNode);
		//Append property node
		propertiesNode.appendChild(propertyNode);/

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
	
}


var cmpDocument:Boolean::GetComponent(const var instanceID:String, cmpComponent comp,var bCompIsConnection:Boolean)
{
	try
	{

		//Returns pointer to List object that has cmpComponents
		//return Comps[instanceID];
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get compnent node
		var compNode:XML;
		if(bCompIsConnection)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		else
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode == NULL){
			//Return
			return false;
		}else{

			//Set component xml string
			comp.SetCompXML(compNode.Getxml());

			//Set bindings xml?
			var bindingsNode:XML;
			bindingsNode = compNode.selectSingleNode("./bindings");
			if(bindingsNode != NULL){
				comp.SetBindingXML(bindingsNode.Getxml());
			}

			//Set asset xml
			var assetsNode:XML;
			assetsNode = compNode.selectSingleNode("./assets");
			if(assetsNode != NULL){
				comp.SetAssetXML(assetsNode.Getxml());
			}

			//Set metadata xml
			var metadataNode:XML;
			metadataNode = compNode.selectSingleNode("./metaData");
			if(metadataNode != NULL){
				comp.SetMetadataXML(metadataNode.Getxml());
			}

			//Set instanceID
			comp.SetInstanceID(instanceID);

			//Get className attribute and set property
			var map:Dictionary = compNode.Getattributes();
			var b:String=_T("className");
			var atrb:String = map.getNamedItem(b);
			var classNameString:String=atrb.Getvalue();
			comp.SetClassName(classNameString);

			//Get styleName attribute and set property
			var b2:String=_T("styleName");
			atrb = map.getNamedItem(b2);
			if(atrb != NULL){
				var styleNameString:String=atrb.Getvalue();
				comp.SetStyleName(styleNameString);
			}

			//Get displayName attribute and set property
			var c:String=_T("displayName");
			atrb = map.getNamedItem(c);
			if(atrb != NULL){
				var displayNameString:String=atrb.Getvalue();
				comp.DisplayName = (displayNameString);
			}

			//Get the var pointer:uiLibrary
			var uiLib:uiLibrary=uiLibrary::GetInstance();
			var uicomp:uiLibraryComponent = uiLib.GetComponent(classNameString);
			//Set the uiComponent pointer
			comp.UIComp = uicomp;

			//Set component properties
			var propNode:XML;
			var valNode:XML;
			//x
			propNode = compNode.selectSingleNode("./properties/property//name[./string = 'x']/following-sibling::");
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				comp.x = atoi(valNode.text);
			}
			//y
			propNode = compNode.selectSingleNode("./properties/property//name[./string = 'y']/following-sibling::");
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				comp.y = atoi(valNode.text);
			}
			//width
			propNode = compNode.selectSingleNode("./properties/property//name[./string = 'width']/following-sibling::");
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				comp.width = atoi(valNode.text);
			}
			//height
			propNode = compNode.selectSingleNode("./properties/property//name[./string = 'height']/following-sibling::");
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				comp.height = atoi(valNode.text);
			}
	/		//displayName
			propNode = compNode.selectSingleNode("./properties/property//name[./string = 'displayName']/following-sibling::");
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./string");
				comp.DisplayName = valNode.text;
			}/

			//properties array
			std::map<std::wstring, std::wstring> props = comp.GetProperties();
			props.clear();
			var nodelist:XMLList;
			nodelist = compNode.selectNodes("./properties/property");

			if(nodelist!=NULL)
			{
				var propCount:int = nodelist.Getlength();
				for( var i:int=0; i<propCount; i++ )
				{
					propNode = nodelist.Getitem(i);

					var nameXML:String;
					var valueXML:String;

					var nameNode:XML = propNode.selectSingleNode( _T("./name/string") );
					var valueNode:XML = propNode.selectSingleNode( _T("./value/") );

					nameNode.get_text( nameXML.GetAddress() );
					valueNode.get_xml( valueXML.GetAddress() );

					(props)[ nameXML.GetBSTR() ] = valueXML.GetBSTR();
				}
			}

			//bindings array
			var bindings:Array = comp.GetBindings();
			bindings.clear();
			nodelist = compNode.selectNodes("./bindings/property");

			if(nodelist!=NULL)
			{
				var propCount:int = nodelist.Getlength();
				for( var i:int=0; i<propCount; i++ )
				{
					propNode = nodelist.Getitem(i);

					BSTR nodeXML;
					propNode.get_xml(&nodeXML);
					bindings.push_back( nodeXML );
				}
			}

			// style map
			std::map<std::wstring, std::wstring> styles = comp.GetStyles();
			styles.clear();
			
			nodelist = compNode.selectNodes("./styles/property");

			if(nodelist!=NULL)
			{
				var propCount:int = nodelist.Getlength();
				for( var i:int=0; i<propCount; i++ )
				{
					propNode = nodelist.Getitem(i);

					var nameNode:XML = propNode.selectSingleNode( _T("./name/string") );
					var valueNode:XML = propNode.selectSingleNode( _T("./value/") );

					var nameXML:String = nameNode.Gettext();
					var valueXML:String = valueNode.Getxml();

					(styles)[ nameXML.c_str() ] = valueXML.c_str();
				}
			}

			// sub components
			var comps:Array = comp.GetSubComponents();
			comps.clear();
			nodelist = compNode.selectNodes("./component");
			if(nodelist!=NULL)
			{
				var compCount:int = nodelist.Getlength();
				for( var i:int=0; i<compCount; i++ )
				{
					compNode = nodelist.Getitem(i);

					//Get instanceID attribute
					var map:Dictionary = compNode.Getattributes();
					var b:String=_T("id");
					var atrb:String = map.getNamedItem(b);
					var c:String=atrb.Getvalue();
					var instanceID:String=c;

					comps.push_back( instanceID );
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

var cmpDocument:int::GetComponentCount()
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		/const var compXML:String= / pDom.Getxml();
		//Get node list
		var nodelist:XMLList;
		nodelist = pDom.selectNodes("/CXCanvas/component");
		//Set return val
		var ret:int = nodelist.Getlength();
		//Return
		return ret;
	}
	catch(...)
	{
		assert(false);
		return 0;
	}
}
var cmpDocument:int::GetConnectionCount()
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;
		//Get node list
		var nodelist:XMLList;
		nodelist = pDom.selectNodes("/CXCanvas/connection");
		//Set return val
		var ret:int = nodelist.Getlength();
		//Return
		return ret;
	}
	catch(...)
	{
		assert(false);
		return 0;
	}
}
var cmpDocument:String::GetComponentID(var idx:int)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get node list
		var nodelist:XMLList;
		nodelist = pDom.selectNodes("/CXCanvas/component");

		//Get component node based on idx
		var compNode:XML = nodelist.Getitem(idx);

		//REMOVE
		var zzz:String=compNode.Getxml();

		//Get instanceID attribute
		var map:Dictionary = compNode.Getattributes();
		var b:String=_T("id");
		var atrb:String = map.getNamedItem(b);
		var c:String=atrb.Getvalue();
		var instanceID:String=c;

		//Return
		return instanceID;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}
var cmpDocument:String::GetConnectionID(var idx:int)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get node list
		var nodelist:XMLList;
		nodelist = pDom.selectNodes("/CXCanvas/connection");

		//Get component node based on idx
		var compNode:XML = nodelist.Getitem(idx);

		//Get instanceID attribute
		var map:Dictionary = compNode.Getattributes();
		var b:String=_T("id");
		var atrb:String = map.getNamedItem(b);
		var c:String=atrb.Getvalue();
		var instanceID:String=c;

		//Return
		return instanceID;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}

var cmpDocument:Boolean::SaveComponent(cmpComponent comp, var pChanged:int)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get properties node
		var propertiesNode:XML;
		if(pChanged)
		{
			pChanged=0;
		}

		propertiesNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(comp.GetInstanceID()) + "']/properties");
		if(!propertiesNode)
		{
			propertiesNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(comp.GetInstanceID()) + "']/properties");
		}
		if(propertiesNode == NULL){
			//Return
			return false;
		}else{
			//Get component node
			var compNode:XML;
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(comp.GetInstanceID()) + "']");
			if(!compNode)
			{
				compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(comp.GetInstanceID()) + "']");
			}
			//Set displayName attribute
			var map:Dictionary = compNode.Getattributes();
			var b:String=_T("displayName");
			var atrb:String = map.getNamedItem(b);
			atrb.text = comp.DisplayName.c_str();

			//Set component properties
			var propNode:XML;
			//New nodes for new xpath
			var propertyNode:XML;
			var valNode:XML;
			var valChild:XML;
			var nameNode:XML;
			var nameChild:XML;
			//x
			propNode = propertiesNode.selectSingleNode("./property//name[./string = 'x']/following-sibling::");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom.createElement("property");
				//Create property name nodes
				nameNode = pDom.createElement("name");
				nameChild = pDom.createElement("string");
				nameChild.text = "x";
				nameNode.appendChild(nameChild);
				propertyNode.appendChild(nameNode);
				//Create property value nodes
				valNode = pDom.createElement("value");
				valChild = pDom.createElement("number");
				valChild.text = "";
				valNode.appendChild(valChild);
				propertyNode.appendChild(valNode);
				//Append property node
				propertiesNode.appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				if(pChanged && valNode.text != (_bstr_t)comp.x)
				{
					pChanged=1;
				}

				valNode.text = (_bstr_t)comp.x;
			}
			//y
			propNode = propertiesNode.selectSingleNode("./property//name[./string = 'y']/following-sibling::");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom.createElement("property");
				//Create property name nodes
				nameNode = pDom.createElement("name");
				nameChild = pDom.createElement("string");
				nameChild.text = "y";
				nameNode.appendChild(nameChild);
				propertyNode.appendChild(nameNode);
				//Create property value nodes
				valNode = pDom.createElement("value");
				valChild = pDom.createElement("number");
				valChild.text = "";
				valNode.appendChild(valChild);
				propertyNode.appendChild(valNode);
				//Append property node
				propertiesNode.appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				if(pChanged && valNode.text != (_bstr_t)comp.y)
				{
					pChanged=1;
				}
				valNode.text = (_bstr_t)comp.y;
			}

			//width
			propNode = propertiesNode.selectSingleNode("./property//name[./string = 'width']/following-sibling::");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom.createElement("property");
				//Create property name nodes
				nameNode = pDom.createElement("name");
				nameChild = pDom.createElement("string");
				nameChild.text = "width";
				nameNode.appendChild(nameChild);
				propertyNode.appendChild(nameNode);
				//Create property value nodes
				valNode = pDom.createElement("value");
				valChild = pDom.createElement("number");
				valChild.text = "";
				valNode.appendChild(valChild);
				propertyNode.appendChild(valNode);
				//Append property node
				propertiesNode.appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				if(pChanged && valNode.text != (_bstr_t)comp.width)
				{
					pChanged=1;
				}
				valNode.text = (_bstr_t)comp.width;
			}
			//height
			propNode = propertiesNode.selectSingleNode("./property//name[./string = 'height']/following-sibling::");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom.createElement("property");
				//Create property name nodes
				nameNode = pDom.createElement("name");
				nameChild = pDom.createElement("string");
				nameChild.text = "height";
				nameNode.appendChild(nameChild);
				propertyNode.appendChild(nameNode);
				//Create property value nodes
				valNode = pDom.createElement("value");
				valChild = pDom.createElement("number");
				valChild.text = "";
				valNode.appendChild(valChild);
				propertyNode.appendChild(valNode);
				//Append property node
				propertiesNode.appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				valNode = propNode.selectSingleNode("./number");
				if(pChanged && valNode.text != (_bstr_t)comp.height)
				{
					pChanged=1;
				}
				valNode.text = (_bstr_t)comp.height;
			}
			//Display Name - we don't set it as a property any more
		
			//Update the component xml string
			comp.SetCompXML(propertiesNode.Getxml());



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

var cmpDocument:Boolean::UpdateComponentXML(const var instanceID:String,const var compXML:String)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get properties node
		var compNode:XML;
		var oldPropertiesNode:XML, newPropertiesNode;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("properties");

			var pValueDom:XML;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(compXML);

			var newValueOrigNode:XML = pValueDom.GetfirstChild();
			newPropertiesNode = newValueOrigNode.cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode.replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode.appendChild(newPropertiesNode);

		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


var cmpDocument:Boolean::UpdateBindingXML(const var instanceID:String,const var bindXML:String)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get properties node
		var compNode:XML;
		var oldPropertiesNode:XML, newPropertiesNode;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("bindings");

			var pValueDom:XML;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(bindXML);

			var newValueOrigNode:XML = pValueDom.GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode.cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode.replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode.appendChild(newPropertiesNode);
			

		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean::UpdateStyleXML(const var instanceID:String,const var styleXML:String)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get properties node
		var compNode:XML;
		var oldPropertiesNode:XML, newPropertiesNode;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("styles");

			var pValueDom:XML;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(styleXML);

			var newValueOrigNode:XML = pValueDom.GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode.cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode.replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode.appendChild(newPropertiesNode);
		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean::UpdateAssetXML(const var instanceID:String,const var assetXML:String)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get properties node
		var compNode:XML;
		var oldPropertiesNode:XML, newPropertiesNode;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("assets");

			var pValueDom:XML;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(assetXML);

			var newValueOrigNode:XML = pValueDom.GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode.cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode.replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode.appendChild(newPropertiesNode);
		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean::UpdatePersistXML(const var instanceID:String,const var persistXML:String)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get properties node
		var compNode:XML;
		var oldPropertiesNode:XML, newPropertiesNode;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("Persist");

			var pValueDom:XML;
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(persistXML);

			var newValueOrigNode:XML = pValueDom.GetfirstChild();

			if(!newValueOrigNode) return false;

			newPropertiesNode = newValueOrigNode.cloneNode(VARIANT_TRUE);

			if( oldPropertiesNode )
				compNode.replaceChild(newPropertiesNode, oldPropertiesNode);
			else
				compNode.appendChild(newPropertiesNode);
		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


var cmpDocument:Boolean::Save(const var fname:String) 
{
	try
	{
		var pDom:XML = pXmlDOM;
		HRESULT hr = pDom.save( fname );
		return (hr==S_OK)? true: false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean::Load(const var fname:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		VARIANT_BOOL varBool;
		varBool = pDom.load( fname);

		if (varBool) 
		{
			pXmlDOM = pDom;

			var nptr:XML = pDom.selectSingleNode("CXCanvas/@guid");
			if(nptr)
			{
				BSTR b;	nptr.get_text(&b);
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
var cmpDocument:Boolean::DeleteComponent(const var instanceID:String, var bCompIsConnection:Boolean)
{ 
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get compnent node
		var compNode:XML;
		if(bCompIsConnection)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		else
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode == NULL){
			//Return
			return false;
		}else
		{
			var csXML:String=(TCHAR)compNode.Getxml();
			m_undoMap[instanceID]=csXML;
			//Set pointer to parent of comp node
			var compParent:XML = compNode.GetparentNode();

			//Remove the component node
			compParent.removeChild(compNode);

		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:Boolean::ShowComponent(const var instanceID:String, var bShow:Boolean)
{
	var pDom:XML = pXmlDOM;
	var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
	if(!compNode)
		return false;
	compNode.setAttribute("visible",(BOOL)bShow);
	return true;
}
var cmpDocument:Boolean::LockComponent(const var instanceID:String, var bLock:Boolean)
{
	var pDom:XML = pXmlDOM;
	var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
	if(!compNode)
		return false;
	compNode.setAttribute("locked",(BOOL)bLock);
	return true;
}
var cmpDocument:Boolean::ShowAllComponents(var bShow:Boolean)
{
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
	var nodelist:XMLList = canvasNode.selectNodes("component");
	var n:int = nodelist.Getlength();
	for(var i:int=0; i<n; i++ )
	{
		var compNode:XML = nodelist.Getitem(i);
		compNode.setAttribute("visible",(BOOL)bShow);
	}
	return true;
}
var cmpDocument:Boolean::LockAllComponents(var bLock:Boolean)
{
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
	var nodelist:XMLList = canvasNode.selectNodes("component");
	var n:int = nodelist.Getlength();
	for(var i:int=0; i<n; i++ )
	{
		var compNode:XML = nodelist.Getitem(i);
		compNode.setAttribute("locked",(BOOL)bLock);
	}
	return true;
}
var cmpDocument:Boolean::IsComponentLocked(var compID:String)
{
	var pDom:XML = pXmlDOM;
	var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
		return false;
	var map:Dictionary=compNode.Getattributes();
	var atrb:String = map.getNamedItem("locked");
	if(!atrb)
		return false;
	var bs:String=atrb.Getvalue();
	var str:String=(TCHAR)bs;
	var val:int=_ttoi(str);
	return (val!=0);

}
var cmpDocument:Boolean::IsComponentVisible(var compID:String)
{
	var pDom:XML = pXmlDOM;
	var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
		return true;
	var map:Dictionary=compNode.Getattributes();
	var atrb:String = map.getNamedItem("visible");
	if(!atrb)
		return true;
	var bs:String=atrb.Getvalue();
	var str:String=(TCHAR)bs;
	var val:int=_ttoi(str);
	return (val!=0);
}
var cmpDocument:Boolean::ModifyComponentProperty(const var compID:String, const var propName:String, const var valueXML:String,CString& oldValXML,var bCompIsConn:Boolean)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get component node
		var compNode:XML;
		var propertiesNode:XML;
		if(bCompIsConn)
		{
			compNode=pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		else
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode ==NULL)
		{
			// component not found
			return false;
		}
		propertiesNode = compNode.selectSingleNode("properties");
		if ( propertiesNode == NULL )
		{
			propertiesNode = pDom.createElement("properties");
			compNode.appendChild( propertiesNode );
		}
		assert( propertiesNode != NULL );
		if(propertiesNode == NULL){
			//Return
			return false;
		}else{
			//Set component properties
			var propNode:XML;
			//New nodes for new xpath
			var propertyNode:XML;
			var valNode:XML;
			var valChild:XML;
			var nameNode:XML;
			var nameChild:XML;

			propNode = propertiesNode.selectSingleNode("./property//name[./string = '" + _bstr_t(propName) + "']/following-sibling::");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom.createElement("property");
				//Create property name nodes
				nameNode = pDom.createElement("name");
				nameChild = pDom.createElement("string");
				nameChild.text = propName;
				nameNode.appendChild(nameChild);
				propertyNode.appendChild(nameNode);
				//Create property value nodes
				valNode = pDom.createElement("value");
				propertyNode.appendChild(valNode);
				//Append property node
				propertiesNode.appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				var pValueDom:XML;
				pValueDom.CreateInstance(__uuidof(DOMDocument40));
				/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(valueXML);

				var newValueOrigNode:XML = pValueDom.GetfirstChild();
				var newValue:XML = newValueOrigNode.cloneNode(VARIANT_TRUE);
			
				var oldValue:XML = propNode.GetfirstChild();
				if ( oldValue != NULL )
				{
					propNode.replaceChild( newValue, oldValue );
					oldValXML=(LPCTSTR)(oldValue.Getxml());
				}
				else
				{
					propNode.appendChild( newValue );
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
var cmpDocument:Boolean::GetComponentProperty(const var compID:String, const var propName:String,CString& valueXML)
{
	try
	{
		var pDom:XML = pXmlDOM;

		//Get component node
		var compNode:XML;
		var propertiesNode:XML;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		
		if(!compNode)
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");

		if(compNode ==NULL)
		{
			// component not found
			return false;
		}
		propertiesNode = compNode.selectSingleNode("properties");
		if(propertiesNode == NULL)
		{
			return false;//properties node not found
		}
		else
		{
			var propNode:XML=NULL;
			propNode = propertiesNode.selectSingleNode("./property//name[./string = '" + _bstr_t(propName) + "']/following-sibling::");
			if(propNode == NULL)
			{
				return false;
			}		
			if(propNode != NULL)
			{
				var oldValue:XML = propNode.GetfirstChild();
				if ( oldValue != NULL )
				{
					valueXML=(LPCTSTR)(oldValue.Getxml());
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

var cmpDocument:Boolean::ModifyCanvasStyle(const var styleName:String, const var valueXML:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var root:XML = pDom.selectSingleNode("CXCanvas");
		var canvasNode:XML = root.selectSingleNode("canvas");
		if(canvasNode==NULL)
		{
			canvasNode = pDom.createElement("canvas");
			root.appendChild(canvasNode);
		}
		var stylesNode:XML = canvasNode.selectSingleNode("styles");
		if(stylesNode == NULL)
		{
			stylesNode = pDom.createElement("styles");
			canvasNode.appendChild(stylesNode);
		}
		
		return modifyStyle(stylesNode, styleName, valueXML);
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean::ModifyComponentStyle(const var compID:String, const var styleName:String, const var valueXML:String, var bGlobal:Boolean)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get component node
		var compNode:XML;
		var stylesNode:XML;
		if(!bGlobal)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
			if(!compNode)
			{
				compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
			}

			if ( compNode == NULL )
			{
				// component not found
				return false;
			}

			stylesNode = compNode.selectSingleNode("./styles");
			if ( stylesNode == NULL )
			{
				// create the node
				stylesNode = pDom.createElement( "styles" );
				compNode.appendChild( stylesNode );
			}
		}
		else
		{
			var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
			var globalStylesNode:XML= canvasNode.selectSingleNode("Styles");
			if(globalStylesNode==NULL)
			{
				CreateCSSStyleDeclaration(compID);
				globalStylesNode= canvasNode.selectSingleNode("Styles");
			}
			stylesNode = globalStylesNode.selectSingleNode("//style[@id='" + _bstr_t(compID) + "']");
			if(!stylesNode)
			{
				stylesNode = pDom.createElement("style");
				var bs:String=compID;
				stylesNode.setAttribute("id",bs);
				globalStylesNode.appendChild(stylesNode);
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
			var propNode:XML;
			//New nodes for new xpath
			var propertyNode:XML;
			var valNode:XML;
			var valChild:XML;
			var nameNode:XML;
			var nameChild:XML;

			propNode = stylesNode.selectSingleNode("./property//name[./string = '" + _bstr_t(styleName) + "']/following-sibling::");
			if(propNode == NULL){
				//Create property node
				propertyNode = pDom.createElement("property");
				//Create property name nodes
				nameNode = pDom.createElement("name");
				nameChild = pDom.createElement("string");
				nameChild.text = styleName;
				nameNode.appendChild(nameChild);
				propertyNode.appendChild(nameNode);
				//Create property value nodes
				valNode = pDom.createElement("value");
				propertyNode.appendChild(valNode);
				//Append property node
				stylesNode.appendChild(propertyNode);
				//Set propNode pointer
				propNode = valNode;
			}		
			if(propNode != NULL){
				var pValueDom:XML;
				pValueDom.CreateInstance(__uuidof(DOMDocument40));
				/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(valueXML);

				var newValueOrigNode:XML = pValueDom.GetfirstChild();
				var newValue:XML = newValueOrigNode.cloneNode(VARIANT_TRUE);
			
				var oldValue:XML = propNode.GetfirstChild();
				if ( oldValue != NULL )
				{
					propNode.replaceChild( newValue, oldValue );
				}
				else
				{
					propNode.appendChild( newValue );
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

var cmpDocument:Boolean::modifyStyle(const var stylesNode:XML, const var styleName:String, const var valueXML:String)
{
	assert(stylesNode != NULL);
	var pDom:XML = pXmlDOM;
	var valueNode:XML = stylesNode.selectSingleNode("//property//name[./string='" + _bstr_t(styleName) + "']/following-sibling::");
	
	try
	{
		if(valueNode == NULL)
		{
			var styleNode:XML = pDom.createElement("property");
			stylesNode.appendChild(styleNode);
			var nameNode:XML = pDom.createElement("name");
			var nameChild:XML = pDom.createElement("string");
			nameChild.text = styleName;
			nameNode.appendChild(nameChild);
			styleNode.appendChild(nameNode);
			valueNode = pDom.createElement("value");
			styleNode.appendChild(valueNode);
		}
		
		//create the 'value' node
		var pValueDom:XML;
		pValueDom.CreateInstance(__uuidof(DOMDocument40));
		pValueDom.loadXML(valueXML);

		var newValueOrigNode:XML = pValueDom.GetfirstChild();
		var newValue:XML = newValueOrigNode.cloneNode(VARIANT_TRUE);
		
		var oldValue:XML = valueNode.GetfirstChild();
		if(oldValue == NULL)
		{
			valueNode.appendChild(newValue);
		}
		else
		{
			valueNode.replaceChild(newValue, oldValue);
		}
	}
	catch(...)
	{
		assert(false);
		return false;
	}

	return true;
}
var cmpDocument:Boolean::ClearCanvasStyles()
{
	try
	{
		var pDom:XML = pXmlDOM;
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas/canvas");
		if(canvasNode)
		{
			canvasNode.GetparentNode().removeChild(canvasNode);
		}
		return true;
	}
	catch(...)
	{
		return false;
	}
}
var cmpDocument:Boolean::IsStyleProtected(var styleName:String)
{
	var names:String[10];
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
	for(var i:int=0;i<10;i++)
	{
		if(styleName.Find(names[i])!=-1)
		{
			return true;
		}
	}
	return false;
}
var cmpDocument:Boolean::IsColorStyle(var styleName:String)
{
	var n:int=styleName.ReverseFind('.');
	var str:String=styleName;
	if(n!=-1)
	{
		str=styleName.Right(styleName.GetLength()-n);
	}
	str.MakeLower();
	var nColor:int=str.Find(_T("color"));
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
		var pDom:XML = pXmlDOM;
		var compNodes:XMLList = pDom.selectNodes("//component");
		var n:int = compNodes.Getlength();
		for(var i:int=0;i<n;i++)
		{
			var compNode:XML= compNodes.Getitem(i);
			var map:Dictionary = compNode.Getattributes();
			var atrb:String = map.getNamedItem("id");
			var compID:String=atrb.Getvalue();
			var stylesNode:XML=compNode.selectSingleNode("styles");
			if(stylesNode)
			{
				var styleNames:XMLList=stylesNode.selectNodes("property/name");
				var nStyles:int=styleNames.Getlength();
				for(var j:int=0;j<nStyles;j++)
				{
					var styleNameNode:XML= styleNames.Getitem(j);
					var t:String=(TCHAR)styleNameNode.Getxml();
					var styleName:String=(TCHAR)styleNameNode.Gettext();
					var valueNode:XML=styleNameNode.selectSingleNode("../value");
					var valueXML:String=valueNode.GetfirstChild().Getxml();
					//Justin Cox confirmed that we clear only color styles
					if(IsColorStyle(styleName))
					{
						var styleNode:XML=styleNameNode.GetparentNode();
						t=(TCHAR)styleNode.Getxml();
						styleNode.GetparentNode().removeChild(styleNode);
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
var cmpDocument:Boolean::ClearComponentStyle(var compID:String, var style:String)
{
	try
	{
		var pDom:XML = pXmlDOM;

		//Get component node
		var compNode:XML;
		var stylesNode:XML;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if ( compNode == NULL )
		{
			// component not found
			return false;
		}
		stylesNode = compNode.selectSingleNode("./styles");
		if ( stylesNode == NULL )
		{
			return false;
		}
		MSXML2::IXMLDOMNodePtr	propNode = stylesNode.selectSingleNode("./property//name[./string = '" + _bstr_t(style) + "']/following-sibling::");
		if(propNode==NULL)
		{
			return false;
		}
		stylesNode.removeChild(propNode.GetparentNode());
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}


var cmpDocument:Boolean::MoveComponentsUp(std::vector <_bstr_t>& comps)
{
	try
	{
		var n:int=comps.size();
		for(var i:int=0;i<n;i++)
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
var cmpDocument:Boolean::MoveComponentsDown(std::vector <_bstr_t>& comps)
{
	try
	{
		var n:int=comps.size();
		for(var i:int=0;i<n;i++)
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
var cmpDocument:Boolean::ReparentComponent(const var compID:String, const var newParentID:String)
{
	try
	{
		//Need to move the component from the current location to be a child of the parent comp
		//for now we are going to tack it on to the end and make it the last child component

		//Set doc pointer;
		var pDom:XML = pXmlDOM;

		//Get compnent node
		var compNode:XML;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		//Get parent node
		var parentNode:XML;
		if ( _tcscmp( _T(""), newParentID ) == 0 ) {
			// the new parent is the canvas
			parentNode = pDom.selectSingleNode("/CXCanvas");
		}
		else {
			parentNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(newParentID) + "']");
		}

		if(compNode == NULL || parentNode == NULL){
			//Return
			return false;
		}else{

			//Clone the comp node
			var clone:XML = compNode.cloneNode(VARIANT_TRUE);

			//Add the cloned node to the parent
			parentNode.appendChild(clone);

			//Set pointer to parent of comp node
			var compParent:XML = compNode.GetparentNode();

			//Remove the component node
			compParent.removeChild(compNode);

		}

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:Boolean::MoveComponentUp(var compID:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		var parentNode:XML=compNode.GetparentNode();
		
		var clone:XML=compNode.cloneNode(VARIANT_TRUE);
		var prevNode:XML=compNode.GetpreviousSibling();
		var nodeType:String=(TCHAR)compNode.GetbaseName();
		var siblingNodeType:String=(TCHAR)prevNode.GetbaseName();
		//we need to move above node of the same type, 
		//i.e. component above previous component or connection above previous connection
		while(prevNode && nodeType!=siblingNodeType)
		{
			prevNode=prevNode.GetpreviousSibling();
			if(prevNode)
			siblingNodeType=(TCHAR)prevNode.GetbaseName();
		}
		if(!prevNode)
		{
			return true;
		}
		parentNode.insertBefore(clone,prevNode.GetInterfacePtr());
		parentNode.removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:Boolean::MoveComponentDown(var compID:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		var clone:XML=compNode.cloneNode(VARIANT_TRUE);

		var parentNode:XML=compNode.GetparentNode();
		var nextNode:XML=compNode.GetnextSibling();
		if(!nextNode)
			return true;
		var nodeType:String=(TCHAR)compNode.GetbaseName();
		var siblingNodeType:String=(TCHAR)nextNode.GetbaseName();
		//we need to move below the node of the same type, 
		//i.e. component below next component or connection below next connection
		while(nextNode && nodeType!=siblingNodeType)
		{
			nextNode=nextNode.GetnextSibling();
			if(nextNode)
			siblingNodeType=(TCHAR)nextNode.GetbaseName();
		}

		if(!nextNode)
		{
			return true;
		}
		var insertBeforeNode:XML=nextNode.GetnextSibling();
		parentNode.insertBefore(clone,insertBeforeNode.GetInterfacePtr());
		parentNode.removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:Boolean::ChangeComponentDepth(const var compID:String, var nLevelsUp:int)
{
	try
	{
		if(nLevelsUp==0)
			return true;
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return false;
		}
		var clone:XML=compNode.cloneNode(VARIANT_TRUE);
		var parent:XML=compNode.GetparentNode();
		if(!parent)
			return false;

		var beforeNode:XML=compNode;
		if(nLevelsUp>0)
		{
			for(var i:int=0;i<nLevelsUp && beforeNode!=0; i++)
			{
				beforeNode=beforeNode.GetpreviousSibling();
			}
			if(beforeNode==0)
			{
				beforeNode=parent.GetfirstChild();
			}
		}
		if(nLevelsUp<0)
		{
			var nLev:int=-nLevelsUp;
			for(var i:int=0;i<nLev+1 && beforeNode!=0; i++)
			{
				beforeNode=beforeNode.GetnextSibling();
			}
		}
		if(beforeNode)
		{
			parent.insertBefore(clone,beforeNode.GetInterfacePtr());
		}
		else
		{
			parent.appendChild(clone);
		}
		parent.removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:Boolean::BringComponentToFront(var compID:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return false;
		}

		var parentNode:XML=compNode.GetparentNode();
		var nextNode:XML=compNode.GetnextSibling();
		if(!nextNode)
		{
			return true;
		}
		var clone:XML=compNode.cloneNode(VARIANT_TRUE);
		parentNode.appendChild(clone);
		parentNode.removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:Boolean::SendComponentToBack(var compID:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return false;
		}
		var prevNode:XML=compNode.GetpreviousSibling();
		if(!prevNode)
		{
			return true;//it's already first
		}

		var parentNode:XML=compNode.GetparentNode();
		
		var clone:XML=compNode.cloneNode(VARIANT_TRUE);
		var firstSiblingNode:XML=parentNode.GetfirstChild();
		parentNode.insertBefore(clone,firstSiblingNode.GetInterfacePtr());
		parentNode.removeChild(compNode);
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

void cmpDocument::GroupComponents(std::vector <_bstr_t>& /comps/,_bstr_t /newGroupID/,_bstr_t /newGroupName/)
{
	
}
void cmpDocument::UngroupComponents(std::vector <_bstr_t>& /groups/)
{

}
var cmpDocument:String::ConvertTransXML(const var transXML:String)
{
	try
	{
		//Load transport xml into a dom
		//Create a xml document
		var pDom:XML;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		//Get instance id
		var nodelist:XMLList;
		nodelist = pDom.selectNodes("//component");

		//Get component node based on idx
		var compNode:XML = nodelist.Getitem(0);

		//REMOVE
		var zzz:String=compNode.Getxml();

		//Get instanceID attribute
		var map:Dictionary = compNode.Getattributes();
		var b:String=_T("id");
		var atrb:String = map.getNamedItem(b);
		var instanceID:String=atrb.Getvalue();

		//Build component node
		var temp:String = "<component id=\"" + instanceID + "\">";

		//Loop through all transport xml props and copy them to compXML string
		nodelist = pDom.selectNodes("//property");

		//Did we find properties?
		if(nodelist != NULL){
			//Loop
			var numProps:int = nodelist.Getlength();
			var i:int = 0;
			for(i=0;i<numProps;i++){
				//Get property node
				var propNode:XML = nodelist.Getitem(i);

				//REMOVE
				var aaa:String=propNode.text;

				//Get name node
				var nameNode:XML = propNode.selectSingleNode("//name//string");
				var valueNode:XML = propNode.selectSingleNode("//value//string");;

				//Add property to string
				temp = temp + "<" + nameNode.text + ">" + valueNode.text + "</" + nameNode.text + ">";
			}
		}

		//Append close component
		temp = temp + "</component>";

	//	//Copy temp to compXML
	//	compXML = temp.copy(true);
		
		return temp;

	/
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
	/


	/  <component className="xcelsius.controls.List" id="E3422102-7DFD-D555-4631-82372B890576">
		<width>125</width>
		<height>200</height>
		<x>0</x>
		<y>0</y>
		<title>"List Box Title"</title>
	  </component>/
	}
	catch(...)
	{
		assert(false);
		return "";
	}
}

var cmpDocument:Boolean::CreateCanvasBinding(const var transXML:String, cmpBinding binding)
{
	try
	{

		var pDom:XML;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		//Get type node
		var pNode:XML;
		var pNodeList:XMLList;
		pNode = pDom.selectSingleNode("//inputtype");
		//Get first child node
		pNodeList = pNode.GetchildNodes();
		pNode = pNodeList.Getitem(0);
		//Set binding property
		binding.inputtype = pNode.Getxml();

		pNode = pDom.selectSingleNode("//outputtype");
		//Get first child node
		pNodeList = pNode.GetchildNodes();
		pNode = pNodeList.Getitem(0);
		//Set binding property
		binding.outputtype = pNode.Getxml();

		//Get component node
		pNode = pDom.selectSingleNode("//component");
		//Get id attribute
		var map:Dictionary = pNode.Getattributes();
		var b:String=_T("id");
		var atrb:String = map.getNamedItem(b);
		var instanceID:String=atrb.Getvalue();
		//Set binding property
		binding.instanceID = _T("<string>") + instanceID + _T("</string>");

		//Get cells node
		pNode = pDom.selectSingleNode("//cells");
		//Get first child node
		pNodeList = pNode.GetchildNodes();
		pNode = pNodeList.Getitem(0);
		//Set binding property
		binding.map = pNode.Getxml();

		//Get property name node
		pNode = pDom.selectSingleNode("//name");
		//Get first child node
		pNodeList = pNode.GetchildNodes();
		pNode = pNodeList.Getitem(0);
		//Set binding property
		binding.propertyName = pNode.Getxml();

		var pEndpoint:XML = pDom.selectSingleNode("//endpoint");
		map = pEndpoint.Getattributes();
		atrb = map.getNamedItem(_T("id"));
		var v:CComVariant = atrb.Getvalue();
		binding.bindingID = _T("<string>");
		binding.bindingID += (LPCTSTR)v.bstrVal;
		binding.bindingID += _T("</string>");

		// Get direction
		pNode = pEndpoint.selectSingleNode("./direction");
		pNode = pNode.GetfirstChild();
		binding.direction = pNode.Getxml();

		// Get chain
		pNode = pEndpoint.selectSingleNode("./chain");
		if (pNode.hasChildNodes()) {
			pNode = pNode.GetfirstChild();
			binding.chain = pNode.Getxml();
		} else {
			binding.chain = _T("<null/>");
		}

		// Get inputmapproperties
		pNode = pEndpoint.selectSingleNode("./inputmapproperties");
		if (pNode.hasChildNodes()) {
			pNode = pNode.GetfirstChild();
			binding.inputmapproperties = pNode.Getxml();
		} else {
			binding.inputmapproperties = _T("<null/>");
		}

		// Get outputmapproperties
		pNode = pEndpoint.selectSingleNode("./outputmapproperties");
		if (pNode.hasChildNodes()) {
			pNode = pNode.GetfirstChild();
			binding.outputmapproperties = pNode.Getxml();
		} else {
			binding.outputmapproperties = _T("<null/>");
		}

		return true;
	/<?xml version="1.0" encoding="UTF-8"?>
	<component id="CC69BF92-53E5-6249-89BB-9B48A91FF477">
	   <onrequestbindings>
		  <property>
			 <name>
				<string>title</string>
			 </name>
			 <value>
				<binding>
				   <endpovar type:int="excelbinding" displayname="'Sheet1'!$C$5:$D$5">
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
	</component>/
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean::UpdateCanvasBinding(const var transXML:String, cmpBinding binding)
{
	try
	{
		var pDom:XML;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		// get the binding id
		var pNode:XML;
		pNode = pDom.selectSingleNode("/binding/endpoint/@id");
		var bindingId:String = (LPCTSTR)pNode.text;
		//binding.bindingID = bindingId;
		binding.bindingID = _T("<string>") + bindingId + _T("</string>");
		pNode = pNode.selectSingleNode("../cells");
		//var clone:XML = pNode.cloneNode(VARIANT_TRUE);

		var pDom2:XML = pXmlDOM;
			// find the node
		var endpointPath:String = "//binding/endpoint[@id='";
		endpointPath += (LPCTSTR)bindingId;
		endpointPath += "']";
		var endpointNode:XML = pDom2.selectSingleNode((LPCTSTR)endpointPath);
		var cellsNode:XML = endpointNode.selectSingleNode("./cells");
		var displayNode:XML = endpointNode.selectSingleNode("./@displayname");
		var rangeNode:XML = endpointNode.selectSingleNode("./range");
		if(pNode)
		{
			cellsNode.replaceChild(pNode.GetfirstChild(), cellsNode.GetfirstChild());
			pNode = pNode.selectSingleNode("../@displayname");
			displayNode.replaceChild(pNode.GetfirstChild(), displayNode.GetfirstChild());
			pNode = pNode.selectSingleNode("../range");
			rangeNode.replaceChild(pNode.GetfirstChild(), rangeNode.GetfirstChild());
		}
		else //the case when binding came empty- for example, all cells have been deleted - so we need to remove it
		{
			var bindingNode:XML=endpointNode.GetparentNode();
			var valueNode:XML=bindingNode.GetparentNode();
			var propertyNode:XML=valueNode.GetparentNode();
			var bindingsNode:XML=propertyNode.GetparentNode();
			bindingsNode.removeChild(propertyNode);
			return false;
		}		

		var guidNode:XML = endpointNode.selectSingleNode("../../../../../@id");
		var guidId:String = (LPCTSTR)guidNode.text;
		binding.instanceID = _T("<string>") + guidId + _T("</string>");

		var propNode:XML = endpointNode.selectSingleNode("../../../name/string");
		binding.propertyName = propNode.Getxml();

		//Get type node
		binding.inputtype = endpointNode.selectSingleNode("./inputtype").GetfirstChild().Getxml();
		binding.outputtype = endpointNode.selectSingleNode("./outputtype").GetfirstChild().Getxml();
		binding.direction = endpointNode.selectSingleNode("./direction").GetfirstChild().Getxml();
		binding.chain = endpointNode.selectSingleNode("./chain").GetfirstChild().Getxml();
		binding.inputmapproperties = endpointNode.selectSingleNode("./inputmapproperties").GetfirstChild().Getxml();
		binding.outputmapproperties = endpointNode.selectSingleNode("./outputmapproperties").GetfirstChild().Getxml();
		binding.map = endpointNode.selectSingleNode("./cells").GetfirstChild().Getxml();

		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:Boolean::GetBinding(const var compID:String,
														 const var bindingId:String,
														 CString& valueXML
														)
{
	try
	{
		var pDom:XML = pXmlDOM;

		//Get component node
		var compNode:XML;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");

		}
		if(compNode ==NULL)
		{
			// component not found
			return false;
		}

		//Select bindings node
		var bindingsNode:XML;
		bindingsNode = compNode.selectSingleNode("./bindings");

		if (bindingsNode) {
			// find the node
			var propPath:String = "./property/value/binding[endpoint/@id='";
			propPath += bindingId;
			propPath += "']";

			var propNode:XML;
			propNode = bindingsNode.selectSingleNode(_bstr_t(propPath));
			if (propNode) {
				valueXML = (LPCTSTR)propNode.Getxml();
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

var cmpDocument:Boolean::SetBinding(const var transXML:String,var bRemoveThisBinding:Boolean)
{
	try
	{

		var pDom:XML;
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		//Get component id from trans xml
		//Get component node
		var cNode:XML;
		cNode = pDom.selectSingleNode("//component");
		//Get id attribute
		var map:Dictionary = cNode.Getattributes();
		var b:String=_T("id");
		var atrb:String = map.getNamedItem(b);
		var instanceID:String=atrb.Getvalue();

		//Select property node in trans xml
		var pNode:XML;
		pNode = cNode.selectSingleNode("./onrequestbindings//property");

		var aaa:String = pNode.Getxml();

		//Get the property name
		var propNameNode:XML;
		propNameNode = pNode.selectSingleNode("./name//string");
		var propName:String = propNameNode.text;

		// get the binding id
		var bindingIdNode:XML;
		bindingIdNode = pNode.selectSingleNode("./value/binding/endpoint/@id");
		var bindingId:String = bindingIdNode.text;

		//clone the property node in trans xml
		var clone:XML = pNode.cloneNode(VARIANT_TRUE);

		//Select the component node in document xml
		var pDom2:XML = pXmlDOM;

		//Get compnent node
		var compNode:XML;
		compNode = pDom2.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		compNode = pDom2.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");

		//Select bindings node
		var bindingsNode:XML;
		bindingsNode = compNode.selectSingleNode("./bindings");

		//If no bidings node then add it
		if(bindingsNode == NULL){
			//Create bindings node
			bindingsNode = pDom2.createElement("bindings");
			compNode.appendChild(bindingsNode);
		}

		//If binding exists then delete it
		if(bRemoveThisBinding){
			// find the node
			var propPath:String = "./property[name/string='";
			propPath += (LPCTSTR)propName;
			propPath += "' and value/binding/endpoint/@id='";
			propPath += (LPCTSTR)bindingId;
			propPath += "']";

			var propNode:XML;
			propNode = bindingsNode.selectSingleNode(_bstr_t(propPath));
			if (propNode)
				propNode.parentNode.removeChild(propNode);
		}
		else
		{
			//Add cloned property node to bindings node if necessary
			var propPath:String = "./property/value/binding/endpoint[@id='";
			propPath += (LPCTSTR)bindingId;
			propPath += "']";

			var oldBinding:XML = bindingsNode.selectSingleNode(_bstr_t(propPath));

			if(!oldBinding)  //don't append the clone if it's already there
			{
				bindingsNode.appendChild(clone);
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

var cmpDocument:Array::RemoveBinding(std::vector<_bstr_t> guids, const CString& id)
{
	var bindingIDs:Array;//result

	try
	{
		//Select the component node in document xml
		var pDom2:XML = pXmlDOM;
		var guid:String=guids[0];
		var bMultipleComps:Boolean=(guids.size()>1);
		//Get compnent node
		var compNode:XML;
		compNode = pDom2.selectSingleNode("//component[@id='" + guid + "']");
		if(!compNode)
		{
			compNode = pDom2.selectSingleNode("//connection[@id='" + guid + "']");
		}

		//Select bindings node
		var bindingsNode:XML;
		bindingsNode = compNode.selectSingleNode("./bindings");

		if(bindingsNode == NULL){
			return bindingIDs;
		}

		// find the node
		var propPath:String = "./property[value/binding/endpoint/@id='";
		propPath += (LPCTSTR)id;
		propPath += "']";

		var propNode:XML;
		propNode = bindingsNode.selectSingleNode(_bstr_t(propPath));
		if (propNode) 
		{
			bstr_t t=propNode.Getxml();
			propNode.parentNode.removeChild(propNode);
			bindingIDs.push_back(_bstr_t(id));
		}
		if(bMultipleComps && propNode)
		{
			//if it's series, find the index
			var nameNode:XML=propNode.selectSingleNode("name");
			var seriesIndex:int=-1;
			var name:String=nameNode.Gettext();
			var chainNode:XML=propNode.selectSingleNode("value/binding/endpoint/chain");
			if(chainNode)
			{
				
				var propIndexNode:XML =chainNode.selectSingleNode("array/property/object/property[@id='properties']/array/property/object/property[@id='name'][./string='index']");
				if(propIndexNode)
				{
					var bs:String=propIndexNode.Getxml();
					var indexValNode:XML=propIndexNode.GetparentNode().selectSingleNode("property[@id='value']");
					bs=indexValNode.Gettext();
					seriesIndex=_ttoi((TCHAR)bs);
				}
			}
			for(UINT i=1;i<guids.size();i++)
			{
				var nextCompNode:XML = pDom2.selectSingleNode("//component[@id='" + guids[i] + "']");
				if(!nextCompNode)
					continue;
				var nextNameNodes:XMLList = nextCompNode.selectNodes("./bindings/property/name[./string='"+name+"']");
				if(!nextNameNodes || nextNameNodes.Getlength()==0)
					continue;
				if(nextNameNodes.Getlength()==1)
				{
					var nextNameNode:XML=nextNameNodes.Getitem(0);
					var t:String=nextNameNode.Getxml();
					var endpointNode:XML=nextNameNode.selectSingleNode("../value/binding/endpoint");
					if(endpointNode)
					{
						var map:Dictionary=endpointNode.Getattributes();
						var atrb:String = map.getNamedItem("id");
						if(atrb!=0)
						{
							var ID:String=atrb.Getvalue();
							bindingIDs.push_back(ID);
							var delNode:XML=nextNameNode.GetparentNode();
							delNode.GetparentNode().removeChild(delNode);
						}
					}
				}
				else if (nextNameNodes.Getlength()>1)//subelement binding - so we need to remove the node with the same series number
				{
					for(var j:int=0;j<nextNameNodes.Getlength();j++)
					{
						var nextNameNode:XML=nextNameNodes.Getitem(j);
						var bs:String=nextNameNode.Getxml();
						var propIndexNode:XML =nextNameNode.selectSingleNode("../value/binding/endpoint/chain/array/property/object/property[@id='properties']/array/property/object/property[@id='name'][./string='index']");
						if(propIndexNode)
						{
							var bs:String=propIndexNode.Getxml();
							var indexValNode:XML=propIndexNode.GetparentNode().selectSingleNode("property[@id='value']");
							bs=indexValNode.Gettext();
							var index:int=_ttoi((TCHAR)bs);
							if(index==seriesIndex)
							{
								var endpointNode:XML=nextNameNode.selectSingleNode("../value/binding/endpoint");
								if(endpointNode)
								{
									var map:Dictionary=endpointNode.Getattributes();
									var atrb:String = map.getNamedItem("id");
									if(atrb!=0)
									{
										var ID:String=atrb.Getvalue();
										bindingIDs.push_back(ID);
										var delNode:XML=nextNameNode.GetparentNode();
										delNode.GetparentNode().removeChild(delNode);
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
		var pDom:XML = pXmlDOM;

		//Get CXCanvas node
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		if ( canvasNode != NULL )
		{
			var map:Dictionary = canvasNode.Getattributes();
			if (map != NULL)
			{
				var atrb:String = map.getNamedItem("width");
				if (atrb != NULL)
				{
					nWidth=atrb.Getvalue();
				}
				atrb = map.getNamedItem("height");
				if (atrb != NULL)
				{
					nHeight=atrb.Getvalue();
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
void cmpDocument::SetCanvasSize(var nWidth:int, var nHeight:int)
{
	try
	{
		//Set doc pointer;
		var pDom:XML = pXmlDOM;
		
		//Get CXCanvas node
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var map:Dictionary = canvasNode.Getattributes();
		var pXMLAttribute:String=pDom.createAttribute("width");
		pXMLAttribute.nodeValue=nWidth;
		map.setNamedItem(pXMLAttribute);
		pXMLAttribute=pDom.createAttribute("height");
		pXMLAttribute.nodeValue=nHeight;
		map.setNamedItem(pXMLAttribute);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
std::vector<CString> cmpDocument::GetBindingsForProperty(var instanceID:String,var propName:String)
{
	std::vector<CString> result;
	try
	{
	var pDom:XML = pXmlDOM;
	var compNode:XML=NULL;
	compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
	if(!compNode)
	{	
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
	}
	if(!compNode)
	{
		return result;
	}
	var tmp:String=compNode.Gettext();
	
		var bindingsNode:XML=compNode.selectSingleNode("bindings");
		if(!bindingsNode)
			return result;
		var propNode:XML=NULL;
		var propPath:String = "./property/name[./string = '"+propName+"']";
		propNode = bindingsNode.selectSingleNode(_bstr_t(propPath));
		var nodeList:XMLList=NULL;
		if(propNode)
		{
			var valueNode:XML=propNode.GetparentNode().selectSingleNode("value");
			if(valueNode)
			{
				nodeList=valueNode.selectNodes("./binding/endpoint");
					
			var n:int = nodeList.Getlength();
			for(var i:int=0;i<n;i++)
			{
				var pEndPointNode:XML= nodeList.item[i];
				var map:Dictionary = pEndPointNode.Getattributes();
				var atrbID:String = map.getNamedItem("id");
				var ID:String=atrbID.Getvalue();
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
void cmpDocument::CreateCSSStyleDeclaration(var guid:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
		var stylesNode:XML= canvasNode.selectSingleNode("Styles");
		if(stylesNode==NULL)
		{
			stylesNode = pDom.createElement("Styles");
			canvasNode.appendChild(stylesNode);
		}
		var styleNode:XML = pDom.createElement("style");
		var bs:String=guid;
		styleNode.setAttribute("id",bs);
		stylesNode.appendChild(styleNode);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
var cmpDocument:Boolean::SetPersist(var compID:String,var xmlString:String)
{
	try
	{
		if(xmlString.IsEmpty())
		{
			return true;//there is nothing to set - component does not have the functionality yet
		}
		var pDom:XML = pXmlDOM;
		var compNode:XML=NULL;
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{	
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		var persistNode:XML= compNode.selectSingleNode("Persist");
		var pDOM2:XML;
		pDOM2.CreateInstance(__uuidof(DOMDocument40));
		pDOM2.PutpreserveWhiteSpace(VARIANT_FALSE);
		//if we come the first time we just uppend the whole persist string
		if(persistNode==NULL)
		{
			persistNode = pDom.createElement("Persist");
			compNode.appendChild(persistNode);
			VARIANT_BOOL loadSuccess =  pDOM2.loadXML(_bstr_t(xmlString));
			if(loadSuccess==VARIANT_TRUE)
			{
				var root:XML = pDOM2.documentElement;
				persistNode.appendChild(root.cloneNode(VARIANT_TRUE));
			}
			return true;
		}
		//if we got here we have to merge persist
		var child:XML=persistNode.GetfirstChild();
		var oldArr:CArrayArgument;
		oldArr.loadString(child.Getxml());
		var nOldNames:int=oldArr.size();
		//the key in the map is the property name; it contains indexes of the arguments in oldArr
		std::map <CString,int> oldNameNodes;
		for(var i:int=0;i<nOldNames;i++)
		{
			var a:CArgument=oldArr.get(i);
			if(a.getType()==CArgument::object)
			{
				var pObj:CObjectArgument=(CObjectArgument)a;
				CStringArgument pNameArg=(CStringArgument)(pObj.get("name"));
				var propName:String=pNameArg.getValue();
				oldNameNodes[propName]=i;
			}
		}
				
		var arrArg:CArrayArgument;
		arrArg.loadString(xmlString);
		var n:int = arrArg.size();
		for(var i:int=0;i<n;i++)
		{
			var pArg:CArgument = arrArg.get(i);
			if( pArg.getType() == CArgument::object)
			{
				var pObjArg:CObjectArgument = (CObjectArgument) pArg;
				CStringArgument pArName=(CStringArgument)(pObjArg.get(_T("name")));
				var propName:String=pArName.getValue();
				var pArValue:CArgument=pObjArg.get(_T("value"));
				if(oldNameNodes.find(propName)==oldNameNodes.end())
				{
					//the property is new - append it to array
					oldArr.add(pObjArg.clone());
				}
				else
				{
					var index:int=oldNameNodes[propName];
					((CObjectArgument)oldArr.get(index)).set(_T("value"),pArValue.clone());
				}
			}
		}
		var strRes:String;
		oldArr.saveString(&strRes);
		VARIANT_BOOL loadSuccess =  pDOM2.loadXML(_bstr_t(strRes));
		if(loadSuccess==VARIANT_TRUE)
		{
			var root:XML = pDOM2.documentElement;
			persistNode.replaceChild(root.cloneNode(VARIANT_TRUE),child);
		}
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
var cmpDocument:String::GetPersist(var compID:String)
{
	var res:String;
	try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML=NULL;
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{	
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return res;
		}
		var persistNode:XML= compNode.selectSingleNode("Persist");
		if(persistNode==NULL)
		{
			return res;
		}
		var child:XML=persistNode.GetfirstChild();
		if(child==NULL)
		{
			return res;
		}
		var bs:String=child.Getxml();
		res=CString((TCHAR)bs);
	}
	catch(...)
	{
		assert(false);
	}
	return res;
}
void cmpDocument::SetExportSettings(var bUseCurrent:Boolean,var path:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var settNode:XML=NULL;
		var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
		settNode = canvasNode.selectSingleNode("ExportSettings");
		if(!settNode)
		{
			settNode = pDom.createElement(_T("ExportSettings"));
			canvasNode.appendChild(settNode);
		}
			settNode.setAttribute(_T("useCurrent"),bUseCurrent);
			var bs:String=path;
			settNode.setAttribute(_T("path"),bs);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
var cmpDocument:Boolean::GetExportSettings(bool& bUseCurrent,CString& path)
{
	bUseCurrent = true;
	try
	{
		var pDom:XML = pXmlDOM;
		var settNode:XML=NULL;
		var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
		settNode = canvasNode.selectSingleNode("ExportSettings");
		if(!settNode)
		{
			return false;
		}
		var map:Dictionary = settNode.Getattributes();
		if(!map)
			return false;
		bstr_t b=_T("useCurrent");
		var atrb:String = map.getNamedItem(b);
		if(!atrb)
			return false;
		bUseCurrent=atrb.Getvalue();
		b=_T("path");
		var atrb1:String = map.getNamedItem(b);
		if(!atrb1)
			return false;
		var bspath:String=atrb1.Getvalue();
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
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
	var airNode:XML = canvasNode.selectSingleNode("AirSettings");
	if(!airNode) {
		airNode = pDom.createElement(_T("AirSettings"));
		canvasNode.appendChild(airNode);
	}
	var bs:String = settings.name;
	airNode.setAttribute(_T("name"),bs);
	bs = settings.id;
	airNode.setAttribute(_T("id"),bs);
	bs = settings.version;
	airNode.setAttribute(_T("version"),bs);
	bs = settings.desc;
	airNode.setAttribute(_T("description"),bs);
	bs = settings.copyright;
	airNode.setAttribute(_T("copyright"),bs);
	bs = settings.img16;
	airNode.setAttribute(_T("img16"),bs);
	bs = settings.img32;
	airNode.setAttribute(_T("img32"),bs);
	bs = settings.img48;
	airNode.setAttribute(_T("img48"),bs);
	bs = settings.img128;
	airNode.setAttribute(_T("img128"),bs);

	airNode.setAttribute(_T("custom"),settings.custom);
	airNode.setAttribute(_T("transparent"),settings.transparent);
	bs = settings.path;
	airNode.setAttribute(_T("path"),bs);
	airNode.setAttribute("useCustomSize",settings.bCustomWindowSize);
	airNode.setAttribute("windowWidth",settings.nWinWidth);
	airNode.setAttribute("windowHeight", settings.nWinHeight);
}
var cmpDocument:Boolean::GetAirSettings(AirSettings& settings) //GV
{
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
	var airNode:XML = canvasNode.selectSingleNode("AirSettings");
	if(!airNode) return false;

	var map:Dictionary = airNode.Getattributes();
	if(!map) return false;

	bstr_t b =_T("name");
	var atrb:String = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.name = (LPCTSTR)b;

	b =_T("id");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.id = (LPCTSTR)b;

	b =_T("version");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.version = (LPCTSTR)b;

	b =_T("description");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.desc = (LPCTSTR)b;

	b =_T("copyright");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.copyright = (LPCTSTR)b;

	b =_T("img16");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.img16 = (LPCTSTR)b;

	b =_T("img32");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.img32 = (LPCTSTR)b;

	b =_T("img48");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.img48 = (LPCTSTR)b;

	b =_T("img128");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.img128 = (LPCTSTR)b;

	b =_T("custom");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	settings.custom = atrb.Getvalue();

	b =_T("transparent");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	settings.transparent = atrb.Getvalue();

	b =_T("path");
	atrb = map.getNamedItem(b);
	if(!atrb) return false;
	b = atrb.Getvalue();
	settings.path = (LPCTSTR)b;

	atrb = map.getNamedItem("useCustomSize");
	if(!atrb)
	{
		settings.bCustomWindowSize=false;
	}
	else
	{
		settings.bCustomWindowSize=atrb.Getvalue();
	}
	var nCanvasWidth:int, nCanvasHeight;
	GetCanvasSize(nCanvasWidth, nCanvasHeight);
	atrb = map.getNamedItem("windowWidth");
	if(!atrb)
	{
		settings.nWinWidth=nCanvasWidth;
	}
	else
	{
		settings.nWinWidth=atrb.Getvalue();
	}
	atrb = map.getNamedItem("windowHeight");
	if(!atrb)
	{
		settings.nWinHeight=nCanvasHeight;
	}
	else
	{
		settings.nWinHeight=atrb.Getvalue();
	}
	return true;

}
void cmpDocument::GetAirWindowSize(int& nWidth, int& nHeight)
{
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
	var airNode:XML = canvasNode.selectSingleNode("AirSettings");
	if(!airNode) 
		return GetCanvasSize(nWidth, nHeight);

	var map:Dictionary = airNode.Getattributes();
	if(!map) 
		return GetCanvasSize(nWidth, nHeight);
	var atrb:String = map.getNamedItem("useCustomSize");
	if(!atrb)
	{
		return GetCanvasSize(nWidth, nHeight);
	}
	else
	{
		var t:String=_bstr_t(atrb.Getvalue());
		if(t==_bstr_t("0"))
		{
			return GetCanvasSize(nWidth, nHeight);
		}
	}
	var atrb1:String = map.getNamedItem("windowWidth");
	var atrb2:String = map.getNamedItem("windowHeight");
	if(!atrb1 || !atrb2)
	{
		return GetCanvasSize(nWidth, nHeight);
	}
	nWidth=atrb1.Getvalue();
	nHeight=atrb2.Getvalue();
}

var cmpDocument:Boolean::FindComponentByProperty(var className:String,var propertyName:String,var propertyValueXML:String,var bCompIsConn:Boolean)
{
	try
	{
		var pDom:XML = pXmlDOM;

		//Get node list
		var nodelist:XMLList;
		if(bCompIsConn)
		{
			nodelist = pDom.selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
		}
		else
		{
			nodelist = pDom.selectNodes("/CXCanvas/component[@className='" + _bstr_t(className) + "']");
		}
		
		var n:int = nodelist.Getlength();
		for(var i:int=0;i<n;i++)
		{
			var compNode:XML=nodelist.Getitem(i);
			var propertiesNode:XML = compNode.selectSingleNode("properties");
			if(propertiesNode != NULL)
			{
				var propNode:XML=NULL;
				propNode = propertiesNode.selectSingleNode("./property//name[./string = '" + _bstr_t(propertyName) + "']/following-sibling::");
				if(propNode != NULL)
				{
					var oldValue:XML = propNode.GetfirstChild();
					if ( oldValue != NULL )
					{
						var valueXML:String=(LPCTSTR)(oldValue.Getxml());
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


var cmpDocument:Boolean::GetSize(const var fn:String,var warningImage:String)
{
	FILE f = _tfopen(fn,_T("rb"));
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

var cmpDocument:Boolean::ConvertFiles(const var filename:String, CString &tempFile,var embed:Boolean)
{
	try
	{
	//we should probably move this function up to XcelsiusDoc
	var fileExt:String;
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

	var uniqExt:String;

	if((_tcsicmp(fileExt.c_str(),_T(".gif"))==0)||(_tcsicmp(fileExt.c_str(),_T(".bmp"))==0)||(_tcsicmp(fileExt.c_str(),_T(".png"))==0))
		uniqExt=_T(".jpg");
	else
		uniqExt=fileExt;

	var tmpname:String = imutil::GetUnique(tempFile, uniqExt.c_str());

	//make sure the path exists
	var buff:String = _tcsdup(tmpname.c_str());
	::PathRemoveFileSpec(buff);
	_tmkdir(buff);
	delete [] buff;

	tempFile = tmpname.c_str();

	var name:String=tmpname.substr(0,tmpname.find_last_of(_T(".")));
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



var cmpDocument:String::CreateAssetProperty(const var assetNode:String,var lastNode:XML)
{
	//var result:String = _T("<array>");
	
	var arrNode:XML = pAssetProp.createElement("array");	
	if(lastNode==NULL)
		pAssetProp.appendChild(arrNode);
	else
		lastNode.appendChild(arrNode);

	//we need to parse the assets node and create the equivalent property value
	var ptemp:XML;
	ptemp.CreateInstance(__uuidof(DOMDocument40));
	ptemp.loadXML( _bstr_t(assetNode) );
	var parentNode:XML = ptemp.GetfirstChild();

	var assets:XMLList = parentNode.selectNodes("asset");
	if(assets && assets.Getlength())
	{
		for(var i:int=0; i<assets.Getlength(); i++)
		{
			//var prop:String;
			//prop.Format(_T("<property id=\"%d\">"), i);
			//result += prop;

			var propNode:XML = pAssetProp.createElement("property");
			std::wostringstream oss;oss<<i;
			var num:String=oss.str();
			propNode.setAttribute("id",num.c_str());
			arrNode.appendChild(propNode);

			var anode:XML = assets.Getitem(i);
			var anodeList:XML = anode.selectSingleNode("array");
			if( anodeList )
			{
				//Lazy way, This should be redone before release
				CreateAssetProperty(anodeList.Getxml(),propNode);				

			}
			else
			{
				var valueNode:XML = anode.selectSingleNode("temporaryFile");
				if(valueNode)
				{
					//var v:String = valueNode.Gettext();
					//prop.Format(_T("<string>%s</string>"), v.c_str());
					//result += prop;

					var strNode:XML = pAssetDoc.createElement("string");
					strNode.text =  valueNode.Gettext();
					propNode.appendChild(strNode);


				}
			}
			//result += _T("</property>");
		}
	}

	//result += _T("</array>");
	var result:String = pAssetProp.xml;
	return result;
}

void cmpDocument::CreateAssetValue(const var tempFile:String, const var origFile:String, const var embed:String,var assetENode:XML)
{
	//var result:String;
	var tempNode:XML = pAssetDoc.createElement("temporaryFile");
	tempNode.text = tempFile;
	var origNode:XML = pAssetDoc.createElement("originalFile");
	origNode.text = origFile;
	var embedNode:XML = pAssetDoc.createElement("embed");
	embedNode.text = embed;

	assetENode.appendChild(tempNode);
	assetENode.appendChild(origNode);
	assetENode.appendChild(embedNode);
	

	/
	var temp:String = _T("<temporaryFile>"); temp+= tempFile; temp += _T("</temporaryFile>");
	var orig:String = _T("<originalFile>"); orig+= origFile; orig += _T("</originalFile>");
	var embedMethod:String = _T("<embed>"); embedMethod+= embed; embedMethod += _T("</embed>");

	result = temp;
	result += orig;
	result += embedMethod;

	return result;/
}

var cmpDocument:Boolean::CreateAssetArray(var filenames:CArrayArgument, var embed:CArrayArgument,var lastNode:XML)
{



	var arrNode:XML = pAssetDoc.createElement("array");	

	if(lastNode==NULL)
		pAssetDoc.appendChild(arrNode);
	else
		lastNode.appendChild(arrNode);	


	//

	//only return false if one of the images failed to convert
	//result = _T("");

	//loop through each 
	
	var count:int = filenames.size();

	if( count != embed.size() || count == 0)
		return true;

	//result = _T("<array>");

	for( var i:int=0; i<count; i++)
	{
		var file:CArgument = filenames.get(i);


		var assetNode:XML = pAssetDoc.createElement("asset");
		std::wostringstream oss;oss<<i;
		var num:String=oss.str();
		assetNode.setAttribute("id",num.c_str());
		arrNode.appendChild(assetNode);

		if( file.getType() == CArgument::string )
		{	
			CStringArgument embedType= (CStringArgument)embed.get(i);
			CStringArgument argString = (CStringArgument)file;

			var tempFile:String = _T("");
			if( _tcscmp(embedType.getValue(),_T("url")) )//convert anything but URL 
			{
				var emb:Boolean=false;
				if((_tcscmp(embedType.getValue(),_T("embed"))==0)||(_tcscmp(embedType.getValue(),_T("true"))==0))
					emb=true;

				
				var res:Boolean = ConvertFiles((LPCTSTR)argString.getValue(), tempFile,emb);
				if(res==false)
				{
					if(assetsFromXlf.find((LPCTSTR)argString.getValue())==assetsFromXlf.end())
					{
						return false; //one of the images failed.
					}
					else
					tempFile=(assetsFromXlf[(LPCTSTR)argString.getValue()]).c_str();
				}
				
			}
			else
				tempFile = argString.getValue();//just duplicate actual path

		
			CreateAssetValue(tempFile, argString.getValue(), embedType.getValue(),assetNode);
		}
		else
		if( file.getType() == CArgument::array )
		{
			var argArray:CArrayArgument =(CArrayArgument)file;
			var embedArray:CArrayArgument= (CArrayArgument)embed.get(i);
			var res:Boolean = CreateAssetArray(argArray, embedArray,assetNode);
			if(res==false) return false;
		}
		/

		var nodeString:String;
		nodeString.Format(_T("<asset id=\"%d\">"), i);
		nodeString += assetNode.c_str();
		nodeString += _T("</asset>");

		result += nodeString;/
	}

	//result += _T("</array>");

	return true;
}

var cmpDocument:Boolean::SetAssets(const var compID:String, const var propName:String, var fileName:CArrayArgument, var embed:CArrayArgument, CString &valueXML)
{

	//find the component node
	var pDom:XML = pXmlDOM;
	var compNode:XML=NULL;
	compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
	{
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
	}
	if(!compNode) return false;

	//check that the arrays are same dimension
	if( fileName.size() != embed.size())
		return false;

	//does the meat of everything
	var asset:String;

	pAssetDoc = NULL;
	pAssetDoc.CreateInstance("Msxml2.DOMDocument.4.0");

	var result:Boolean = CreateAssetArray( fileName, embed );
	if(!result) return false;


	//need the assets node to set the asset
	var assetsNode:XML = compNode.selectSingleNode("./assets");

	//if no assets node, then make one
	if(!assetsNode) 
	{
		assetsNode = pDom.createElement("assets");
		if(compNode) compNode.appendChild(assetsNode);
	}

	//find asset
	var pname:String = propName;
	var assetPath:String = "./asset/name[./string = '"+pname+"']";
	var oldAssetNode:XML;
	if( assetsNode.selectSingleNode(_bstr_t(assetPath)) )
		oldAssetNode = assetsNode.selectSingleNode(_bstr_t(assetPath)).GetparentNode();

	var newAssetNode:XML = pDom.createElement("asset");
	var nameNode:XML = pDom.createElement("name");
	var nameChild:XML = pDom.createElement("string");
	nameChild.text = propName;
	var valueNode:XML = pDom.createElement("value");

	newAssetNode.appendChild(nameNode);
	newAssetNode.appendChild(valueNode);
	nameNode.appendChild(nameChild);

	//begin lame way to create a node from markup in MSXML2
	var ptemp:XML;
	ptemp.CreateInstance(__uuidof(DOMDocument40));
	ptemp.loadXML( pAssetDoc.xml );
	var bindTemp:XML = ptemp.GetfirstChild();
	var bindings:XML = bindTemp.cloneNode(VARIANT_TRUE);

	valueNode.appendChild(bindings);
	//end lameness

	if(oldAssetNode)
		assetsNode.replaceChild(newAssetNode, oldAssetNode);
	else
		assetsNode.appendChild(newAssetNode);

	pAssetProp = NULL;
	pAssetProp.CreateInstance("Msxml2.DOMDocument.4.0");
	//need the properties node to set the property..this is going to be an array of stuff
	valueXML = CreateAssetProperty(pAssetDoc.xml).c_str();

	return true;
}

var cmpDocument:Boolean::SetCanvasAsset(const var propName:String, const var fileName:String, var embed:Boolean, CString &valueXML)
{
	try
	{
		var temp:String;
		if( ConvertFiles(fileName, temp, embed) == false ) return false;

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		var pDom:XML = pXmlDOM;
		var compNode:XML=NULL;
		compNode = pDom.selectSingleNode("//canvas");

		if(!compNode)
		{
			compNode = pDom.createElement("canvas");
			var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
			canvasNode.appendChild(compNode);
		}


		//need the assets node to set the asset
		var assetsNode:XML = compNode.selectSingleNode("./assets");

		//if no assets node, then make one
		if(!assetsNode) 
		{
			assetsNode = pDom.createElement("assets");
			if(compNode) compNode.appendChild(assetsNode);
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

var cmpDocument:Boolean::SetAsset(const var compID:String, const var propName:String, const var fileName:String, var embed:Boolean, CString &valueXML)
{
	try
	{
		var temp:String;
		if( ConvertFiles(fileName, temp,embed) == false ) return false;

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		var pDom:XML = pXmlDOM;
		var compNode:XML=NULL;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}

		if(!compNode) return false;


		//need the assets node to set the asset
		var assetsNode:XML = compNode.selectSingleNode("./assets");

		//if no assets node, then make one
		if(!assetsNode) 
		{
			assetsNode = pDom.createElement("assets");
			if(compNode) compNode.appendChild(assetsNode);
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

void cmpDocument::setAsset(const var assetsNode:XML, const var propName:String, const TCHAR  originalFileName, const TCHAR  tmpFileName, var embed:Boolean)
{
	var pDom:XML = pXmlDOM;
	//find asset
	var pname:String = propName;
	var assetPath:String = "./asset/name[./string = '"+pname+"']";
	var oldAssetNode:XML;
	if( assetsNode.selectSingleNode(_bstr_t(assetPath)) )
		oldAssetNode = assetsNode.selectSingleNode(_bstr_t(assetPath)).GetparentNode();

	var newAssetNode:XML = pDom.createElement("asset");
	var nameNode:XML = pDom.createElement("name");
	var valueNode:XML = pDom.createElement("value");
	var nameChild:XML = pDom.createElement("string");
	nameChild.text = propName;

	newAssetNode.appendChild(nameNode);
	newAssetNode.appendChild(valueNode);
	nameNode.appendChild(nameChild);

	var tempFileName:XML = pDom.createElement("temporaryFile");
	tempFileName.text = _bstr_t(tmpFileName);
	valueNode.appendChild(tempFileName);

	var origFileName:XML = pDom.createElement("originalFile");
	origFileName.text = originalFileName;
	valueNode.appendChild(origFileName);

	var embedTag:XML = pDom.createElement("embed");
	embedTag.text = embed==true?"true":"false";
	valueNode.appendChild(embedTag);

	if(oldAssetNode)
		assetsNode.replaceChild(newAssetNode, oldAssetNode);
	else
		assetsNode.appendChild(newAssetNode);
}

var cmpDocument:String::GetAsset(const var compID:String)
{
	try
	{
		var asset:String=_T("");

		var pDom:XML = pXmlDOM;
		var compNode:XML=NULL;
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}

		if(compNode)
		{
			//need the assets node to set the asset
			var assetsNode:XML = compNode.selectSingleNode("./assets");
			var buff:String = assetsNode.xml;

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


var cmpDocument:Boolean::SetEmbed(const var compID:String, const var propName:String, var embed:Boolean)
{
	try
	{

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		var pDom:XML = pXmlDOM;
		var compNode:XML=NULL;
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}

		if(!compNode) return false;


		//need the assets node to set the asset
		var assetsNode:XML = compNode.selectSingleNode("./assets/asset/name[string='" + _bstr_t(propName) + "']/parent::");

		//if no assets node, return
		if(!assetsNode) return false;

		//we need the temporary file name and to change the embed
		var embedNode:XML = assetsNode.selectSingleNode("./value/embed");

		if(!embedNode) return false;

		//set the embed flag
		embedNode.text = embed?_T("true"):_T("false");	
	
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
		var pDom:XML = pXmlDOM;

		//Get CXCanvas node
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var nodelist:XMLList = canvasNode.selectNodes("connection");
		var n:int = nodelist.Getlength();
		for(var i:int=0; i<n; i++ )
		{
			var connNode:XML = nodelist.Getitem(i);
			canvasNode.removeChild(connNode);
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
std::vector<CString> cmpDocument::ApplyColorSchemeToExistingComponents(var csColorScheme:String)
{
	std::vector<CString> ids;
	try
	{
		//returns vector of component ids where it was applied
		var pDom:XML = pXmlDOM;
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var nodelist:XMLList = canvasNode.selectNodes("//component");
		var n:int = nodelist.Getlength();
		for(var i:int=0; i<n; i++ )
		{
			var compNode:XML = nodelist.Getitem(i);
			var colorSchemeNode:XML=compNode.selectSingleNode("colorScheme");
			if(!colorSchemeNode)
			{
				colorSchemeNode = pDom.createElement("colorScheme");
				compNode.appendChild(colorSchemeNode);
			}
			colorSchemeNode.text = _bstr_t(csColorScheme);
			//Get id
			var map:Dictionary = compNode.Getattributes();
			var b:String=_T("id");
			var atrb:String = map.getNamedItem(b);
			var id:String=atrb.Getvalue();
			var csId:String=(TCHAR)id;
			ids.push_back(csId);
		}
	}
	catch(...)
	{
		assert(false);
	}
	return ids;
}
void cmpDocument::SetCurrentColorScheme(var csColorScheme:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var colorSchemeNode:XML=canvasNode.selectSingleNode("colorScheme");
		if(!colorSchemeNode)
		{
			colorSchemeNode = pDom.createElement("colorScheme");
			canvasNode.appendChild(colorSchemeNode);
		}
		colorSchemeNode.text = _bstr_t(csColorScheme);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}

const var cmpDocument:String::GetColorScheme(const var id:String)
{
	return colorSchemesXMLMap[id];
}


var cmpDocument:String::GetCurrentColorSchemeID()
{
	var res:String;
	try
	{
		var pDom:XML = pXmlDOM;
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var colorSchemeNode:XML=canvasNode.selectSingleNode("colorScheme");
		if(!colorSchemeNode)
		{
			return res;
		}
		res=(TCHAR)(colorSchemeNode.text);
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
		var pDom:XML = pXmlDOM;
		var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
		var nodelist:XMLList=canvasNode.selectNodes("component/colorScheme");
		var n:int=nodelist.Getlength();
		for(var i:int=0;i<n;i++)
		{
			var bs:String=nodelist.Getitem(i).Gettext();
			var str:String=(TCHAR)bs;
			colorSchemeSet.insert(str);
		}
		var canvasSchemeNode:XML=canvasNode.selectSingleNode("colorScheme");
		if(canvasSchemeNode)
		{
			var str:String=(TCHAR)canvasSchemeNode.Gettext();
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
		var pDom:XML = pXmlDOM;
		var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
		var oldSchemesNode:XML= canvasNode.selectSingleNode("ColorSchemes");
		var schemesNode:XML=pDom.createElement("ColorSchemes");

		if(!oldSchemesNode)
		{
			canvasNode.appendChild(schemesNode);
		}
		else
		{
			canvasNode.replaceChild(schemesNode,oldSchemesNode);
		}
		std::set<CString> names=GetColorSchemeNames();
		for(std::map<CString,CString>::const_iterator itr = xmlMap.begin(); itr != xmlMap.end(); ++itr )
		{
			var csName:String=itr.first;
			if(csName.IsEmpty())
				continue;//do nothing for default
			if(names.find(csName)==names.end())//don't save if it's not referenced
			{
				continue;
			}
			var csXML:String=itr.second;
			var pTempDom:XML;
			pTempDom.CreateInstance(__uuidof(DOMDocument40));
			/ VARIANT_BOOL loadSuccess = / pTempDom.loadXML(_bstr_t(csXML));
			var pRoot:XML=pTempDom.selectSingleNode("Root");
			var pClone:XML=pRoot.cloneNode(VARIANT_TRUE);
			var schemeNode:XML=pDom.createElement("Scheme");
			schemesNode.appendChild(schemeNode);
			schemeNode.setAttribute("name",_bstr_t(csName));
			schemeNode.appendChild(pClone);
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
		var pDom:XML = pXmlDOM;
		var canvasNode:XML = pDom.selectSingleNode("CXCanvas");
		var schemesNode:XML= canvasNode.selectSingleNode("ColorSchemes");
		if(!schemesNode)
		{
			return;
		}
		var list:XMLList=schemesNode.selectNodes("scheme/Root");
		var n:int = list.Getlength();
		for(var i:int=0;i<n;i++)
		{
			var pRootNode:XML= list.Getitem(i);
			var map:Dictionary = pRootNode.Getattributes();
			var atrb:String = map.getNamedItem("ID");
			var ID:String=atrb.Getvalue();
			colorSchemesXMLMap[ID]=(TCHAR)pRootNode.Getxml();
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
var cmpDocument:Boolean::ClearMetaData(var compID:String,var tag:String)
{
	try
	{
		var pDom:XML = pXmlDOM;

		//Get compnent node
		var compNode:XML;
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return false;
		}
		var metaNode:XML=compNode.selectSingleNode("metaData");
		if(!metaNode)
		{
			return false;
		}
		var tagList:XMLList = metaNode.selectNodes("tag");
		if( tagList )
		{
			var count:int = tagList.Getlength();
			for(var i:int=0; i<count; i++)
			{
				var node:XML = tagList.Getitem(i);
				if((TCHAR)node.Gettext()==tag)
				{
					metaNode.removeChild(node);
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
var cmpDocument:Boolean::SetMetaData(var compID:String,var tag:String, var data:String)
{
	try
	{
		var pDom:XML = pXmlDOM;

		//Get compnent node
		var compNode:XML;
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return false;
		}
		var metaNode:XML=compNode.selectSingleNode("metaData");
		if(!metaNode)
		{
			metaNode=pDom.createElement("metaData");
			compNode.appendChild(metaNode);
		}
		var tagNode:XML=metaNode.selectSingleNode("tag");
		if(!tagNode)
		{
			tagNode=pDom.createElement("tag");
			metaNode.appendChild(tagNode);
		}
		tagNode.Puttext(_bstr_t(tag));
		var dataNode:XML=metaNode.selectSingleNode("data");
		if(!dataNode)
		{
			dataNode=pDom.createElement("data");
			metaNode.appendChild(dataNode);
		}
		dataNode.Puttext(_bstr_t(data));
	/	removed as data is just a string
		var oldDataNode:XML=metaNode.selectSingleNode("data");
		var dataNode:XML;//=pDom.createElement("data");
		var pDOM2:XML;
		pDOM2.CreateInstance(__uuidof(DOMDocument40));
		pDOM2.PutpreserveWhiteSpace(VARIANT_FALSE);
		VARIANT_BOOL loadSuccess = pDOM2.loadXML(_bstr_t(data));
		var root:XML = pDOM2.documentElement;
		//Set data node to clone of passed in xml
		dataNode=root.cloneNode(VARIANT_TRUE);
		if(oldDataNode)
		{
			metaNode.replaceChild(dataNode,oldDataNode);
		}
		else
		{
			metaNode.appendChild(dataNode);
		}
	/
		return true;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}
void cmpDocument::SetComponentColorScheme(var compID:String,var csColorSchemeID:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		//Get compnent node
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return;
		}
		var colorSchemeNode:XML=compNode.selectSingleNode("colorScheme");
		if(!colorSchemeNode)
		{
			colorSchemeNode=pDom.createElement("colorScheme");
			compNode.appendChild(colorSchemeNode);
		}
		colorSchemeNode.Puttext(_bstr_t(csColorSchemeID));
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
void cmpDocument::SetCurrentSkin(var csSkin:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var skinNode:XML=canvasNode.selectSingleNode("colorTheme");
		if(!skinNode)
		{
			skinNode = pDom.createElement("colorTheme");
			canvasNode.appendChild(skinNode);
		}
		skinNode.text = _bstr_t(csSkin);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
var cmpDocument:String::GetCurrentSkin()
{
	try
	{
		var pDom:XML = pXmlDOM;
		var canvasNode:XML;
		var res:String;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var skinNode:XML=canvasNode.selectSingleNode("colorTheme");
		if(skinNode)
		{
			res=(TCHAR)(skinNode.text);
		}
		return res;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}
void cmpDocument::SetDisplayName(const var compID:String,const var displayName:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		//Get component node
		var compNode:XML;
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return;
		}
		var dispName:String = _bstr_t(displayName);
		compNode.setAttribute("displayName",displayName);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}

var cmpDocument:int::GetAssets(std::vector<std::wstring> &assets,var save:Boolean)
{
	try
	{
		//empty list
		assets.clear();

		var pDom:XML = pXmlDOM;

		var assetList:XMLList = pDom.selectNodes("//temporaryFile");
		if( assetList )
		{
			var count:int = assetList.Getlength();
			for(var i:int=0; i<count; i++)
			{
				BSTR b;
				var node:XML = assetList.Getitem(i);

				var parent:XML = node.GetparentNode();

				//don't archive if it's a URL
				if(parent)
				{
					var embed:XML = parent.selectSingleNode("./embed");
					if(embed)
					{
						if (_tcscmp(embed.Gettext(), _T("url")) == 0)
							continue; //don't copy this file
					}
				}

				node.get_text(&b);

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
		
		var pDom:XML = pXmlDOM;
		var acount:int=0;
		var assetList:XMLList = pDom.selectNodes("//originalFile");
		if( assetList )
		{
			var count:int = assetList.Getlength();
			for(var i:int=0; i<count; i++)
			{
				BSTR b;
				var node:XML = assetList.Getitem(i);
				var parent:XML = node.GetparentNode();

				//don't archive if it's a URL
				if(parent)
				{
					var embed:XML = parent.selectSingleNode("./embed");
					if(embed)
					{
						if (_tcscmp(embed.Gettext(), _T("url")) == 0)
							continue; //don't copy this file
					}
				}

				node.get_text(&b);

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
	var rv:int = 0;
	var pDom:XML = pXmlDOM;
	if (pDom) {
		var txt:String = (LPCTSTR)pDom.Getxml();
		var list:XMLList = pDom.selectNodes("//assets/asset/value[embed=\"false\"]");
		if (list) {
			rv = list.Getlength();
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

		var pDOM:XML = pXmlDOM;

		var nodeList:XMLList = pDOM.selectNodes("//temporaryFile");
		if( nodeList.Getlength() )
		{
			var tempFileNode:XML;
			BSTR b;
			var key:String;
			for(var i:int=0; i<nodeList.Getlength(); i++)
			{
				tempFileNode = nodeList.Getitem(i);
				tempFileNode.get_text(&b);

				key = b;
				if ( assetMap.find(key) != assetMap.end() ) 
				{
					tempFileNode.text = assetMap[key].c_str();

					//now find the property strings
					var propNodeList:XMLList = pDOM.selectNodes("//value[./string = '" + _bstr_t(key.c_str()) + "']");
					if( propNodeList )
					{
						var cnt:int = propNodeList.Getlength();
						for(var j:int=0; j<cnt; j++)
						{
							var propNode:XML = propNodeList.Getitem(j);
							var stringNode:XML = propNode.selectSingleNode("./string");
							stringNode.text = assetMap[key].c_str();
						}
					}
				}
			}
		}
		//now we need to reset properties of the components
		std::map<std::wstring,std::wstring>::iterator it = assetMap.begin();
		for (; it != assetMap.end(); ++it) 
		{
			var path:String = (it).second;
			var str1:String=(it).first.c_str();
			var str2:String=path.c_str();
			var request:String=_T("//property[./string = '")+str1+_T("']");
			var pathNodes:XMLList=pDOM.selectNodes(_bstr_t(request));
			var n:int=pathNodes.Getlength();
			for(var i:int=0;i<n;i++)
			{
				var pathNode:XML=pathNodes.Getitem(i);
				var stringNode:XML=pathNode.GetfirstChild();
				if(stringNode)
				{
					stringNode.Puttext(_bstr_t(str2));
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
var cmpDocument:String::GenerateName(var templateStr:String)
{
	var n:int=1;
	var tryStr:String;
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
var cmpDocument:Boolean::IsNameUsedAlready(var csName:String)
{
	try
	{
		var pDom:XML = pXmlDOM;
		var compList:XMLList = pDom.selectNodes("//component");
		if(compList)
		{
			var count:int = compList.Getlength();
			for(var i:int=0; i<count; i++)
			{
				var compNode:XML = compList.Getitem(i);
				var map:Dictionary = compNode.Getattributes();
				var atrb:String = map.getNamedItem("displayName");
				var bs:String=atrb.Getvalue();
				var displayName:String=(TCHAR)bs;
				if(displayName==csName)
					return true;
			}
		}
		compList = pDom.selectNodes("//connection");
		if(compList)
		{
			var count:int = compList.Getlength();
			for(var i:int=0; i<count; i++)
			{
				var compNode:XML = compList.Getitem(i);
				var map:Dictionary = compNode.Getattributes();
				var atrb:String = map.getNamedItem("displayName");
				var bs:String=atrb.Getvalue();
				var displayName:String=(TCHAR)bs;
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
var cmpDocument:String::GetParentGroup(var compID:String)
{
	var res:String=_T("");
	try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
			return res;
		var parentNode:XML=compNode.GetparentNode();
		if(!parentNode)
			return res;
		var map:Dictionary = parentNode.Getattributes();
		var atrb:String = map.getNamedItem("className");
		var bs:String=atrb.Getvalue();
		var className:String=(TCHAR)bs;
		if(className==_T("xcelsius.canvas.Group"))
		{
			atrb=map.getNamedItem("id");
			bs=atrb.Getvalue();
			res=(TCHAR)bs;
		}
		return res;
	}
	catch(...)
	{
		return _T("");
	}
}
var cmpDocument:Boolean::IsLastMemberOfGroup(var compID:String)
{
	var pDom:XML = pXmlDOM;
	try
	{
	var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
	if(!compNode)
		return false;

	var parentNode:XML=compNode.GetparentNode();
	if(!parentNode)
		return false;
	var map:Dictionary = parentNode.Getattributes();
	var atrb:String = map.getNamedItem("className");
	var bs:String=atrb.Getvalue();
	var className:String=(TCHAR)bs;
	if(className==_T("xcelsius.canvas.Group"))
	{
		var nodeList:XMLList=parentNode.selectNodes("component");
		if(nodeList.Getlength()==1)
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
std::map<CString,CString> cmpDocument::GetBindingInfo(var compID:String,var propName:String)
{
	std::map<CString,CString> res;
	try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
			return res;
		var request:String=_T("bindings/property/name[./string = '")+propName+_T("']");
		var nameNodes:XMLList=compNode.selectNodes(_bstr_t(request));
		if(!nameNodes)
			return res;
		var n:int=nameNodes.Getlength();
		for(var i:int=0;i<n;i++)
		{
			var bindingNode:XML=nameNodes.Getitem(i).GetparentNode().selectSingleNode("value/binding");
			if(!bindingNode)
				continue;
			var endpointNode:XML=bindingNode.selectSingleNode("endpoint");
			if(!endpointNode)
				continue;
			var map:Dictionary = endpointNode.Getattributes();
			var atrb:String = map.getNamedItem("id");
			var bindingID:String=(TCHAR)(_bstr_t)atrb.Getvalue();
			var chainNode:XML=endpointNode.selectSingleNode("chain");
			if(!chainNode)
				continue;
			var chainXML:String=(TCHAR)chainNode.Getxml();
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
		var pDom:XML = pXmlDOM;

		//Get CXCanvas node
		var canvasNode:XML;
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var nodelist:XMLList = canvasNode.selectNodes("component");
		var n:int = nodelist.Getlength();
		for(var i:int=0; i<n; i++ )
		{
			var compNode:XML = nodelist.Getitem(i);
			canvasNode.removeChild(compNode);
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
	var pDom:XML = pXmlDOM;
	result.clear();
	for(UINT i=0;i<list.size();i++)
	{
		if(IsGroup(list[i]))
		{
			var compNode:XML = pDom.selectSingleNode("//component[@id='" + list[i] + "']");
			var nodelist:XMLList=compNode.selectNodes("component");
			for(var j:int=0;j<nodelist.Getlength();j++)
			{
				var childNode:XML=nodelist.Getitem(j);
				var childmap:Dictionary = childNode.Getattributes();
				var atrbID:String = childmap.getNamedItem("id");
				var childID:String=atrbID.Getvalue();
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
var cmpDocument:Boolean::IsGroup(var compID:String)
{
	var pDom:XML = pXmlDOM;
	try
	{
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + compID + "']");
		if(!compNode)
			return false;
		var map:Dictionary = compNode.Getattributes();
		var atrb:String = map.getNamedItem("className");
		if(!atrb)
			return false;
		var classNameString:String=atrb.Getvalue();
		var csClassName:String=(TCHAR)classNameString;
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
var cmpDocument:String::GetAssets(var compID:String,var propName:String)
{
	var res:String=_T("");
	var pDom:XML = pXmlDOM;
	try
	{
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
			return res;
		var assetsNode:XML=compNode.selectSingleNode("assets");
		if(!assetsNode)
			return res;
		var request:String=_T("asset/name[./string = '")+propName+_T("']");
		var nameNode:XML=assetsNode.selectSingleNode(_bstr_t(request));
		if(!nameNode)
			return res;
		var valueNode:XML=nameNode.GetparentNode().selectSingleNode("value");
		if(!valueNode)
			return res;
		var list:XMLList=valueNode.selectNodes("array/asset");
		var arrArg:CArrayArgument=new CArrayArgument();
		var n:int=list.Getlength();
		for(var i:int=0;i<n;i++)
		{
			var aNode:XML = list.Getitem(i);
			//assets can be an 1D array or 2D array
			var subArrNode:XML=aNode.selectSingleNode("array");
			if(subArrNode)
			{
				//2D array
				var arrSubArg:CArrayArgument=new CArrayArgument();
				var subList:XMLList = subArrNode.selectNodes("asset");
				var m:int=subList.Getlength();
				for(var j:int=0; j<m; j++)
				{
					var aSubNode:XML = subList.Getitem(j);
					arrSubArg.add(buildAssetsItem(aSubNode));
				}
				arrArg.add(arrSubArg);
			}
			//1D array
			else
			{
				arrArg.add(buildAssetsItem(aNode));
			}
		}
		arrArg.saveString(&res);
		delete arrArg;
		return res;

	}
	catch(...)
	{
		return res;
	}
}

var cmpDocument:CObjectArgument::buildAssetsItem(var item:XML)
{
	var objArg:CObjectArgument=new CObjectArgument();
	var tempFileNode:XML=item.selectSingleNode("temporaryFile");
	var csTempFile:String=(TCHAR)tempFileNode.Gettext();
	objArg.set("temporaryFile",csTempFile);
	var origFileNode:XML=item.selectSingleNode("originalFile");
	var csOrigFile:String=(TCHAR)origFileNode.Gettext();
	objArg.set("originalFile",csOrigFile);
	var embedNode:XML=item.selectSingleNode("embed");
	var csEmbed:String=(TCHAR)embedNode.Gettext();
	var bEmbedded:Boolean=false;
	if(csEmbed==_T("embed"))
		bEmbedded=true;
	objArg.set("embed",bEmbedded);
	return objArg;
}
var cmpDocument:int::RemoveConnectionsOfClass(var className:String)
{
	var nodelist:XMLList;
	var pDom:XML = pXmlDOM;
	nodelist = pDom.selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
	var n:int = nodelist.Getlength();
	for(var i:int=0;i<n;i++)
	{
		var connNode:XML=nodelist.Getitem(i);
		var parent:XML=connNode.GetparentNode();
		parent.removeChild(connNode);
	}
	return n;
}
var cmpDocument:Boolean::HasConnectionsOfClass(var className:String)
{
	var nodelist:XMLList;
	var pDom:XML = pXmlDOM;
	nodelist = pDom.selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
	var n:int = nodelist.Getlength();
	return (n!=0);
}
var cmpDocument:String::GetNote()
{
	var res:String;
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("/CXCanvas");
	var noteNode:XML = canvasNode.selectSingleNode("Notes");
	if(noteNode)
	{
		MSXML2::IXMLDOMCDATASectionPtr cData=noteNode.GetfirstChild();
		res=(TCHAR)cData.data;
	}
	return res;

}
void cmpDocument::SetNote(var csNote:String)
{
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("/CXCanvas");
	var noteNode:XML = canvasNode.selectSingleNode("Notes");
	if(!noteNode)
	{
		noteNode=pDom.createElement("Notes");
		canvasNode.appendChild(noteNode);
		MSXML2::IXMLDOMCDATASectionPtr cData=pDom.createCDATASection(_bstr_t(csNote));
		noteNode.appendChild(cData);
		return;
	}
	else
	{
		MSXML2::IXMLDOMCDATASectionPtr cData=noteNode.GetfirstChild();
		cData.data=_bstr_t(csNote);
	}
}

var cmpDocument:Array::GetLiveOfficeCuids()
{
	var cuids:Array;

	//get crystalPersistXML
	//get the value
	//parse the persistxml
	//grab the cuids
	//put it into the array and return it
	var pDom:XML = pXmlDOM;
	var canvasNode:XML = pDom.selectSingleNode("/CXCanvas");

	var persistXMLNode:XML = canvasNode.selectSingleNode("//properties/property/name[./string='crystalPersistXML']/following-sibling::");
	if(persistXMLNode)
	{
		var oldValue:XML = persistXMLNode.GetfirstChild();
		var persistXML:String = oldValue.Gettext();

		//we need to parse "cuids" out of b.
		var cuidDOM:XML;
		cuidDOM.CreateInstance(__uuidof(DOMDocument40));
		
		VARIANT_BOOL loadSuccess = cuidDOM.loadXML(persistXML);

		if(loadSuccess)
		{
			var reportList:XMLList = cuidDOM.selectNodes("//Report");
			if(reportList)
			{
				var size:int = reportList.Getlength();
				for(var i:int=0; i<size; i++)
				{
					var report:XML = reportList.Getitem(i);
					_variant_t v = report.getAttribute("cuid");

					//we could check for duplicates here
					cuids.push_back(v.bstrVal);
				}
			}
		}
	}
	return cuids;
}
void cmpDocument::ChangeComponentLevel(var compID:String, var newLevel:int)
{
try
	{
		var pDom:XML = pXmlDOM;
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			return;
		}
		var parent:XML=compNode.GetparentNode();
		if(!parent)
			return;
		var clone:XML=compNode.cloneNode(VARIANT_TRUE);
		parent.removeChild(compNode);

		var nextSibling:XML=parent.GetfirstChild();
		if(newLevel==0)
		{
			if(nextSibling)
			{
				parent.insertBefore(clone,nextSibling.GetInterfacePtr());
			}
			else
			{
				parent.appendChild(clone);

			}
			return;
		}
		while(nextSibling && (nextSibling.GetnodeName())!=_bstr_t("component"))
		{
			nextSibling=nextSibling.GetnextSibling();
		}
		for(var i:int=0;(i<newLevel) && nextSibling;i++)
		{
			nextSibling=nextSibling.GetnextSibling();
			while(nextSibling && (nextSibling.GetnodeName())!=_bstr_t("component"))
			{
				nextSibling=nextSibling.GetnextSibling();
			}
		}
		if(nextSibling)
		{
			parent.insertBefore(clone,nextSibling.GetInterfacePtr());
		}
		else
		{
			parent.appendChild(clone);

		}
	
		return;
	}
	catch(...)
	{
		assert(false);
		return ;
	}
}
void cmpDocument::SetWarningTitle(var title:String)
{
	m_csWarningTitle=title;
}
var cmpDocument:Array::UnbindProperty(var compIds:Array,var propName:String)
{
	//returns list of binding ids to unbind
	var bindingIDs:Array;
	var pDom:XML = pXmlDOM;
	var n:int=compIds.size();
	for(var i:int=1;i<n;i++)
	{
		var compNode:XML = pDom.selectSingleNode("//component[@id='" + _bstr_t(compIds[i]) + "']");		
		var nameNodes:XMLList=compNode.selectNodes("bindings/property/name[./string='"+_bstr_t(propName)+"']");
		for(var j:int=0;j<nameNodes.Getlength();j++)
		{
			var nextNameNode:XML=nameNodes.Getitem(j);
			var endpointNode:XML=nextNameNode.selectSingleNode("../value/binding/endpoint");
			if(endpointNode)
			{
				var map:Dictionary=endpointNode.Getattributes();
				var atrb:String = map.getNamedItem("id");
				if(atrb!=0)
				{
					var ID:String=atrb.Getvalue();
					bindingIDs.push_back(ID);
					var delNode:XML=nextNameNode.GetparentNode();
					delNode.GetparentNode().removeChild(delNode);
				}
			}

		}
	}
	return bindingIDs;
}
var cmpDocument:String::GetConnectionsROL()
{
	var res:String;
	var arrArg:CArrayArgument;
	var valueXML:String;
	var pDom:XML = pXmlDOM;
	var nodelist:XMLList = pDom.selectNodes("//connection");
	var n:int=nodelist.Getlength();
	for(var i:int=0;i<n;i++)
	{
		var pObj:CObjectArgument=new CObjectArgument();
		var connNode:XML=nodelist.Getitem(i);
		var map:Dictionary = connNode.Getattributes();
		var atrb:String = map.getNamedItem("id");
		if(!atrb)
			continue;
		var id:String=(TCHAR)(_bstr_t)atrb.Getvalue();
		var propertiesNode:XML = connNode.selectSingleNode("properties");
		if(propertiesNode == NULL)
		{
			valueXML=_T("<null/>");
		}
		else
		{
			var propNode:XML=NULL;
			propNode = propertiesNode.selectSingleNode("./property//name[./string = 'refreshOnLoad']/following-sibling::");
			if(propNode == NULL)
			{
				valueXML=_T("<null/>");
			}		
			if(propNode != NULL)
			{
				var oldValue:XML = propNode.GetfirstChild();
				if ( oldValue != NULL )
				{
					valueXML=(LPCTSTR)(oldValue.Getxml());
				}
			}
		}
		var bs:String=valueXML;
		var pArgValue:CArgument = CArgument::Load(bs);
		pObj.set(_T("ID"),id);
		pObj.set(_T("ROL"),pArgValue);
		arrArg.add(pObj);
	}
	arrArg.saveString(&res);
	return res;
}

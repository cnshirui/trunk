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

#pragma warning (disable:4800)	// forcing a var to:String

cmpDocument::cmpDocument()
{
	//Create a xml document
	var pDOM:XML
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

	var pDom:XML

	uget uid;
	DocGUID = uid.getUUID(); 

	//"<?xml version="1.0" encoding="utf-8"?><CXCanvas></CXCanvas>
	//VARIANT_BOOL loadSuccess = pDom.loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas><HSlider id=\"growthRate\"><x>21</x><y>207</y><minimum>0</minimum><maximum>1</maximum><liveDragging>true</liveDragging><themeColor>haloOrange</themeColor><snapInterval>.01</snapInterval></HSlider></CXCanvas>");
	pDom.PutpreserveWhiteSpace(VARIANT_FALSE);

	//add docxml as attribute
	var docxml:String
	docxml += DocGUID.c_str();
	docxml += _T("\"></CXCanvas>");

	//Add default font

//	/ VARIANT_BOOL loadSuccess = / pDom.loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas></CXCanvas>");
	/ VARIANT_BOOL loadSuccess = / pDom.loadXML(docxml.c_str());

	SetExportSettings(true, _T(""));

}


var cmpDocument:Boolean
{
	try
	{
		var pDom:XML

		var fontNode:XML

		if(fontNode!=NULL)
		{
			var compParent:XML
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


var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get CXCanvas node
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");

		// Check and remove if font node already exists
		RemoveFont();
		
		//Create font node
		var compNode:XML

		compNode.setAttribute("fontName",fontName);

		canvasNode.appendChild(compNode);

		var setChild:XML
		var selChild:XML
		var emChild:XML

		var b_embed:int

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

var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		cmpFontInfo cmpFont=cmpFontInfo::GetInstance();

		/const var ff:String

		//Get node list
		var nodelist:XMLList

		var fontNode:XML

		if(fontNode==NULL)
			return false;

		var map:Dictionary
		var b:String
		var atrb:String
		var c:String
		cmpFontInfo::GetInstance().SetFontName((TCHAR)(LPCTSTR)c);//imutil::SetFont( (TCHAR)(LPCTSTR)c);

		//embed or not

		var selNode:XML
		var selText:String
		
		var embed:int
		delete [] selText;

		if(embed)
		{
			//Get font data
			
			var selNode:XML
			var selText:String
			
			cmpFont.SetCharSel((uint)atoi(selText));

			delete [] selText;

			var setNode:XML
			var setText:String
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

var cmpDocument:Boolean
{
	try
	{

		//Set doc pointer;
		var pDom:XML

		//Get CXCanvas node
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");

		//Create component node
		var compNode:XML

		//Set id attribute
		compNode.setAttribute("id",instanceID);

		//set className attribute
		compNode.setAttribute("className",className);

		//Figure out display name count based on number of components
		var nodelist:XMLList
		nodelist = pDom.selectNodes("//connection");
		var len:int
		if(nodelist != NULL){
			len = nodelist.length;
			len++;
		}
		var dispName:String

		//set displayName attribute
		compNode.setAttribute("displayName",dispName);

		//Append compnent node to cxcanvas
		canvasNode.appendChild(compNode);

		//Get default properties string from uiComp
		//Get the var pointer:uiLibrary
		var uiLib:uiLibrary
		var uicomp:uiLibraryComponent

		//Did we get a uiComp?
		if(uicomp != NULL){
			//Copy default xml from uiComponent to componentXML string
			var tmp:String

			//Load default props into a dom
			//Create a xml document
			var pDOM2:XML
			pDOM2.CreateInstance(__uuidof(DOMDocument40));
			pDOM2.PutpreserveWhiteSpace(VARIANT_FALSE);
			/VARIANT_BOOL loadSuccess = / pDOM2.loadXML(tmp);

			//Get properties node
			var uiCompProps:XML
			uiCompProps = pDOM2.selectSingleNode("//component[@className='" + _bstr_t(className) + "']/properties");

			//Are there properties to clone?
			if(uiCompProps != NULL){
				//clone the uiComp properties node
				var clone:XML

				//Add properties node to new comp
				compNode.appendChild(clone);
			}
		}
		//Append properties node to component node?
		var propertiesNode:XML
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

var cmpDocument:Boolean
{
	try
	{

		//Set doc pointer;
		var pDom:XML

		//Get CXCanvas node
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		if(!m_undoMap[instanceID].IsEmpty())
		{
			var xmlStr:String
			var pDom:XML
			pDom.CreateInstance(__uuidof(DOMDocument40));
			VARIANT_BOOL loadSuccess = pDom.loadXML(_bstr_t(xmlStr));
			if(loadSuccess)
			{
				var pCompNode:XML
				canvasNode.appendChild(pCompNode.cloneNode(VARIANT_TRUE));
				return true;
			}

		}

		//Create component node
		var compNode:XML

		//Set id attribute
		compNode.setAttribute("id",instanceID);

		//set className attribute
		compNode.setAttribute("className",className);

		//set className attribute
		compNode.setAttribute("styleName",styleName);

		//Figure out display name count based on number of components
		var nodelist:XMLList
		nodelist = pDom.selectNodes("//component[@className='" + _bstr_t(className) + "']");
		var len:int
		if(nodelist != NULL){
			len = nodelist.length;
			len++;
		}
		var dispName:String

		//set displayName attribute
		compNode.setAttribute("displayName",dispName);

		//Append compnent node to cxcanvas
		canvasNode.appendChild(compNode);
		var colorSchemeNode:XML
		colorSchemeNode.text = colorSchemeName;
		compNode.appendChild(colorSchemeNode);

		//Get default properties string from uiComp
		//Get the var pointer:uiLibrary
		var uiLib:uiLibrary
		var uicomp:uiLibraryComponent

		//Did we get a uiComp?
		if(uicomp != NULL){
			//Copy default xml from uiComponent to componentXML string
			var tmp:String

			//Load default props into a dom
			//Create a xml document
			var pDOM2:XML
			pDOM2.CreateInstance(__uuidof(DOMDocument40));
			pDOM2.PutpreserveWhiteSpace(VARIANT_FALSE);
			/VARIANT_BOOL loadSuccess = / pDOM2.loadXML(tmp);

			//Get properties node
			var uiCompProps:XML
			uiCompProps = pDOM2.selectSingleNode("//component[@className='" + _bstr_t(className) + "']/properties");

			//Are there properties to clone?
			if(uiCompProps != NULL){
				//clone the uiComp properties node
				var clone:XML

				//Add properties node to new comp
				compNode.appendChild(clone);
			}
		}

		//Append properties node to component node?
		var propertiesNode:XML
		propertiesNode = compNode.selectSingleNode("properties");
		if (propertiesNode == NULL)
		{
			propertiesNode = pDom.createElement("properties");
			compNode.appendChild(propertiesNode);
		}

	/	//Add displayName node to properties
		var propertyNode:XML
		var valNode:XML
		var valChild:XML
		var nameNode:XML
		var nameChild:XML
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


var cmpDocument:Boolean
{
	try
	{

		//Returns pointer to List object that has cmpComponents
		//return Comps[instanceID];
		//Set doc pointer;
		var pDom:XML

		//Get compnent node
		var compNode:XML
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
			var bindingsNode:XML
			bindingsNode = compNode.selectSingleNode("./bindings");
			if(bindingsNode != NULL){
				comp.SetBindingXML(bindingsNode.Getxml());
			}

			//Set asset xml
			var assetsNode:XML
			assetsNode = compNode.selectSingleNode("./assets");
			if(assetsNode != NULL){
				comp.SetAssetXML(assetsNode.Getxml());
			}

			//Set metadata xml
			var metadataNode:XML
			metadataNode = compNode.selectSingleNode("./metaData");
			if(metadataNode != NULL){
				comp.SetMetadataXML(metadataNode.Getxml());
			}

			//Set instanceID
			comp.SetInstanceID(instanceID);

			//Get className attribute and set property
			var map:Dictionary
			var b:String
			var atrb:String
			var classNameString:String
			comp.SetClassName(classNameString);

			//Get styleName attribute and set property
			var b2:String
			atrb = map.getNamedItem(b2);
			if(atrb != NULL){
				var styleNameString:String
				comp.SetStyleName(styleNameString);
			}

			//Get displayName attribute and set property
			var c:String
			atrb = map.getNamedItem(c);
			if(atrb != NULL){
				var displayNameString:String
				comp.DisplayName = (displayNameString);
			}

			//Get the var pointer:uiLibrary
			var uiLib:uiLibrary
			var uicomp:uiLibraryComponent
			//Set the uiComponent pointer
			comp.UIComp = uicomp;

			//Set component properties
			var propNode:XML
			var valNode:XML
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
			var nodelist:XMLList
			nodelist = compNode.selectNodes("./properties/property");

			if(nodelist!=NULL)
			{
				var propCount:int
				for( var i:int
				{
					propNode = nodelist.Getitem(i);

					var nameXML:String
					var valueXML:String

					var nameNode:XML
					var valueNode:XML

					nameNode.get_text( nameXML.GetAddress() );
					valueNode.get_xml( valueXML.GetAddress() );

					(props)[ nameXML.GetBSTR() ] = valueXML.GetBSTR();
				}
			}

			//bindings array
			var bindings:Array
			bindings.clear();
			nodelist = compNode.selectNodes("./bindings/property");

			if(nodelist!=NULL)
			{
				var propCount:int
				for( var i:int
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
				var propCount:int
				for( var i:int
				{
					propNode = nodelist.Getitem(i);

					var nameNode:XML
					var valueNode:XML

					var nameXML:String
					var valueXML:String

					(styles)[ nameXML.c_str() ] = valueXML.c_str();
				}
			}

			// sub components
			var comps:Array
			comps.clear();
			nodelist = compNode.selectNodes("./component");
			if(nodelist!=NULL)
			{
				var compCount:int
				for( var i:int
				{
					compNode = nodelist.Getitem(i);

					//Get instanceID attribute
					var map:Dictionary
					var b:String
					var atrb:String
					var c:String
					var instanceID:String

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

var cmpDocument:int
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		/const var compXML:String
		//Get node list
		var nodelist:XMLList
		nodelist = pDom.selectNodes("/CXCanvas/component");
		//Set return val
		var ret:int
		//Return
		return ret;
	}
	catch(...)
	{
		assert(false);
		return 0;
	}
}
var cmpDocument:int
{
	try
	{
		//Set doc pointer;
		var pDom:XML
		//Get node list
		var nodelist:XMLList
		nodelist = pDom.selectNodes("/CXCanvas/connection");
		//Set return val
		var ret:int
		//Return
		return ret;
	}
	catch(...)
	{
		assert(false);
		return 0;
	}
}
var cmpDocument:String
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get node list
		var nodelist:XMLList
		nodelist = pDom.selectNodes("/CXCanvas/component");

		//Get component node based on idx
		var compNode:XML

		//REMOVE
		var zzz:String

		//Get instanceID attribute
		var map:Dictionary
		var b:String
		var atrb:String
		var c:String
		var instanceID:String

		//Return
		return instanceID;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}
var cmpDocument:String
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get node list
		var nodelist:XMLList
		nodelist = pDom.selectNodes("/CXCanvas/connection");

		//Get component node based on idx
		var compNode:XML

		//Get instanceID attribute
		var map:Dictionary
		var b:String
		var atrb:String
		var c:String
		var instanceID:String

		//Return
		return instanceID;
	}
	catch(...)
	{
		assert(false);
		return _T("");
	}
}

var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get properties node
		var propertiesNode:XML
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
			var compNode:XML
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(comp.GetInstanceID()) + "']");
			if(!compNode)
			{
				compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(comp.GetInstanceID()) + "']");
			}
			//Set displayName attribute
			var map:Dictionary
			var b:String
			var atrb:String
			atrb.text = comp.DisplayName.c_str();

			//Set component properties
			var propNode:XML
			//New nodes for new xpath
			var propertyNode:XML
			var valNode:XML
			var valChild:XML
			var nameNode:XML
			var nameChild:XML
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

var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get properties node
		var compNode:XML
		var oldPropertiesNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("properties");

			var pValueDom:XML
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(compXML);

			var newValueOrigNode:XML
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


var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get properties node
		var compNode:XML
		var oldPropertiesNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}

		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("bindings");

			var pValueDom:XML
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(bindXML);

			var newValueOrigNode:XML

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

var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get properties node
		var compNode:XML
		var oldPropertiesNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("styles");

			var pValueDom:XML
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(styleXML);

			var newValueOrigNode:XML

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

var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get properties node
		var compNode:XML
		var oldPropertiesNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("assets");

			var pValueDom:XML
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(assetXML);

			var newValueOrigNode:XML

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

var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get properties node
		var compNode:XML
		var oldPropertiesNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
		}
		if(compNode)
		{
			oldPropertiesNode = compNode.selectSingleNode("Persist");

			var pValueDom:XML
			pValueDom.CreateInstance(__uuidof(DOMDocument40));
			/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(persistXML);

			var newValueOrigNode:XML

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


var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		HRESULT hr = pDom.save( fname );
		return (hr==S_OK)? true: false;
	}
	catch(...)
	{
		assert(false);
		return false;
	}
}

var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		VARIANT_BOOL varBool;
		varBool = pDom.load( fname);

		if (varBool) 
		{
			pXmlDOM = pDom;

			var nptr:XML
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
var cmpDocument:Boolean
{ 
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get compnent node
		var compNode:XML
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
			var csXML:String
			m_undoMap[instanceID]=csXML;
			//Set pointer to parent of comp node
			var compParent:XML

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
var cmpDocument:Boolean
{
	var pDom:XML
	var compNode:XML
	if(!compNode)
		return false;
	compNode.setAttribute("visible",(BOOL)bShow);
	return true;
}
var cmpDocument:Boolean
{
	var pDom:XML
	var compNode:XML
	if(!compNode)
		return false;
	compNode.setAttribute("locked",(BOOL)bLock);
	return true;
}
var cmpDocument:Boolean
{
	var pDom:XML
	var canvasNode:XML
	var nodelist:XMLList
	var n:int
	for(var i:int
	{
		var compNode:XML
		compNode.setAttribute("visible",(BOOL)bShow);
	}
	return true;
}
var cmpDocument:Boolean
{
	var pDom:XML
	var canvasNode:XML
	var nodelist:XMLList
	var n:int
	for(var i:int
	{
		var compNode:XML
		compNode.setAttribute("locked",(BOOL)bLock);
	}
	return true;
}
var cmpDocument:Boolean
{
	var pDom:XML
	var compNode:XML
	if(!compNode)
		return false;
	var map:Dictionary
	var atrb:String
	if(!atrb)
		return false;
	var bs:String
	var str:String
	var val:int
	return (val!=0);

}
var cmpDocument:Boolean
{
	var pDom:XML
	var compNode:XML
	if(!compNode)
		return true;
	var map:Dictionary
	var atrb:String
	if(!atrb)
		return true;
	var bs:String
	var str:String
	var val:int
	return (val!=0);
}
var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get component node
		var compNode:XML
		var propertiesNode:XML
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
			var propNode:XML
			//New nodes for new xpath
			var propertyNode:XML
			var valNode:XML
			var valChild:XML
			var nameNode:XML
			var nameChild:XML

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
				var pValueDom:XML
				pValueDom.CreateInstance(__uuidof(DOMDocument40));
				/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(valueXML);

				var newValueOrigNode:XML
				var newValue:XML
			
				var oldValue:XML
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML

		//Get component node
		var compNode:XML
		var propertiesNode:XML
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
			var propNode:XML
			propNode = propertiesNode.selectSingleNode("./property//name[./string = '" + _bstr_t(propName) + "']/following-sibling::");
			if(propNode == NULL)
			{
				return false;
			}		
			if(propNode != NULL)
			{
				var oldValue:XML
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

var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		var root:XML
		var canvasNode:XML
		if(canvasNode==NULL)
		{
			canvasNode = pDom.createElement("canvas");
			root.appendChild(canvasNode);
		}
		var stylesNode:XML
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

var cmpDocument:Boolean
{
	try
	{
		//Set doc pointer;
		var pDom:XML

		//Get component node
		var compNode:XML
		var stylesNode:XML
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
			var canvasNode:XML
			var globalStylesNode:XML
			if(globalStylesNode==NULL)
			{
				CreateCSSStyleDeclaration(compID);
				globalStylesNode= canvasNode.selectSingleNode("Styles");
			}
			stylesNode = globalStylesNode.selectSingleNode("//style[@id='" + _bstr_t(compID) + "']");
			if(!stylesNode)
			{
				stylesNode = pDom.createElement("style");
				var bs:String
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
			var propNode:XML
			//New nodes for new xpath
			var propertyNode:XML
			var valNode:XML
			var valChild:XML
			var nameNode:XML
			var nameChild:XML

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
				var pValueDom:XML
				pValueDom.CreateInstance(__uuidof(DOMDocument40));
				/VARIANT_BOOL loadSuccess = / pValueDom.loadXML(valueXML);

				var newValueOrigNode:XML
				var newValue:XML
			
				var oldValue:XML
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

var cmpDocument:Boolean
{
	assert(stylesNode != NULL);
	var pDom:XML
	var valueNode:XML
	
	try
	{
		if(valueNode == NULL)
		{
			var styleNode:XML
			stylesNode.appendChild(styleNode);
			var nameNode:XML
			var nameChild:XML
			nameChild.text = styleName;
			nameNode.appendChild(nameChild);
			styleNode.appendChild(nameNode);
			valueNode = pDom.createElement("value");
			styleNode.appendChild(valueNode);
		}
		
		//create the 'value' node
		var pValueDom:XML
		pValueDom.CreateInstance(__uuidof(DOMDocument40));
		pValueDom.loadXML(valueXML);

		var newValueOrigNode:XML
		var newValue:XML
		
		var oldValue:XML
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		var canvasNode:XML
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
var cmpDocument:Boolean
{
	var names:String
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
	for(var i:int
	{
		if(styleName.Find(names[i])!=-1)
		{
			return true;
		}
	}
	return false;
}
var cmpDocument:Boolean
{
	var n:int
	var str:String
	if(n!=-1)
	{
		str=styleName.Right(styleName.GetLength()-n);
	}
	str.MakeLower();
	var nColor:int
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
		var pDom:XML
		var compNodes:XMLList
		var n:int
		for(var i:int
		{
			var compNode:XML
			var map:Dictionary
			var atrb:String
			var compID:String
			var stylesNode:XML
			if(stylesNode)
			{
				var styleNames:XMLList
				var nStyles:int
				for(var j:int
				{
					var styleNameNode:XML
					var t:String
					var styleName:String
					var valueNode:XML
					var valueXML:String
					//Justin Cox confirmed that we clear only color styles
					if(IsColorStyle(styleName))
					{
						var styleNode:XML
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML

		//Get component node
		var compNode:XML
		var stylesNode:XML
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


var cmpDocument:Boolean
{
	try
	{
		var n:int
		for(var i:int
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
var cmpDocument:Boolean
{
	try
	{
		var n:int
		for(var i:int
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
var cmpDocument:Boolean
{
	try
	{
		//Need to move the component from the current location to be a child of the parent comp
		//for now we are going to tack it on to the end and make it the last child component

		//Set doc pointer;
		var pDom:XML

		//Get compnent node
		var compNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		//Get parent node
		var parentNode:XML
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
			var clone:XML

			//Add the cloned node to the parent
			parentNode.appendChild(clone);

			//Set pointer to parent of comp node
			var compParent:XML

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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		var compNode:XML
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		var parentNode:XML
		
		var clone:XML
		var prevNode:XML
		var nodeType:String
		var siblingNodeType:String
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		var compNode:XML
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		var clone:XML

		var parentNode:XML
		var nextNode:XML
		if(!nextNode)
			return true;
		var nodeType:String
		var siblingNodeType:String
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
		var insertBeforeNode:XML
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
var cmpDocument:Boolean
{
	try
	{
		if(nLevelsUp==0)
			return true;
		var pDom:XML
		var compNode:XML
		if(!compNode)
		{
			return false;
		}
		var clone:XML
		var parent:XML
		if(!parent)
			return false;

		var beforeNode:XML
		if(nLevelsUp>0)
		{
			for(var i:int
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
			var nLev:int
			for(var i:int
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		var compNode:XML
		if(!compNode)
		{
			return false;
		}

		var parentNode:XML
		var nextNode:XML
		if(!nextNode)
		{
			return true;
		}
		var clone:XML
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		var compNode:XML
		if(!compNode)
		{
			return false;
		}
		var prevNode:XML
		if(!prevNode)
		{
			return true;//it's already first
		}

		var parentNode:XML
		
		var clone:XML
		var firstSiblingNode:XML
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
var cmpDocument:String
{
	try
	{
		//Load transport xml into a dom
		//Create a xml document
		var pDom:XML
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		//Get instance id
		var nodelist:XMLList
		nodelist = pDom.selectNodes("//component");

		//Get component node based on idx
		var compNode:XML

		//REMOVE
		var zzz:String

		//Get instanceID attribute
		var map:Dictionary
		var b:String
		var atrb:String
		var instanceID:String

		//Build component node
		var temp:String

		//Loop through all transport xml props and copy them to compXML string
		nodelist = pDom.selectNodes("//property");

		//Did we find properties?
		if(nodelist != NULL){
			//Loop
			var numProps:int
			var i:int
			for(i=0;i<numProps;i++){
				//Get property node
				var propNode:XML

				//REMOVE
				var aaa:String

				//Get name node
				var nameNode:XML
				var valueNode:XML

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

var cmpDocument:Boolean
{
	try
	{

		var pDom:XML
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		//Get type node
		var pNode:XML
		var pNodeList:XMLList
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
		var map:Dictionary
		var b:String
		var atrb:String
		var instanceID:String
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

		var pEndpoint:XML
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
				   <endpovar type:int
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

var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		// get the binding id
		var pNode:XML
		pNode = pDom.selectSingleNode("/binding/endpoint/@id");
		var bindingId:String
		//binding.bindingID = bindingId;
		binding.bindingID = _T("<string>") + bindingId + _T("</string>");
		pNode = pNode.selectSingleNode("../cells");
		//var clone:XML

		var pDom2:XML
			// find the node
		var endpointPath:String
		endpointPath += (LPCTSTR)bindingId;
		endpointPath += "']";
		var endpointNode:XML
		var cellsNode:XML
		var displayNode:XML
		var rangeNode:XML
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
			var bindingNode:XML
			var valueNode:XML
			var propertyNode:XML
			var bindingsNode:XML
			bindingsNode.removeChild(propertyNode);
			return false;
		}		

		var guidNode:XML
		var guidId:String
		binding.instanceID = _T("<string>") + guidId + _T("</string>");

		var propNode:XML
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
var cmpDocument:Boolean
														 const var bindingId:String
														 CString& valueXML
														)
{
	try
	{
		var pDom:XML

		//Get component node
		var compNode:XML
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
		var bindingsNode:XML
		bindingsNode = compNode.selectSingleNode("./bindings");

		if (bindingsNode) {
			// find the node
			var propPath:String
			propPath += bindingId;
			propPath += "']";

			var propNode:XML
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

var cmpDocument:Boolean
{
	try
	{

		var pDom:XML
		pDom.CreateInstance(__uuidof(DOMDocument40));
		/ VARIANT_BOOL loadSuccess = / pDom.loadXML(transXML);

		//Get component id from trans xml
		//Get component node
		var cNode:XML
		cNode = pDom.selectSingleNode("//component");
		//Get id attribute
		var map:Dictionary
		var b:String
		var atrb:String
		var instanceID:String

		//Select property node in trans xml
		var pNode:XML
		pNode = cNode.selectSingleNode("./onrequestbindings//property");

		var aaa:String

		//Get the property name
		var propNameNode:XML
		propNameNode = pNode.selectSingleNode("./name//string");
		var propName:String

		// get the binding id
		var bindingIdNode:XML
		bindingIdNode = pNode.selectSingleNode("./value/binding/endpoint/@id");
		var bindingId:String

		//clone the property node in trans xml
		var clone:XML

		//Select the component node in document xml
		var pDom2:XML

		//Get compnent node
		var compNode:XML
		compNode = pDom2.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(!compNode)
		compNode = pDom2.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");

		//Select bindings node
		var bindingsNode:XML
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
			var propPath:String
			propPath += (LPCTSTR)propName;
			propPath += "' and value/binding/endpoint/@id='";
			propPath += (LPCTSTR)bindingId;
			propPath += "']";

			var propNode:XML
			propNode = bindingsNode.selectSingleNode(_bstr_t(propPath));
			if (propNode)
				propNode.parentNode.removeChild(propNode);
		}
		else
		{
			//Add cloned property node to bindings node if necessary
			var propPath:String
			propPath += (LPCTSTR)bindingId;
			propPath += "']";

			var oldBinding:XML

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

var cmpDocument:Array
{
	var bindingIDs:Array

	try
	{
		//Select the component node in document xml
		var pDom2:XML
		var guid:String
		var bMultipleComps:Boolean
		//Get compnent node
		var compNode:XML
		compNode = pDom2.selectSingleNode("//component[@id='" + guid + "']");
		if(!compNode)
		{
			compNode = pDom2.selectSingleNode("//connection[@id='" + guid + "']");
		}

		//Select bindings node
		var bindingsNode:XML
		bindingsNode = compNode.selectSingleNode("./bindings");

		if(bindingsNode == NULL){
			return bindingIDs;
		}

		// find the node
		var propPath:String
		propPath += (LPCTSTR)id;
		propPath += "']";

		var propNode:XML
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
			var nameNode:XML
			var seriesIndex:int
			var name:String
			var chainNode:XML
			if(chainNode)
			{
				
				var propIndexNode:XML
				if(propIndexNode)
				{
					var bs:String
					var indexValNode:XML
					bs=indexValNode.Gettext();
					seriesIndex=_ttoi((TCHAR)bs);
				}
			}
			for(UINT i=1;i<guids.size();i++)
			{
				var nextCompNode:XML
				if(!nextCompNode)
					continue;
				var nextNameNodes:XMLList
				if(!nextNameNodes || nextNameNodes.Getlength()==0)
					continue;
				if(nextNameNodes.Getlength()==1)
				{
					var nextNameNode:XML
					var t:String
					var endpointNode:XML
					if(endpointNode)
					{
						var map:Dictionary
						var atrb:String
						if(atrb!=0)
						{
							var ID:String
							bindingIDs.push_back(ID);
							var delNode:XML
							delNode.GetparentNode().removeChild(delNode);
						}
					}
				}
				else if (nextNameNodes.Getlength()>1)//subelement binding - so we need to remove the node with the same series number
				{
					for(var j:int
					{
						var nextNameNode:XML
						var bs:String
						var propIndexNode:XML
						if(propIndexNode)
						{
							var bs:String
							var indexValNode:XML
							bs=indexValNode.Gettext();
							var index:int
							if(index==seriesIndex)
							{
								var endpointNode:XML
								if(endpointNode)
								{
									var map:Dictionary
									var atrb:String
									if(atrb!=0)
									{
										var ID:String
										bindingIDs.push_back(ID);
										var delNode:XML
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
		var pDom:XML

		//Get CXCanvas node
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		if ( canvasNode != NULL )
		{
			var map:Dictionary
			if (map != NULL)
			{
				var atrb:String
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
void cmpDocument::SetCanvasSize(var nWidth:int
{
	try
	{
		//Set doc pointer;
		var pDom:XML
		
		//Get CXCanvas node
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var map:Dictionary
		var pXMLAttribute:String
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
std::vector<CString> cmpDocument::GetBindingsForProperty(var instanceID:String
{
	std::vector<CString> result;
	try
	{
	var pDom:XML
	var compNode:XML
	compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(instanceID) + "']");
	if(!compNode)
	{	
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
	}
	if(!compNode)
	{
		return result;
	}
	var tmp:String
	
		var bindingsNode:XML
		if(!bindingsNode)
			return result;
		var propNode:XML
		var propPath:String
		propNode = bindingsNode.selectSingleNode(_bstr_t(propPath));
		var nodeList:XMLList
		if(propNode)
		{
			var valueNode:XML
			if(valueNode)
			{
				nodeList=valueNode.selectNodes("./binding/endpoint");
					
			var n:int
			for(var i:int
			{
				var pEndPointNode:XML
				var map:Dictionary
				var atrbID:String
				var ID:String
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
void cmpDocument::CreateCSSStyleDeclaration(var guid:String
{
	try
	{
		var pDom:XML
		var canvasNode:XML
		var stylesNode:XML
		if(stylesNode==NULL)
		{
			stylesNode = pDom.createElement("Styles");
			canvasNode.appendChild(stylesNode);
		}
		var styleNode:XML
		var bs:String
		styleNode.setAttribute("id",bs);
		stylesNode.appendChild(styleNode);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
var cmpDocument:Boolean
{
	try
	{
		if(xmlString.IsEmpty())
		{
			return true;//there is nothing to set - component does not have the functionality yet
		}
		var pDom:XML
		var compNode:XML
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{	
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return false;
		}
		var persistNode:XML
		var pDOM2:XML
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
				var root:XML
				persistNode.appendChild(root.cloneNode(VARIANT_TRUE));
			}
			return true;
		}
		//if we got here we have to merge persist
		var child:XML
		var oldArr:CArrayArgument
		oldArr.loadString(child.Getxml());
		var nOldNames:int
		//the key in the map is the property name; it contains indexes of the arguments in oldArr
		std::map <CString,int> oldNameNodes;
		for(var i:int
		{
			var a:CArgument
			if(a.getType()==CArgument::object)
			{
				var pObj:CObjectArgument
				CStringArgument pNameArg=(CStringArgument)(pObj.get("name"));
				var propName:String
				oldNameNodes[propName]=i;
			}
		}
				
		var arrArg:CArrayArgument
		arrArg.loadString(xmlString);
		var n:int
		for(var i:int
		{
			var pArg:CArgument
			if( pArg.getType() == CArgument::object)
			{
				var pObjArg:CObjectArgument
				CStringArgument pArName=(CStringArgument)(pObjArg.get(_T("name")));
				var propName:String
				var pArValue:CArgument
				if(oldNameNodes.find(propName)==oldNameNodes.end())
				{
					//the property is new - append it to array
					oldArr.add(pObjArg.clone());
				}
				else
				{
					var index:int
					((CObjectArgument)oldArr.get(index)).set(_T("value"),pArValue.clone());
				}
			}
		}
		var strRes:String
		oldArr.saveString(&strRes);
		VARIANT_BOOL loadSuccess =  pDOM2.loadXML(_bstr_t(strRes));
		if(loadSuccess==VARIANT_TRUE)
		{
			var root:XML
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
var cmpDocument:String
{
	var res:String
	try
	{
		var pDom:XML
		var compNode:XML
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{	
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
		{
			return res;
		}
		var persistNode:XML
		if(persistNode==NULL)
		{
			return res;
		}
		var child:XML
		if(child==NULL)
		{
			return res;
		}
		var bs:String
		res=CString((TCHAR)bs);
	}
	catch(...)
	{
		assert(false);
	}
	return res;
}
void cmpDocument::SetExportSettings(var bUseCurrent:Boolean
{
	try
	{
		var pDom:XML
		var settNode:XML
		var canvasNode:XML
		settNode = canvasNode.selectSingleNode("ExportSettings");
		if(!settNode)
		{
			settNode = pDom.createElement(_T("ExportSettings"));
			canvasNode.appendChild(settNode);
		}
			settNode.setAttribute(_T("useCurrent"),bUseCurrent);
			var bs:String
			settNode.setAttribute(_T("path"),bs);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
var cmpDocument:Boolean
{
	bUseCurrent = true;
	try
	{
		var pDom:XML
		var settNode:XML
		var canvasNode:XML
		settNode = canvasNode.selectSingleNode("ExportSettings");
		if(!settNode)
		{
			return false;
		}
		var map:Dictionary
		if(!map)
			return false;
		bstr_t b=_T("useCurrent");
		var atrb:String
		if(!atrb)
			return false;
		bUseCurrent=atrb.Getvalue();
		b=_T("path");
		var atrb1:String
		if(!atrb1)
			return false;
		var bspath:String
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
	var pDom:XML
	var canvasNode:XML
	var airNode:XML
	if(!airNode) {
		airNode = pDom.createElement(_T("AirSettings"));
		canvasNode.appendChild(airNode);
	}
	var bs:String
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
var cmpDocument:Boolean
{
	var pDom:XML
	var canvasNode:XML
	var airNode:XML
	if(!airNode) return false;

	var map:Dictionary
	if(!map) return false;

	bstr_t b =_T("name");
	var atrb:String
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
	var nCanvasWidth:int
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
	var pDom:XML
	var canvasNode:XML
	var airNode:XML
	if(!airNode) 
		return GetCanvasSize(nWidth, nHeight);

	var map:Dictionary
	if(!map) 
		return GetCanvasSize(nWidth, nHeight);
	var atrb:String
	if(!atrb)
	{
		return GetCanvasSize(nWidth, nHeight);
	}
	else
	{
		var t:String
		if(t==_bstr_t("0"))
		{
			return GetCanvasSize(nWidth, nHeight);
		}
	}
	var atrb1:String
	var atrb2:String
	if(!atrb1 || !atrb2)
	{
		return GetCanvasSize(nWidth, nHeight);
	}
	nWidth=atrb1.Getvalue();
	nHeight=atrb2.Getvalue();
}

var cmpDocument:Boolean
{
	try
	{
		var pDom:XML

		//Get node list
		var nodelist:XMLList
		if(bCompIsConn)
		{
			nodelist = pDom.selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
		}
		else
		{
			nodelist = pDom.selectNodes("/CXCanvas/component[@className='" + _bstr_t(className) + "']");
		}
		
		var n:int
		for(var i:int
		{
			var compNode:XML
			var propertiesNode:XML
			if(propertiesNode != NULL)
			{
				var propNode:XML
				propNode = propertiesNode.selectSingleNode("./property//name[./string = '" + _bstr_t(propertyName) + "']/following-sibling::");
				if(propNode != NULL)
				{
					var oldValue:XML
					if ( oldValue != NULL )
					{
						var valueXML:String
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


var cmpDocument:Boolean
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

var cmpDocument:Boolean
{
	try
	{
	//we should probably move this function up to XcelsiusDoc
	var fileExt:String
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

	var uniqExt:String

	if((_tcsicmp(fileExt.c_str(),_T(".gif"))==0)||(_tcsicmp(fileExt.c_str(),_T(".bmp"))==0)||(_tcsicmp(fileExt.c_str(),_T(".png"))==0))
		uniqExt=_T(".jpg");
	else
		uniqExt=fileExt;

	var tmpname:String

	//make sure the path exists
	var buff:String
	::PathRemoveFileSpec(buff);
	_tmkdir(buff);
	delete [] buff;

	tempFile = tmpname.c_str();

	var name:String
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



var cmpDocument:String
{
	//var result:String
	
	var arrNode:XML
	if(lastNode==NULL)
		pAssetProp.appendChild(arrNode);
	else
		lastNode.appendChild(arrNode);

	//we need to parse the assets node and create the equivalent property value
	var ptemp:XML
	ptemp.CreateInstance(__uuidof(DOMDocument40));
	ptemp.loadXML( _bstr_t(assetNode) );
	var parentNode:XML

	var assets:XMLList
	if(assets && assets.Getlength())
	{
		for(var i:int
		{
			//var prop:String
			//prop.Format(_T("<property id=\"%d\">"), i);
			//result += prop;

			var propNode:XML
			std::wostringstream oss;oss<<i;
			var num:String
			propNode.setAttribute("id",num.c_str());
			arrNode.appendChild(propNode);

			var anode:XML
			var anodeList:XML
			if( anodeList )
			{
				//Lazy way, This should be redone before release
				CreateAssetProperty(anodeList.Getxml(),propNode);				

			}
			else
			{
				var valueNode:XML
				if(valueNode)
				{
					//var v:String
					//prop.Format(_T("<string>%s</string>"), v.c_str());
					//result += prop;

					var strNode:XML
					strNode.text =  valueNode.Gettext();
					propNode.appendChild(strNode);


				}
			}
			//result += _T("</property>");
		}
	}

	//result += _T("</array>");
	var result:String
	return result;
}

void cmpDocument::CreateAssetValue(const var tempFile:String
{
	//var result:String
	var tempNode:XML
	tempNode.text = tempFile;
	var origNode:XML
	origNode.text = origFile;
	var embedNode:XML
	embedNode.text = embed;

	assetENode.appendChild(tempNode);
	assetENode.appendChild(origNode);
	assetENode.appendChild(embedNode);
	

	/
	var temp:String
	var orig:String
	var embedMethod:String

	result = temp;
	result += orig;
	result += embedMethod;

	return result;/
}

var cmpDocument:Boolean
{



	var arrNode:XML

	if(lastNode==NULL)
		pAssetDoc.appendChild(arrNode);
	else
		lastNode.appendChild(arrNode);	


	//

	//only return false if one of the images failed to convert
	//result = _T("");

	//loop through each 
	
	var count:int

	if( count != embed.size() || count == 0)
		return true;

	//result = _T("<array>");

	for( var i:int
	{
		var file:CArgument


		var assetNode:XML
		std::wostringstream oss;oss<<i;
		var num:String
		assetNode.setAttribute("id",num.c_str());
		arrNode.appendChild(assetNode);

		if( file.getType() == CArgument::string )
		{	
			CStringArgument embedType= (CStringArgument)embed.get(i);
			CStringArgument argString = (CStringArgument)file;

			var tempFile:String
			if( _tcscmp(embedType.getValue(),_T("url")) )//convert anything but URL 
			{
				var emb:Boolean
				if((_tcscmp(embedType.getValue(),_T("embed"))==0)||(_tcscmp(embedType.getValue(),_T("true"))==0))
					emb=true;

				
				var res:Boolean
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
			var argArray:CArrayArgument
			var embedArray:CArrayArgument
			var res:Boolean
			if(res==false) return false;
		}
		/

		var nodeString:String
		nodeString.Format(_T("<asset id=\"%d\">"), i);
		nodeString += assetNode.c_str();
		nodeString += _T("</asset>");

		result += nodeString;/
	}

	//result += _T("</array>");

	return true;
}

var cmpDocument:Boolean
{

	//find the component node
	var pDom:XML
	var compNode:XML
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
	var asset:String

	pAssetDoc = NULL;
	pAssetDoc.CreateInstance("Msxml2.DOMDocument.4.0");

	var result:Boolean
	if(!result) return false;


	//need the assets node to set the asset
	var assetsNode:XML

	//if no assets node, then make one
	if(!assetsNode) 
	{
		assetsNode = pDom.createElement("assets");
		if(compNode) compNode.appendChild(assetsNode);
	}

	//find asset
	var pname:String
	var assetPath:String
	var oldAssetNode:XML
	if( assetsNode.selectSingleNode(_bstr_t(assetPath)) )
		oldAssetNode = assetsNode.selectSingleNode(_bstr_t(assetPath)).GetparentNode();

	var newAssetNode:XML
	var nameNode:XML
	var nameChild:XML
	nameChild.text = propName;
	var valueNode:XML

	newAssetNode.appendChild(nameNode);
	newAssetNode.appendChild(valueNode);
	nameNode.appendChild(nameChild);

	//begin lame way to create a node from markup in MSXML2
	var ptemp:XML
	ptemp.CreateInstance(__uuidof(DOMDocument40));
	ptemp.loadXML( pAssetDoc.xml );
	var bindTemp:XML
	var bindings:XML

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

var cmpDocument:Boolean
{
	try
	{
		var temp:String
		if( ConvertFiles(fileName, temp, embed) == false ) return false;

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		var pDom:XML
		var compNode:XML
		compNode = pDom.selectSingleNode("//canvas");

		if(!compNode)
		{
			compNode = pDom.createElement("canvas");
			var canvasNode:XML
			canvasNode.appendChild(compNode);
		}


		//need the assets node to set the asset
		var assetsNode:XML

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

var cmpDocument:Boolean
{
	try
	{
		var temp:String
		if( ConvertFiles(fileName, temp,embed) == false ) return false;

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		var pDom:XML
		var compNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}

		if(!compNode) return false;


		//need the assets node to set the asset
		var assetsNode:XML

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

void cmpDocument::setAsset(const var assetsNode:XML
{
	var pDom:XML
	//find asset
	var pname:String
	var assetPath:String
	var oldAssetNode:XML
	if( assetsNode.selectSingleNode(_bstr_t(assetPath)) )
		oldAssetNode = assetsNode.selectSingleNode(_bstr_t(assetPath)).GetparentNode();

	var newAssetNode:XML
	var nameNode:XML
	var valueNode:XML
	var nameChild:XML
	nameChild.text = propName;

	newAssetNode.appendChild(nameNode);
	newAssetNode.appendChild(valueNode);
	nameNode.appendChild(nameChild);

	var tempFileName:XML
	tempFileName.text = _bstr_t(tmpFileName);
	valueNode.appendChild(tempFileName);

	var origFileName:XML
	origFileName.text = originalFileName;
	valueNode.appendChild(origFileName);

	var embedTag:XML
	embedTag.text = embed==true?"true":"false";
	valueNode.appendChild(embedTag);

	if(oldAssetNode)
		assetsNode.replaceChild(newAssetNode, oldAssetNode);
	else
		assetsNode.appendChild(newAssetNode);
}

var cmpDocument:String
{
	try
	{
		var asset:String

		var pDom:XML
		var compNode:XML
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}

		if(compNode)
		{
			//need the assets node to set the asset
			var assetsNode:XML
			var buff:String

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


var cmpDocument:Boolean
{
	try
	{

		//okay we have the temp file (converted if needed)
		//we need to construct the asset xml and set the property

		var pDom:XML
		var compNode:XML
		compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}

		if(!compNode) return false;


		//need the assets node to set the asset
		var assetsNode:XML

		//if no assets node, return
		if(!assetsNode) return false;

		//we need the temporary file name and to change the embed
		var embedNode:XML

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
		var pDom:XML

		//Get CXCanvas node
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var nodelist:XMLList
		var n:int
		for(var i:int
		{
			var connNode:XML
			canvasNode.removeChild(connNode);
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
std::vector<CString> cmpDocument::ApplyColorSchemeToExistingComponents(var csColorScheme:String
{
	std::vector<CString> ids;
	try
	{
		//returns vector of component ids where it was applied
		var pDom:XML
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var nodelist:XMLList
		var n:int
		for(var i:int
		{
			var compNode:XML
			var colorSchemeNode:XML
			if(!colorSchemeNode)
			{
				colorSchemeNode = pDom.createElement("colorScheme");
				compNode.appendChild(colorSchemeNode);
			}
			colorSchemeNode.text = _bstr_t(csColorScheme);
			//Get id
			var map:Dictionary
			var b:String
			var atrb:String
			var id:String
			var csId:String
			ids.push_back(csId);
		}
	}
	catch(...)
	{
		assert(false);
	}
	return ids;
}
void cmpDocument::SetCurrentColorScheme(var csColorScheme:String
{
	try
	{
		var pDom:XML
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var colorSchemeNode:XML
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

const var cmpDocument:String
{
	return colorSchemesXMLMap[id];
}


var cmpDocument:String
{
	var res:String
	try
	{
		var pDom:XML
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var colorSchemeNode:XML
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
		var pDom:XML
		var canvasNode:XML
		var nodelist:XMLList
		var n:int
		for(var i:int
		{
			var bs:String
			var str:String
			colorSchemeSet.insert(str);
		}
		var canvasSchemeNode:XML
		if(canvasSchemeNode)
		{
			var str:String
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
		var pDom:XML
		var canvasNode:XML
		var oldSchemesNode:XML
		var schemesNode:XML

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
			var csName:String
			if(csName.IsEmpty())
				continue;//do nothing for default
			if(names.find(csName)==names.end())//don't save if it's not referenced
			{
				continue;
			}
			var csXML:String
			var pTempDom:XML
			pTempDom.CreateInstance(__uuidof(DOMDocument40));
			/ VARIANT_BOOL loadSuccess = / pTempDom.loadXML(_bstr_t(csXML));
			var pRoot:XML
			var pClone:XML
			var schemeNode:XML
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
		var pDom:XML
		var canvasNode:XML
		var schemesNode:XML
		if(!schemesNode)
		{
			return;
		}
		var list:XMLList
		var n:int
		for(var i:int
		{
			var pRootNode:XML
			var map:Dictionary
			var atrb:String
			var ID:String
			colorSchemesXMLMap[ID]=(TCHAR)pRootNode.Getxml();
		}
	}
	catch(...)
	{
		assert(false);
		return;
	}
}
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML

		//Get compnent node
		var compNode:XML
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return false;
		}
		var metaNode:XML
		if(!metaNode)
		{
			return false;
		}
		var tagList:XMLList
		if( tagList )
		{
			var count:int
			for(var i:int
			{
				var node:XML
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML

		//Get compnent node
		var compNode:XML
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return false;
		}
		var metaNode:XML
		if(!metaNode)
		{
			metaNode=pDom.createElement("metaData");
			compNode.appendChild(metaNode);
		}
		var tagNode:XML
		if(!tagNode)
		{
			tagNode=pDom.createElement("tag");
			metaNode.appendChild(tagNode);
		}
		tagNode.Puttext(_bstr_t(tag));
		var dataNode:XML
		if(!dataNode)
		{
			dataNode=pDom.createElement("data");
			metaNode.appendChild(dataNode);
		}
		dataNode.Puttext(_bstr_t(data));
	/	removed as data is just a string
		var oldDataNode:XML
		var dataNode:XML
		var pDOM2:XML
		pDOM2.CreateInstance(__uuidof(DOMDocument40));
		pDOM2.PutpreserveWhiteSpace(VARIANT_FALSE);
		VARIANT_BOOL loadSuccess = pDOM2.loadXML(_bstr_t(data));
		var root:XML
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
void cmpDocument::SetComponentColorScheme(var compID:String
{
	try
	{
		var pDom:XML
		//Get compnent node
		var compNode:XML
		if(!compNode)
		{
			return;
		}
		var colorSchemeNode:XML
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
void cmpDocument::SetCurrentSkin(var csSkin:String
{
	try
	{
		var pDom:XML
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var skinNode:XML
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
var cmpDocument:String
{
	try
	{
		var pDom:XML
		var canvasNode:XML
		var res:String
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var skinNode:XML
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
void cmpDocument::SetDisplayName(const var compID:String
{
	try
	{
		var pDom:XML
		//Get component node
		var compNode:XML
		compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//component[@id='" + _bstr_t(compID) + "']");
		}
		if(compNode == NULL)
		{
			return;
		}
		var dispName:String
		compNode.setAttribute("displayName",displayName);
	}
	catch(...)
	{
		assert(false);
		return;
	}
}

var cmpDocument:int
{
	try
	{
		//empty list
		assets.clear();

		var pDom:XML

		var assetList:XMLList
		if( assetList )
		{
			var count:int
			for(var i:int
			{
				BSTR b;
				var node:XML

				var parent:XML

				//don't archive if it's a URL
				if(parent)
				{
					var embed:XML
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
		
		var pDom:XML
		var acount:int
		var assetList:XMLList
		if( assetList )
		{
			var count:int
			for(var i:int
			{
				BSTR b;
				var node:XML
				var parent:XML

				//don't archive if it's a URL
				if(parent)
				{
					var embed:XML
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
	var rv:int
	var pDom:XML
	if (pDom) {
		var txt:String
		var list:XMLList
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

		var pDOM:XML

		var nodeList:XMLList
		if( nodeList.Getlength() )
		{
			var tempFileNode:XML
			BSTR b;
			var key:String
			for(var i:int
			{
				tempFileNode = nodeList.Getitem(i);
				tempFileNode.get_text(&b);

				key = b;
				if ( assetMap.find(key) != assetMap.end() ) 
				{
					tempFileNode.text = assetMap[key].c_str();

					//now find the property strings
					var propNodeList:XMLList
					if( propNodeList )
					{
						var cnt:int
						for(var j:int
						{
							var propNode:XML
							var stringNode:XML
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
			var path:String
			var str1:String
			var str2:String
			var request:String
			var pathNodes:XMLList
			var n:int
			for(var i:int
			{
				var pathNode:XML
				var stringNode:XML
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
var cmpDocument:String
{
	var n:int
	var tryStr:String
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
var cmpDocument:Boolean
{
	try
	{
		var pDom:XML
		var compList:XMLList
		if(compList)
		{
			var count:int
			for(var i:int
			{
				var compNode:XML
				var map:Dictionary
				var atrb:String
				var bs:String
				var displayName:String
				if(displayName==csName)
					return true;
			}
		}
		compList = pDom.selectNodes("//connection");
		if(compList)
		{
			var count:int
			for(var i:int
			{
				var compNode:XML
				var map:Dictionary
				var atrb:String
				var bs:String
				var displayName:String
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
var cmpDocument:String
{
	var res:String
	try
	{
		var pDom:XML
		var compNode:XML
		if(!compNode)
			return res;
		var parentNode:XML
		if(!parentNode)
			return res;
		var map:Dictionary
		var atrb:String
		var bs:String
		var className:String
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
var cmpDocument:Boolean
{
	var pDom:XML
	try
	{
	var compNode:XML
	if(!compNode)
		return false;

	var parentNode:XML
	if(!parentNode)
		return false;
	var map:Dictionary
	var atrb:String
	var bs:String
	var className:String
	if(className==_T("xcelsius.canvas.Group"))
	{
		var nodeList:XMLList
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
std::map<CString,CString> cmpDocument::GetBindingInfo(var compID:String
{
	std::map<CString,CString> res;
	try
	{
		var pDom:XML
		var compNode:XML
		if(!compNode)
		{
			compNode = pDom.selectSingleNode("//connection[@id='" + _bstr_t(compID) + "']");
		}
		if(!compNode)
			return res;
		var request:String
		var nameNodes:XMLList
		if(!nameNodes)
			return res;
		var n:int
		for(var i:int
		{
			var bindingNode:XML
			if(!bindingNode)
				continue;
			var endpointNode:XML
			if(!endpointNode)
				continue;
			var map:Dictionary
			var atrb:String
			var bindingID:String
			var chainNode:XML
			if(!chainNode)
				continue;
			var chainXML:String
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
		var pDom:XML

		//Get CXCanvas node
		var canvasNode:XML
		canvasNode = pDom.selectSingleNode("CXCanvas");
		var nodelist:XMLList
		var n:int
		for(var i:int
		{
			var compNode:XML
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
	var pDom:XML
	result.clear();
	for(UINT i=0;i<list.size();i++)
	{
		if(IsGroup(list[i]))
		{
			var compNode:XML
			var nodelist:XMLList
			for(var j:int
			{
				var childNode:XML
				var childmap:Dictionary
				var atrbID:String
				var childID:String
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
var cmpDocument:Boolean
{
	var pDom:XML
	try
	{
		var compNode:XML
		if(!compNode)
			return false;
		var map:Dictionary
		var atrb:String
		if(!atrb)
			return false;
		var classNameString:String
		var csClassName:String
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
var cmpDocument:String
{
	var res:String
	var pDom:XML
	try
	{
		var compNode:XML
		if(!compNode)
			return res;
		var assetsNode:XML
		if(!assetsNode)
			return res;
		var request:String
		var nameNode:XML
		if(!nameNode)
			return res;
		var valueNode:XML
		if(!valueNode)
			return res;
		var list:XMLList
		var arrArg:CArrayArgument
		var n:int
		for(var i:int
		{
			var aNode:XML
			//assets can be an 1D array or 2D array
			var subArrNode:XML
			if(subArrNode)
			{
				//2D array
				var arrSubArg:CArrayArgument
				var subList:XMLList
				var m:int
				for(var j:int
				{
					var aSubNode:XML
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

var cmpDocument:CObjectArgument
{
	var objArg:CObjectArgument
	var tempFileNode:XML
	var csTempFile:String
	objArg.set("temporaryFile",csTempFile);
	var origFileNode:XML
	var csOrigFile:String
	objArg.set("originalFile",csOrigFile);
	var embedNode:XML
	var csEmbed:String
	var bEmbedded:Boolean
	if(csEmbed==_T("embed"))
		bEmbedded=true;
	objArg.set("embed",bEmbedded);
	return objArg;
}
var cmpDocument:int
{
	var nodelist:XMLList
	var pDom:XML
	nodelist = pDom.selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
	var n:int
	for(var i:int
	{
		var connNode:XML
		var parent:XML
		parent.removeChild(connNode);
	}
	return n;
}
var cmpDocument:Boolean
{
	var nodelist:XMLList
	var pDom:XML
	nodelist = pDom.selectNodes("/CXCanvas/connection[@className='" + _bstr_t(className) + "']");
	var n:int
	return (n!=0);
}
var cmpDocument:String
{
	var res:String
	var pDom:XML
	var canvasNode:XML
	var noteNode:XML
	if(noteNode)
	{
		MSXML2::IXMLDOMCDATASectionPtr cData=noteNode.GetfirstChild();
		res=(TCHAR)cData.data;
	}
	return res;

}
void cmpDocument::SetNote(var csNote:String
{
	var pDom:XML
	var canvasNode:XML
	var noteNode:XML
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

var cmpDocument:Array
{
	var cuids:Array

	//get crystalPersistXML
	//get the value
	//parse the persistxml
	//grab the cuids
	//put it into the array and return it
	var pDom:XML
	var canvasNode:XML

	var persistXMLNode:XML
	if(persistXMLNode)
	{
		var oldValue:XML
		var persistXML:String

		//we need to parse "cuids" out of b.
		var cuidDOM:XML
		cuidDOM.CreateInstance(__uuidof(DOMDocument40));
		
		VARIANT_BOOL loadSuccess = cuidDOM.loadXML(persistXML);

		if(loadSuccess)
		{
			var reportList:XMLList
			if(reportList)
			{
				var size:int
				for(var i:int
				{
					var report:XML
					_variant_t v = report.getAttribute("cuid");

					//we could check for duplicates here
					cuids.push_back(v.bstrVal);
				}
			}
		}
	}
	return cuids;
}
void cmpDocument::ChangeComponentLevel(var compID:String
{
try
	{
		var pDom:XML
		var compNode:XML
		if(!compNode)
		{
			return;
		}
		var parent:XML
		if(!parent)
			return;
		var clone:XML
		parent.removeChild(compNode);

		var nextSibling:XML
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
		for(var i:int
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
void cmpDocument::SetWarningTitle(var title:String
{
	m_csWarningTitle=title;
}
var cmpDocument:Array
{
	//returns list of binding ids to unbind
	var bindingIDs:Array
	var pDom:XML
	var n:int
	for(var i:int
	{
		var compNode:XML
		var nameNodes:XMLList
		for(var j:int
		{
			var nextNameNode:XML
			var endpointNode:XML
			if(endpointNode)
			{
				var map:Dictionary
				var atrb:String
				if(atrb!=0)
				{
					var ID:String
					bindingIDs.push_back(ID);
					var delNode:XML
					delNode.GetparentNode().removeChild(delNode);
				}
			}

		}
	}
	return bindingIDs;
}
var cmpDocument:String
{
	var res:String
	var arrArg:CArrayArgument
	var valueXML:String
	var pDom:XML
	var nodelist:XMLList
	var n:int
	for(var i:int
	{
		var pObj:CObjectArgument
		var connNode:XML
		var map:Dictionary
		var atrb:String
		if(!atrb)
			continue;
		var id:String
		var propertiesNode:XML
		if(propertiesNode == NULL)
		{
			valueXML=_T("<null/>");
		}
		else
		{
			var propNode:XML
			propNode = propertiesNode.selectSingleNode("./property//name[./string = 'refreshOnLoad']/following-sibling::");
			if(propNode == NULL)
			{
				valueXML=_T("<null/>");
			}		
			if(propNode != NULL)
			{
				var oldValue:XML
				if ( oldValue != NULL )
				{
					valueXML=(LPCTSTR)(oldValue.Getxml());
				}
			}
		}
		var bs:String
		var pArgValue:CArgument
		pObj.set(_T("ID"),id);
		pObj.set(_T("ROL"),pArgValue);
		arrArg.add(pObj);
	}
	arrArg.saveString(&res);
	return res;
}
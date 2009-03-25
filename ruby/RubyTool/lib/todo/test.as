void CCanvasView::BindComponent(cmpBinding binding){
	var a1:String=binding.map.c_str();
	var a2:String=binding.bindingID.c_str();
	var xmlRequest1:String=CString("<invoke name=\"createRange\" returntype=\"xml\"><arguments>")+a1+a2+CString("</arguments></invoke>");
	m_ctrlFlash.CallFunction(xmlRequest1);

	var v1:String=binding.instanceID.c_str();
	var v2:String=binding.propertyName.c_str();
	var v3:String=binding.chain.c_str();
	var v4:String=binding.bindingID.c_str();
	var v5:String=binding.direction.c_str();
	var v6:String=binding.inputtype.c_str();
	var v7:String=binding.outputtype.c_str();
	var v8:String=binding.inputmapproperties.c_str();
	var v9:String=binding.outputmapproperties.c_str();
	var xmlRequest2:String=CString("<invoke name=\"bind\" returntype=\"xml\"><arguments>")+v1+v2+v3+v4+v5+v6+v7+v8+v9+CString("</arguments></invoke>");
	m_ctrlFlash.CallFunction(xmlRequest2);

}

void CXcelsiusDoc::OnBinding( LPCTSTR transXML,var bConn:Boolean)
{
	//Send the binding to the Excel view
	GetExcelView().SetBinding((LPCTSTR)transXML);

	//Create binding action
//	OnBindingRequest((CString)transXML);

	//Persist binding
	Document.SetBinding(transXML);

	//Create binding in canvas
	cmpBinding binding = new cmpBinding();

	if(Document.CreateCanvasBinding((_bstr_t)transXML,binding))
	{
		if(!bConn)
		{
			GetCanvasView().BindComponent(binding);
		}
	}
	delete binding;
	binding=NULL;
	SetModifiedFlag();

}

void CXcelsiusDoc::CreateNewBindingID(_bstr_t& bindingXML)
{
	var pTempDom:XML;
	pTempDom.CreateInstance(__uuidof(DOMDocument40));
	/VARIANT_BOOL loadSuccess =/ pTempDom.loadXML(bindingXML);
	var pEndPoint:XML=pTempDom.selectSingleNode("//property/value/binding/endpoint");
	if (!pEndPoint)
		return;
	var pRange:XML=pEndPoint.selectSingleNode("range");
	if(!pRange)
		return;
	var strRange:String=(TCHAR)pRange.Gettext();
	//var map:Dictionary = pEndPoint.Getattributes();
	//var atrb:String = map.getNamedItem("id");
	//var id:String=atrb.Getvalue();
	var newID:String=bo::ActionBindUtil::createID(bo::ActionBindUtil::getWorkbookFromView(),(LPCTSTR)strRange);
	pEndPoint.setAttribute("id",_bstr_t(newID));
	bindingXML=pTempDom.Getxml();
}

void CXcelsiusDoc::CopyComponentsFromDom(IUnknownPtr pIUDom, BOOL bSameIDs)
{
	CFunction functionM( _T("addComponents"), _T("xml"));
	var pTypes:CArrayArgument=functionM.addArrayArgument();
	var pParentNames:CArrayArgument=functionM.addArrayArgument();
	var pAllPropertyValues:CArrayArgument=functionM.addArrayArgument();
	var pGuids:CArrayArgument=functionM.addArrayArgument();
	var pColorSchemeXMLs:CArrayArgument=functionM.addArrayArgument();

	m_bApplyCanvasColorTheme=FALSE;
	AddColorSchemesFromDom(pIUDom);
	var pDom:XML=pIUDom;
	std::map<_bstr_t,_bstr_t> idMap;
	var nodelist:XMLList;
	nodelist = pDom.selectNodes("//component");
	var n:int = nodelist.Getlength();
	for(var i:int=0;i<n;i++)
	{
		var pCompNode:XML= nodelist.Getitem(i);
		var map:Dictionary = pCompNode.Getattributes();
		var atrb:String = map.getNamedItem("className");
		var className:String=atrb.Getvalue();
		var atrb1:String = map.getNamedItem("styleName");
		var styleName:String=atrb1.Getvalue();
		var atrb2:String = map.getNamedItem("id");
		var compID:String=atrb2.Getvalue();
		var str:String=className;
		//CFunction function( _T("addComponent"), _T("xml"));
		//function.addArgument( className );
		pTypes.add(className);
		var pCompParent:XML = pCompNode.GetparentNode();
		var bsParent:String="";
		if(pCompParent!=NULL)
		{
			var mapParent:Dictionary = pCompParent.Getattributes();
			var atrbID:String = mapParent.getNamedItem("id");
			if(atrbID)
			{
				bsParent=atrbID.Getvalue();
				if(!bSameIDs)
				{
					bsParent=idMap[bsParent];
				}
			}
		}
		//function.addArgument(bsParent);
		pParentNames.add(bsParent);
		var pArgs:CArrayArgument = new CArrayArgument();
		var csStyleName:String=styleName;
		if(!csStyleName.IsEmpty())
		{
			var pObjArg:CObjectArgument = new CObjectArgument();
			pObjArg.set( _T("id"), _T("styleName"));
			pObjArg.set( _T("value"), csStyleName );
			pArgs.add( pObjArg );

		}
		var pPropertiesNode:XML = pCompNode.selectSingleNode( "properties" );
		if(pPropertiesNode)
		{

			var pChildList:XMLList=pPropertiesNode.GetchildNodes();
			var nProp:int=pChildList.Getlength();
			for(var j:int=0;j<nProp;j++)
			{
				var pPropNode:XML= pChildList.Getitem(j);
				var pNameNode:XML = pPropNode.selectSingleNode("name");
				var bstrName:String=pNameNode.Gettext();
				var pValueNode:XML = pPropNode.selectSingleNode("value");
				var pNodeValueObject:XML;
				pValueNode.get_firstChild(&pNodeValueObject);
				CComBSTR bstrValue;
				pNodeValueObject.get_xml( &bstrValue );
				var pArgValue:CArgument = CArgument::Load( bstrValue );
				//add 10 to coordinates when copying comps to canvas (only to the first level components)
				if(m_bAddToCmpDoc && bsParent.length()==0)
				{
					var temp:String=(TCHAR)bstrName;
					if(temp==_T("x")||temp==_T("y"))
					{
						if(pArgValue.getType()==CArgument::number)
						{
							CNumberArgument pCoordArg=(CNumberArgument)pArgValue;
							double coord=pCoordArg.getValue();
							pCoordArg.setValue(coord+10);
						}
					}
				}
				var pObjArg:CObjectArgument = new CObjectArgument();
				pObjArg.set( _T("id"), bstrName);
				pObjArg.set( _T("value"), pArgValue );
				pArgs.add( pObjArg );
			}
		}
		if(pPropertiesNode || !csStyleName.IsEmpty())
		{
			//function.addArgument(pArgs);
			pAllPropertyValues.add(pArgs);
		}
		else
		{
			var pNullArg:CArgument = new CNullArgument();
			//function.addArgument(pNullArg);
			pAllPropertyValues.add(pNullArg);
		}
		var newID:String=GenerateGUID();
		pGuids.add(newID);
		idMap[compID]=newID;

		///apply color theme
		var pColorSchemeNode:XML = pCompNode.selectSingleNode("colorScheme");
		var colorXML:String=_T("");
		if(pColorSchemeNode)
		{
			// find the xml for this scheme and call ApplyColorThemeToComponent
			var colorSchemeID:String=pColorSchemeNode.Gettext();
			m_csCopyColorThemeName=colorSchemeID;
			var colorSchemeXml:String=Document.colorSchemesXMLMap[colorSchemeID];
			if(!colorSchemeXml.IsEmpty() && !colorSchemeID.IsEmpty())
			{
				ColorTheme theme(colorSchemeXml,m_pColorSchemeDom);
				colorXML=theme.getCSS(TRUE);
				//function.addArgument();//last argument seems to cause problems with pasting twice
				pColorSchemeXMLs.add(colorXML);

			}
		}
	}
	var request:String;
	functionM.saveString(&request);
	var retval:String;
	try
	{
		retval = GetCanvasView().GetFlashObject().CallFunction(request);
	}
	catch(...)
	{
		((CMainFrame)(::AfxGetMainWnd())).LogDebug(_T("CXcelsiusDoc::CopyComponentsFromDom function failed"));
		TRACE0("call function failed");
	}
	CopyStylesAndBindingsFromDom(pIUDom,idMap);
}

void CXcelsiusDoc::CopyStylesAndBindingsFromDom(IUnknownPtr pIUDom,std::map<_bstr_t,_bstr_t>& idMap)
{
	var pDom:XML=pIUDom;
	var nodelist:XMLList;
	nodelist = pDom.selectNodes("//component");
	var n:int = nodelist.Getlength();
	for(var i:int=0;i<n;i++)
	{
		var pCompNode:XML= nodelist.Getitem(i);
		var map:Dictionary = pCompNode.Getattributes();
		var atrb2:String = map.getNamedItem("id");
		var compID:String=atrb2.Getvalue();
		var instanceID:String=idMap[compID];
		if(instanceID.length()==0)
		{
			ASSERT(FALSE);
			continue;
		}
		//component styles...is there a way to bootstrap the canvas in a less granular way?
		var pStylesNode:XML = pCompNode.selectSingleNode("./styles");
		if(pStylesNode)
		{
			var stylesList:XMLList = pStylesNode.selectNodes("./property");
			var nProp:int=stylesList.Getlength();
			for(var j:int=0;j<nProp;j++)
			{
				var pPropNode:XML= stylesList.Getitem(j);
				var pNameNode:XML = pPropNode.selectSingleNode("name/string");
				var bstrName:String=pNameNode.Gettext();

				var pValueNode:XML = pPropNode.selectSingleNode("value");
				var pValueElement:XML = pValueNode.GetfirstChild();
				var bstrValXML:String = pValueElement.Getxml();

				GetCanvasView().ModifyComponentStyle(instanceID, bstrName, bstrValXML);
			}

		}
		//when copy pasting, we should copy the styles and properties to the doc, as Canvas does not send us notifications about their changes
		var pMainDom:XML = Document.GetXmlDom();
		var compNode:XML = pMainDom.selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(pStylesNode)
		{
			compNode.appendChild(pStylesNode.cloneNode(VARIANT_TRUE));
		}
		var propNode:XML=compNode.selectSingleNode("properties");//main domain properties
		var pPropertiesNode:XML = pCompNode.selectSingleNode( "properties" );//copied properties

		compNode.replaceChild(pPropertiesNode.cloneNode(VARIANT_TRUE),propNode);
		//copy persist information
		var pPersistNode:XML = pCompNode.selectSingleNode("Persist");
		if(pPersistNode)
		{
			compNode.appendChild(pPersistNode.cloneNode(VARIANT_TRUE));
		}
		var pAssetsNode:XML = pCompNode.selectSingleNode("assets");
		if(pAssetsNode)
		{
			compNode.appendChild(pAssetsNode.cloneNode(VARIANT_TRUE));
		}
		////now create bindings
		//Get list of bindings
		var pBindingsNode:XML = pCompNode.selectSingleNode("./bindings");
		if ( pBindingsNode )
		{
			var pBindingsList:XMLList = pBindingsNode.GetchildNodes();//pCompNode.selectNodes("./bindings");
			if(pBindingsList)
			{
				//Loop through all bindings
				var pBindNode:XML = pBindingsList.nextNode();
				var len:int = pBindingsList.Getlength();
				var j:int = 0;
				while (j<len)
				{
					//Get bindings xml
					var bindingXML:String = pBindNode.Getxml();
					if(m_bAddToCmpDoc) //it means we are copying
					CreateNewBindingID(bindingXML);
					//Construct bindings xml
					bindingXML = "<component id=\"" + instanceID + "\"><onrequestbindings>" + bindingXML + "</onrequestbindings></component>";
					//Call OnBinding method
					this.OnBinding(bindingXML);
					//Get next node
					pBindNode = pBindingsList.nextNode();
					//Increment counter
					j++;
				}
			}
		}

	}
	m_bApplyCanvasColorTheme=TRUE;

}

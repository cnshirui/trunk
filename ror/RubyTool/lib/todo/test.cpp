void CCanvasView::BindComponent(cmpBinding *binding){
	CString a1=binding->map.c_str();
	CString a2=binding->bindingID.c_str();
	CString xmlRequest1=CString("<invoke name=\"createRange\" returntype=\"xml\"><arguments>")+a1+a2+CString("</arguments></invoke>");
	m_ctrlFlash.CallFunction(xmlRequest1);

	CString v1=binding->instanceID.c_str();
	CString v2=binding->propertyName.c_str();
	CString v3=binding->chain.c_str();
	CString v4=binding->bindingID.c_str();
	CString v5=binding->direction.c_str();
	CString v6=binding->inputtype.c_str();
	CString v7=binding->outputtype.c_str();
	CString v8=binding->inputmapproperties.c_str();
	CString v9=binding->outputmapproperties.c_str();
	CString xmlRequest2=CString("<invoke name=\"bind\" returntype=\"xml\"><arguments>")+v1+v2+v3+v4+v5+v6+v7+v8+v9+CString("</arguments></invoke>");
	m_ctrlFlash.CallFunction(xmlRequest2);

}

void CXcelsiusDoc::OnBinding( LPCTSTR transXML,bool bConn)
{
	//Send the binding to the Excel view
	GetExcelView()->SetBinding((LPCTSTR)transXML);

	//Create binding action
//	OnBindingRequest((CString)transXML);

	//Persist binding
	Document->SetBinding(transXML);

	//Create binding in canvas
	cmpBinding* binding = new cmpBinding();

	if(Document->CreateCanvasBinding((_bstr_t)transXML,binding))
	{
		if(!bConn)
		{
			GetCanvasView()->BindComponent(binding);
		}
	}
	delete binding;
	binding=NULL;
	SetModifiedFlag();

}

void CXcelsiusDoc::CreateNewBindingID(_bstr_t& bindingXML)
{
	IXMLDOMDocument2Ptr pTempDom;
	pTempDom.CreateInstance(__uuidof(DOMDocument40));
	/*VARIANT_BOOL loadSuccess =*/ pTempDom->loadXML(bindingXML);
	MSXML2::IXMLDOMElementPtr pEndPoint=pTempDom->selectSingleNode("//property/value/binding/endpoint");
	if (!pEndPoint)
		return;
	MSXML2::IXMLDOMNodePtr pRange=pEndPoint->selectSingleNode("range");
	if(!pRange)
		return;
	CString strRange=(TCHAR*)pRange->Gettext();
	//MSXML2::IXMLDOMNamedNodeMapPtr map = pEndPoint->Getattributes();
	//MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("id");
	//_bstr_t id=atrb->Getvalue();
	CString newID=bo::ActionBindUtil::createID(bo::ActionBindUtil::getWorkbookFromView(),(LPCTSTR)strRange);
	pEndPoint->setAttribute("id",_bstr_t(newID));
	bindingXML=pTempDom->Getxml();
}

void CXcelsiusDoc::CopyComponentsFromDom(IUnknownPtr pIUDom, BOOL bSameIDs)
{
	CFunction functionM( _T("addComponents"), _T("xml"));
	CArrayArgument* pTypes=functionM.addArrayArgument();
	CArrayArgument* pParentNames=functionM.addArrayArgument();
	CArrayArgument* pAllPropertyValues=functionM.addArrayArgument();
	CArrayArgument* pGuids=functionM.addArrayArgument();
	CArrayArgument* pColorSchemeXMLs=functionM.addArrayArgument();

	m_bApplyCanvasColorTheme=FALSE;
	AddColorSchemesFromDom(pIUDom);
	IXMLDOMDocument2Ptr pDom=pIUDom;
	std::map<_bstr_t,_bstr_t> idMap;
	MSXML2::IXMLDOMNodeListPtr nodelist;
	nodelist = pDom->selectNodes("//component");
	int n = nodelist->Getlength();
	for(int i=0;i<n;i++)
	{
		MSXML2::IXMLDOMNodePtr pCompNode= nodelist->Getitem(i);
		MSXML2::IXMLDOMNamedNodeMapPtr map = pCompNode->Getattributes();
		MSXML2::IXMLDOMAttributePtr atrb = map->getNamedItem("className");
		_bstr_t className=atrb->Getvalue();
		MSXML2::IXMLDOMAttributePtr atrb1 = map->getNamedItem("styleName");
		_bstr_t styleName=atrb1->Getvalue();
		MSXML2::IXMLDOMAttributePtr atrb2 = map->getNamedItem("id");
		_bstr_t compID=atrb2->Getvalue();
		CString str=className;
		//CFunction function( _T("addComponent"), _T("xml"));
		//function.addArgument( className );
		pTypes->add(className);
		MSXML2::IXMLDOMNodePtr pCompParent = pCompNode->GetparentNode();
		_bstr_t bsParent="";
		if(pCompParent!=NULL)
		{
			MSXML2::IXMLDOMNamedNodeMapPtr mapParent = pCompParent->Getattributes();
			MSXML2::IXMLDOMAttributePtr atrbID = mapParent->getNamedItem("id");
			if(atrbID)
			{
				bsParent=atrbID->Getvalue();
				if(!bSameIDs)
				{
					bsParent=idMap[bsParent];
				}
			}
		}
		//function.addArgument(bsParent);
		pParentNames->add(bsParent);
		CArrayArgument* pArgs = new CArrayArgument();
		CString csStyleName=styleName;
		if(!csStyleName.IsEmpty())
		{
			CObjectArgument *pObjArg = new CObjectArgument();
			pObjArg->set( _T("id"), _T("styleName"));
			pObjArg->set( _T("value"), csStyleName );
			pArgs->add( pObjArg );

		}
		MSXML2::IXMLDOMNodePtr pPropertiesNode = pCompNode->selectSingleNode( "properties" );
		if(pPropertiesNode)
		{

			MSXML2::IXMLDOMNodeListPtr pChildList=pPropertiesNode->GetchildNodes();
			int nProp=pChildList->Getlength();
			for(int j=0;j<nProp;j++)
			{
				MSXML2::IXMLDOMNodePtr pPropNode= pChildList->Getitem(j);
				MSXML2::IXMLDOMNodePtr pNameNode = pPropNode->selectSingleNode("name");
				_bstr_t bstrName=pNameNode->Gettext();
				MSXML2::IXMLDOMNodePtr pValueNode = pPropNode->selectSingleNode("value");
				MSXML2::IXMLDOMNodePtr pNodeValueObject;
				pValueNode->get_firstChild(&pNodeValueObject);
				CComBSTR bstrValue;
				pNodeValueObject->get_xml( &bstrValue );
				CArgument *pArgValue = CArgument::Load( bstrValue );
				//add 10 to coordinates when copying comps to canvas (only to the first level components)
				if(m_bAddToCmpDoc && bsParent.length()==0)
				{
					CString temp=(TCHAR*)bstrName;
					if(temp==_T("x")||temp==_T("y"))
					{
						if(pArgValue->getType()==CArgument::number)
						{
							CNumberArgument* pCoordArg=(CNumberArgument*)pArgValue;
							double coord=pCoordArg->getValue();
							pCoordArg->setValue(coord+10);
						}
					}
				}
				CObjectArgument *pObjArg = new CObjectArgument();
				pObjArg->set( _T("id"), bstrName);
				pObjArg->set( _T("value"), pArgValue );
				pArgs->add( pObjArg );
			}
		}
		if(pPropertiesNode || !csStyleName.IsEmpty())
		{
			//function.addArgument(pArgs);
			pAllPropertyValues->add(pArgs);
		}
		else
		{
			CArgument* pNullArg = new CNullArgument();
			//function.addArgument(pNullArg);
			pAllPropertyValues->add(pNullArg);
		}
		CString newID=GenerateGUID();
		pGuids->add(newID);
		idMap[compID]=newID;

		///apply color theme
		MSXML2::IXMLDOMNodePtr pColorSchemeNode = pCompNode->selectSingleNode("colorScheme");
		CString colorXML=_T("");
		if(pColorSchemeNode)
		{
			// find the xml for this scheme and call ApplyColorThemeToComponent
			CString colorSchemeID=pColorSchemeNode->Gettext();
			m_csCopyColorThemeName=colorSchemeID;
			CString colorSchemeXml=Document->colorSchemesXMLMap[colorSchemeID];
			if(!colorSchemeXml.IsEmpty() && !colorSchemeID.IsEmpty())
			{
				ColorTheme theme(colorSchemeXml,m_pColorSchemeDom);
				colorXML=theme.getCSS(TRUE);
				//function.addArgument();//last argument seems to cause problems with pasting twice
				pColorSchemeXMLs->add(colorXML);

			}
		}
	}
	CString request;
	functionM.saveString(&request);
	_bstr_t retval;
	try
	{
		retval = GetCanvasView()->GetFlashObject()->CallFunction(request);
	}
	catch(...)
	{
		((CMainFrame*)(::AfxGetMainWnd()))->LogDebug(_T("CXcelsiusDoc::CopyComponentsFromDom function failed"));
		TRACE0("call function failed");
	}
	CopyStylesAndBindingsFromDom(pIUDom,idMap);
}

void CXcelsiusDoc::CopyStylesAndBindingsFromDom(IUnknownPtr pIUDom,std::map<_bstr_t,_bstr_t>& idMap)
{
	IXMLDOMDocument2Ptr pDom=pIUDom;
	MSXML2::IXMLDOMNodeListPtr nodelist;
	nodelist = pDom->selectNodes("//component");
	int n = nodelist->Getlength();
	for(int i=0;i<n;i++)
	{
		MSXML2::IXMLDOMNodePtr pCompNode= nodelist->Getitem(i);
		MSXML2::IXMLDOMNamedNodeMapPtr map = pCompNode->Getattributes();
		MSXML2::IXMLDOMAttributePtr atrb2 = map->getNamedItem("id");
		_bstr_t compID=atrb2->Getvalue();
		_bstr_t instanceID=idMap[compID];
		if(instanceID.length()==0)
		{
			ASSERT(FALSE);
			continue;
		}
		//component styles...is there a way to bootstrap the canvas in a less granular way?
		MSXML2::IXMLDOMNodePtr pStylesNode = pCompNode->selectSingleNode("./styles");
		if(pStylesNode)
		{
			MSXML2::IXMLDOMNodeListPtr stylesList = pStylesNode->selectNodes("./property");
			int nProp=stylesList->Getlength();
			for(int j=0;j<nProp;j++)
			{
				MSXML2::IXMLDOMNodePtr pPropNode= stylesList->Getitem(j);
				MSXML2::IXMLDOMNodePtr pNameNode = pPropNode->selectSingleNode("name/string");
				_bstr_t bstrName=pNameNode->Gettext();

				MSXML2::IXMLDOMNodePtr pValueNode = pPropNode->selectSingleNode("value");
				MSXML2::IXMLDOMNodePtr pValueElement = pValueNode->GetfirstChild();
				_bstr_t bstrValXML = pValueElement->Getxml();

				GetCanvasView()->ModifyComponentStyle(instanceID, bstrName, bstrValXML);
			}

		}
		//when copy pasting, we should copy the styles and properties to the doc, as Canvas does not send us notifications about their changes
		IXMLDOMDocument2Ptr pMainDom = Document->GetXmlDom();
		MSXML2::IXMLDOMNodePtr compNode = pMainDom->selectSingleNode("//component[@id='" + _bstr_t(instanceID) + "']");
		if(pStylesNode)
		{
			compNode->appendChild(pStylesNode->cloneNode(VARIANT_TRUE));
		}
		MSXML2::IXMLDOMNodePtr propNode=compNode->selectSingleNode("properties");//main domain properties
		MSXML2::IXMLDOMNodePtr pPropertiesNode = pCompNode->selectSingleNode( "properties" );//copied properties

		compNode->replaceChild(pPropertiesNode->cloneNode(VARIANT_TRUE),propNode);
		//copy persist information
		MSXML2::IXMLDOMNodePtr pPersistNode = pCompNode->selectSingleNode("Persist");
		if(pPersistNode)
		{
			compNode->appendChild(pPersistNode->cloneNode(VARIANT_TRUE));
		}
		MSXML2::IXMLDOMNodePtr pAssetsNode = pCompNode->selectSingleNode("assets");
		if(pAssetsNode)
		{
			compNode->appendChild(pAssetsNode->cloneNode(VARIANT_TRUE));
		}
		////now create bindings
		//Get list of bindings
		MSXML2::IXMLDOMNodePtr pBindingsNode = pCompNode->selectSingleNode("./bindings");
		if ( pBindingsNode )
		{
			MSXML2::IXMLDOMNodeListPtr pBindingsList = pBindingsNode->GetchildNodes();//pCompNode->selectNodes("./bindings");
			if(pBindingsList)
			{
				//Loop through all bindings
				MSXML2::IXMLDOMNodePtr pBindNode = pBindingsList->nextNode();
				int len = pBindingsList->Getlength();
				int j = 0;
				while (j<len)
				{
					//Get bindings xml
					_bstr_t bindingXML = pBindNode->Getxml();
					if(m_bAddToCmpDoc) //it means we are copying
					CreateNewBindingID(bindingXML);
					//Construct bindings xml
					bindingXML = "<component id=\"" + instanceID + "\"><onrequestbindings>" + bindingXML + "</onrequestbindings></component>";
					//Call OnBinding method
					this->OnBinding(bindingXML);
					//Get next node
					pBindNode = pBindingsList->nextNode();
					//Increment counter
					j++;
				}
			}
		}

	}
	m_bApplyCanvasColorTheme=TRUE;

}

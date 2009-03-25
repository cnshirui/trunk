package xcelsius.online.compdoc
{
	public interface IDocument
	{
		// Component apis
		function AddConnection(String className, String classID, String displayName):Boolean;
		function AddComponent(String className, String classID, String displayName, String styleName, String colorSchemeName, Boolean rename=true):Boolean;
		function AddFont(String fontName, int charSet, int charSel, Boolean embed):Boolean;
		function GetFont():Boolean;
		function RemoveFont():Boolean;
		function GetComponent(String instanceID, cmpComponent comp, Boolean bCompIsConnection=false):Boolean;
		function FindComponentByProperty(String className, String propertyName, String propertyValueXML, Boolean bCompIsConn):Boolean;
		function SaveComponent(cmpComponent comp, int pChanged=NULL):Boolean;
		function DeleteAllConnections():void;
		function ApplyColorSchemeToExistingComponents(String csColorScheme):Array;
		function SetCurrentColorScheme(String csColorScheme):void;
		function GetCurrentColorSchemeID():String;
		function SetCurrentSkin(String csSkin):void;
		function GetCurrentSkin():String;
		function SetComponentColorScheme(String compID,String csColorSchemeID):void;
		function SetMetaData(String compID,String tag, String data):Boolean;
		function ClearMetaData(String compID,String tag):Boolean;
		function GetParentGroup(String compID):String;
		function IsLastMemberOfGroup(String compID):Boolean;
		function GetBindingInfo(String compID, String propName):Dictionary;
		function Reset():void;
		function IsComponentLocked(String compID):Boolean;
		function IsComponentVisible(String compID):Boolean;
		function AddGroupMembersToList(Array list, Array result):void;
		function IsGroup(String compID):Boolean;
		
		// used by migration
		function UpdateComponentXML(String instanceID, String compXML):Boolean;
		function UpdateBindingXML(String instanceID, String bindXML):Boolean;
		function UpdateStyleXML(String instanceID, String styleXML):Boolean;
		function UpdateAssetXML(String instanceID, String assetXML):Boolean;
		function UpdatePersistXML(String instanceID, String persistXML):Boolean;
		
		// used by binding
		function HasConnectionsOfClass(String className):Boolean;
		function UpdateComponentXML(String compXML);
		function CreateCanvasBinding(String transXML, IBinding binding):Boolean;
		function UpdateCanvasBinding(String transXML, IBinding binding):Boolean;
		function SetBinding(String transXML, Boolean bRemoveThisBinding=false):Boolean;
		function GetBinding(String compID, String bindingId, String valueXML):Boolean;
		function RemoveBinding(Array guids, String id):Arrar;		//returns array of binding ids to be removed
		function GetBindingsForProperty(String instanceID, String propName):Arrar;
		function GetColorSchemeNames():Arrar;
		function SaveColorSchemes(Dictionary xmlMap):void;
		function LoadColorSchemes():void;
		
		// for SWF external assets
		function ConvertFiles(String filename, String tempFile, Boolean embed=false):Boolean;
		function SetAsset(String compID, String propName, String fileName, Boolean embed, String valueXML):Boolean;
		function SetCanvasAsset(String propName, String fileName, Boolean embed, String valueXML):Boolean;
		function SetAssets(String compID, String propName, CArrayArgument fileName, CArrayArgument embed, String valueXML):Boolean;
		function SetEmbed(String compID, String propName, Boolean embed):Boolean;
		function GetConnectionsROL():String;
		
		//util functions
		function CreateAssetValue(String tempFile, String origFile, String embed, MSXML2::IXMLDOMElementPtr assetNode):void;
		function CreateAssetArray(CArrayArgument filenames, CArrayArgument embed, MSXML2::IXMLDOMElementPtr lastNode=NULL ):Boolean;
		function CreateAssetProperty(String assetNode, MSXML2::IXMLDOMElementPtr lastNode=NULL):String;
		function GetNote():String;
		function SetNote(String csNote):void;
		function IsStyleProtected(String styleName):Boolean;
		function IsColorStyle(String styleName):Boolean;
		function GetAsset(String compID):String;
		function GetAssets(String compID, String propName):String;
		function ClearCanvasStyles():Boolean;
		function ClearAllComponentStyles():void;
		function SetDisplayName(String compID, String displayName):void;
		function GetColorScheme(String id):String;
	
		// Temporary for SWF gen
		function GetComponentCount():int;
		function GetComponentID(int idx):String;
		function GetConnectionCount():int;
		function GetConnectionID(int idx):String;
		
		//End temporary for SWF gen
		function ConvertTransXML(String transXML):String;
	
		// Document apis
		function GenerateName(String templateStr):String;
		function IsNameUsedAlready(String csName):Boolean;
		function Save(String fname):Boolean;
		function Load(String fname):Boolean;
		function DeleteComponent(String instanceID, Boolean bCompIsConnection=false):Boolean;
		function ShowComponent(String instanceID, Boolean bShow):Boolean;
		function LockComponent(String instanceID, Boolean bLock):Boolean;
		function ShowAllComponents(Boolean bShow):Boolean;
		function LockAllComponents(Boolean bLock):Boolean;
		function GetComponentProperty(String compID, String propName, String valueXML):Boolean;
		function ModifyComponentProperty(String compID, String propName, String valueXML, String oldValXML, Boolean bCompIsConn=false):Boolean;
		function ModifyCanvasStyle(String styleName, String valueXML):Boolean;
		function ModifyComponentStyle(String compID, String styleName, String valueXML, Boolean bGlobal):Boolean;
		function ClearComponentStyle(String compID, String style):Boolean;
		function MoveComponentsUp(Array comps):Boolean;
		function MoveComponentsDown(Array comps):Boolean;
		function MoveComponentUp(String compID):Boolean;
		function MoveComponentDown(String compID):Boolean;
		function BringComponentToFront(String compID):Boolean;
		function SendComponentToBack(String compID):Boolean;
		function ChangeComponentDepth(String compID, int nLevelsUp):Boolean;
		function ReparentComponent(String compID, String newParentID):Boolean;
		function GroupComponents(Array comps, String newGroupID, String newGroupName):void;
		function UngroupComponents(Array groups):void;
		function GetCanvasSize(int nWidth, int nHeight):void;
		function SetCanvasSize(int nWidth, int nHeight):void;
		function CreateCSSStyleDeclaration(String guid):void;
		function SetPersist(String compID, String xmlString):Boolean;
		function GetPersist(String compID):String;
		function GetXmlDom() { return pXmlDOM;}
		function SetExportSettings(Boolean bUseCurrent, String path):void;
		function GetExportSettings(bool bUseCurrent, String path):Boolean;
		function InitDocument():void;		// made public for xlf migration
		function GetAssets(Array assets, Boolean save=false):int;
		function GetExternalAssetCount():int;
		function FixAssetPaths(Dictionary assetPath):void;
		function GetDocGUID();
		function RemoveConnectionsOfClass(String className):int;
		function ChangeComponentLevel(String compID, int newLevel):void;
		function GetLiveOfficeCuids():Arrar;
		function LoadAssetMap():void;
		function SetImageWarning(String msg):void;		//{warningImage=msg;}
		function UnbindProperty(Array compIds, String propName):Arrar;//returns list of binding ids to unbind
		function GetImageWarning():Boolean;
		function SetWarningTitle(String title):void;
		
		// used by AIR
//		function GetAirSettings(AirSettings setting):Boolean;		// GV
//		function GetAirWindowSize(int nWidth, int nHeight):void;
//		function SetAirSettings(AirSettings setting):void;		// GV
		
		function buildAssetsItem(XML item):CObjectArgument;
		function modifyStyle(XML item, String styleName, String valueXML):Boolean;
		function setAsset(XML assetsNode, String propName, String originalFileName, String tmpFileName, Boolean embed):void;
		function GetSize(String fn, String warningImage):Boolean;
	}
}
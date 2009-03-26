package xcelsius.online.components.compdoc
{
        public interface IDocument
        {
        	import flash.utils.Dictionary;
        	
            // Component apis
            function AddConnection(className:String, classID:String, displayName:String):Boolean;
            function AddComponent(className:String, classID:String, displayName:String, styleName:String, colorSchemeName:String, rename:Boolean = true):Boolean;
            function AddFont(fontName:String, charSet:int, charSel:int, embed:Boolean):Boolean;
            function GetFont():Boolean;
            function RemoveFont():Boolean;
            function GetComponent(instanceID:String, comp:Component, bCompIsConnection:Boolean = false):Boolean;
            function FindComponentByProperty(className:String, propertyName:String, propertyValueXML:String, bCompIsConn:Boolean):Boolean;
            function SaveComponent(comp:Component, pChanged:int = 0):Boolean;
            function DeleteAllConnections():void;
            function ApplyColorSchemeToExistingComponents(csColorScheme:String):Array;
            function SetCurrentColorScheme(csColorScheme:String):void;
            function GetCurrentColorSchemeID():String;
            function SetCurrentSkin(csSkin:String):void;
            function GetCurrentSkin():String;
            function SetComponentColorScheme(compID:String, csColorSchemeID:String):void;
            function SetMetaData(compID:String, tag:String, data:String):Boolean;
            function ClearMetaData(compID:String, tag:String):Boolean;
            function GetParentGroup(compID:String):String;
            function IsLastMemberOfGroup(compID:String):Boolean;
            function GetBindingInfo(compID:String, propName:String):Dictionary;
            function Reset():void;
            function IsComponentLocked(compID:String):Boolean;
            function IsComponentVisible(compID:String):Boolean;
            function AddGroupMembersToList(list:Array, result:Array):void;
            function IsGroup(compID:String):Boolean;
            
            // used by migration
            function UpdateComponentXMLWithID(instanceID:String, compXML:String):Boolean;
            function UpdateBindingXML(instanceID:String, bindXML:String):Boolean;
            function UpdateStyleXML(instanceID:String, styleXML:String):Boolean;
            function UpdateAssetXML(instanceID:String, assetXML:String):Boolean;
            function UpdatePersistXML(instanceID:String, persistXML:String):Boolean;
            
            // used by binding
            function HasConnectionsOfClass(className:String):Boolean;
            function UpdateComponentXML(compXML:String):Boolean;
            function CreateCanvasBinding(transXML:String, binding:Binding):Boolean;
            function UpdateCanvasBinding(transXML:String, binding:Binding):Boolean;
            function SetBinding(transXML:String, bRemoveThisBinding:Boolean = false):Boolean;
            function GetBinding(compID:String, bindingId:String, valueXML:String):Boolean;
            function RemoveBinding(guids:Array, id:String):Array;                //returns array of binding ids to be removed
            function GetBindingsForProperty(instanceID:String, propName:String):Array;
            function GetColorSchemeNames():Array;
            function SaveColorSchemes(xmlMap:Dictionary):void;
            function LoadColorSchemes():void;
            
            // for SWF external assets
            function ConvertFiles(filename:String, tempFile:String, embed:Boolean = false):Boolean;
            function SetAsset(compID:String, propName:String, fileName:String, embed:Boolean, valueXML:String):Boolean;
            function SetCanvasAsset(propName:String, fileName:String, embed:Boolean, valueXML:String):Boolean;
            function SetAssets(compID:String, propName:String, fileName:ArrayArgument, embed:ArrayArgument, valueXML:String):Boolean;
            function SetEmbed(compID:String, propName:String, embed:Boolean):Boolean;
            function GetConnectionsROL():String;
            
            //util functions
            function CreateAssetValue(tempFile:String, origFile:String, embed:String, assetNode:XML):void;
            function CreateAssetArray(filenames:ArrayArgument, embed:ArrayArgument, lastNode:XML = null):Boolean;
            function CreateAssetProperty(assetNode:String, lastNode:XML = null):String;
            function GetNote():String;
            function SetNote(csNote:String):void;
            function IsStyleProtected(styleName:String):Boolean;
            function IsColorStyle(styleName:String):Boolean;
            function GetAsset(compID:String):String;
            function GetAssetsWithName(compID:String, propName:String):String;
            function ClearCanvasStyles():Boolean;
            function ClearAllComponentStyles():void;
            function SetDisplayName(compID:String, displayName:String):void;
            function GetColorScheme(id:String):String;
    
            // Temporary for SWF gen
            function GetComponentCount():int;
            function GetComponentID(idx:int):String;
            function GetConnectionCount():int;
            function GetConnectionID(idx:int):String;
            
            //End temporary for SWF gen
            function ConvertTransXML(transXML:String):String;
    
            // Document apis
            function GenerateName(templateStr:String):String;
            function IsNameUsedAlready(csName:String):Boolean;
            function Save():String;
            function Load(document:XML):Boolean;
            function DeleteComponent(instanceID:String, bCompIsConnection:Boolean = false):Boolean;
            function ShowComponent(instanceID:String, bShow:Boolean):Boolean;
            function LockComponent(instanceID:String, bLock:Boolean):Boolean;
            function ShowAllComponents(bShow:Boolean):Boolean;
            function LockAllComponents(bLock:Boolean):Boolean;
            function GetComponentProperty(compID:String, propName:String, valueXML:String):Boolean;
            function ModifyComponentProperty(compID:String, propName:String, valueXML:String, oldValXML:String, bCompIsConn:Boolean = false):Boolean;
            function ModifyCanvasStyle(styleName:String, valueXML:String):Boolean;
            function ModifyComponentStyle(compID:String, styleName:String, valueXML:String, bGlobal:Boolean):Boolean;
            function ClearComponentStyle(compID:String, style:String):Boolean;
            function MoveComponentsUp(comps:Array):Boolean;
            function MoveComponentsDown(comps:Array):Boolean;
            function MoveComponentUp(compID:String):Boolean;
            function MoveComponentDown(compID:String):Boolean;
            function BringComponentToFront(compID:String):Boolean;
            function SendComponentToBack(compID:String):Boolean;
            function ChangeComponentDepth(compID:String, nLevelsUp:int):Boolean;
            function ReparentComponent(compID:String, newParentID:String):Boolean;
            function GroupComponents(comps:Array, newGroupID:String, newGroupName:String):void;
            function UngroupComponents(groups:Array):void;
            function GetCanvasSize(nWidth:int, nHeight:int):void;
            function SetCanvasSize(nWidth:int, nHeight:int):void;
            function CreateCSSStyleDeclaration(guid:String):void;
            function SetPersist(compID:String, xmlString:String):Boolean;
            function GetPersist(compID:String):String;
            function GetXmlDom():XML;
            function SetExportSettings(bUseCurrent:Boolean, path:String):void;
            function GetExportSettings(bUseCurrent:Boolean, path:String):Boolean;
            function InitDocument():void;                // made public for xlf migration
            function GetAssets(assets:Array, save:Boolean = false):int;
            function GetExternalAssetCount():int;
            function FixAssetPaths(assetPath:Dictionary):void;
            function GetDocGUID():String;
            function RemoveConnectionsOfClass(className:String):int;
            function ChangeComponentLevel(compID:String, newLevel:int):void;
            function GetLiveOfficeCuids():Array;
            function LoadAssetMap():void;
            function SetImageWarning(msg:String):void;                //{warningImage=msg;}
            function UnbindProperty(compIds:Array, propName:String):Array;//returns list of binding ids to unbind
            function GetImageWarning():Boolean;
            function SetWarningTitle(title:String):void;
            
            // used by AIR
	//		function GetAirSettings(setting:AirSettings):Boolean;		// GV
	//		function GetAirWindowSize(nWidth:int, nHeight:int):void;
	//		function SetAirSettings(setting:AirSettings):void;		// GV
            
            function buildAssetsItem(item:XML):ObjectArgument;
            function modifyStyle(item:XML, styleName:String, valueXML:String):Boolean;
            function setAsset(assetsNode:XML, propName:String, originalFileName:String, tmpFileName:String, embed:Boolean):void;
            function GetSize(fn:String, warningImage:String):Boolean;
        }
}
package xcelsius.online.components
{
	import flash.utils.Dictionary;
	import mx.utils.UIDUtil;
	
	import xcelsius.external.ExternalInterfaceCodec;
	import xcelsius.online.components.compdoc.ArrayArgument;
	import xcelsius.online.components.compdoc.Binding;
	import xcelsius.online.components.compdoc.Component;
	import xcelsius.online.components.compdoc.IDocument;
	import xcelsius.online.components.compdoc.ObjectArgument;

	
	public class XDocument implements IDocument
	{
		public var colorSchemesXMLMap:Dictionary;
		public var m_undoMap:Dictionary;
		
		private var pAssetDoc:XML;
		private var pAssetProp:XML;
		private var assetENode:XML;
		private var assetsFromXlf:Dictionary;
		private var assetMapped:Dictionary;
		private var assetsXLF:Array;
	
		protected var pXmlDOM:XML;
		protected var DocGUID:String;
		protected var warningImage:String;
		protected var m_csWarningTitle:String;
		
		public function XDocument()
		{
			// protect the xml, only document itself can change the xml
			pXmlDOM = new XML();
			pAssetDoc = null;
			pAssetProp = null;
			
			//Initialize the xml doc
			InitDocument();
			warningImage = "";			
		}
		
		public function get documentXml():XML{
			// protect the xml, only document itself can change the xml
			return this.pXmlDOM.copy();
		}
		
		public function get documentXmlStr():String{
			// protect the xml, only document itself can change the xml
			return this.pXmlDOM.toXMLString()
		}
		
		public function get canvasWidth():Number{
			if(this.pXmlDOM){
				//- <CXCanvas guid="2202450108-26755-4573-184-180-23921125186053" width="800" height="600">
				return Number(this.pXmlDOM.@width);
			}
			return -1;
		}
		
		public function get canvasHeight():Number{
			if(this.pXmlDOM){
				return Number(this.pXmlDOM.@height);
			}
			return -1;
		}
		
		
		private var canvasStylesArray:Array = [];
		
		public function get canvasStyles():Array{
			
			if(canvasStylesArray.length == 0 && this.pXmlDOM){
				trace("get canvasStyles");
				var stylesXmlList:XMLList = this.pXmlDOM.canvas.styles;
				if(stylesXmlList.length() > 0){
					canvasStylesArray = parsePropertyXmlToObject(stylesXmlList[0]);
				}
			}
			return canvasStylesArray;
		}
		
		public function changeCanvasSizeHandler(width:Number, height:Number, relative:Boolean = true):void{
			if(relative){
				this.pXmlDOM.@width = Number(this.pXmlDOM.@width) + width;
				this.pXmlDOM.@height = Number(this.pXmlDOM.@height) + height;
			}else{
				this.pXmlDOM.@width = width;
				this.pXmlDOM.@height = height;
			}
		}
		
		public static function parsePropertyXmlToObject(propertiesXML:XML):Array{
			/*
			<anyNode>
				<property>
				- <name>
				  <string>backgroundImage</string> 
				  </name>
				- <value>
				  <null /> | <string /> | <number />
				  </value>
				</property>
			</anyNode>
			*/
			
			/*
			arr	Array (@1add8939)	
				[0]	Object (@17d18e71)	
					name	"data"	
					value	Array (@1add8971)	
						[0]	Array (@1add89e1)	
							[0]	"13"	
							[1]	"7"	
							[2]	"5"	
							[3]	"3"	
							[4]	"3"	
							length	5	
						length	1	
				length	1	
			
				[1]	Object (@183068d1)	
					name	"titleNumberFormat"	
					value	null	
			
				[4]	Object (@182fa7e1)	
					name	"title"	
					value	"Pie Chart"	
					*/
			
			// refer to XLFManager.addComponentsFromDOM
			
			var properties:Array = [];
			
			var propertyList:XMLList = propertiesXML.children();
			
			var counts:int = propertyList.length();
			
			for (var i:Number = 0; i < counts; i++) {
				var property:Object = new Object();
				var propertyNode:XML = propertyList[i];
				
				property.name = propertyNode.name.string.toString();
									
				var rawValue:XML = propertyNode.value[0].children()[0];
				property.value = ExternalInterfaceCodec.decode(rawValue);
				
				properties.push(property);
			}
			
			return properties;
		}
		
//------ the follow are ported from Cpp -------------------------------------------------------------		
		
		public function AddConnection(className:String, classID:String, displayName:String):Boolean
		{
			return false;
		}
		
		public function AddComponent(className:String, classID:String, displayName:String, styleName:String, colorSchemeName:String, rename:Boolean=true):Boolean
		{
			return false;
		}
		
		public function AddFont(fontName:String, charSet:int, charSel:int, embed:Boolean):Boolean
		{
			return false;
		}
		
		public function GetFont():Boolean
		{
			return false;
		}
		
		public function RemoveFont():Boolean
		{
			return false;
		}
		
		public function GetComponent(instanceID:String, comp:Component, bCompIsConnection:Boolean=false):Boolean
		{
			return false;
		}
		
		public function FindComponentByProperty(className:String, propertyName:String, propertyValueXML:String, bCompIsConn:Boolean):Boolean
		{
			return false;
		}
		
		public function SaveComponent(comp:Component, pChanged:int=0):Boolean
		{
			return false;
		}
		
		public function DeleteAllConnections():void
		{
		}
		
		public function ApplyColorSchemeToExistingComponents(csColorScheme:String):Array
		{
			return null;
		}
		
		public function SetCurrentColorScheme(csColorScheme:String):void
		{
		}
		
		public function GetCurrentColorSchemeID():String
		{
			return null;
		}
		
		public function SetCurrentSkin(csSkin:String):void
		{
		}
		
		public function GetCurrentSkin():String
		{
			return null;
		}
		
		public function SetComponentColorScheme(compID:String, csColorSchemeID:String):void
		{
		}
		
		public function SetMetaData(compID:String, tag:String, data:String):Boolean
		{
			return false;
		}
		
		public function ClearMetaData(compID:String, tag:String):Boolean
		{
			return false;
		}
		
		public function GetParentGroup(compID:String):String
		{
			return null;
		}
		
		public function IsLastMemberOfGroup(compID:String):Boolean
		{
			return false;
		}
		
		public function GetBindingInfo(compID:String, propName:String):Dictionary
		{
			return null;
		}
		
		public function Reset():void
		{
		}
		
		public function IsComponentLocked(compID:String):Boolean
		{
			return false;
		}
		
		public function IsComponentVisible(compID:String):Boolean
		{
			return false;
		}
		
		public function AddGroupMembersToList(list:Array, result:Array):void
		{
		}
		
		public function IsGroup(compID:String):Boolean
		{
			return false;
		}
		
		public function UpdateComponentXMLWithID(instanceID:String, compXML:String):Boolean
		{
			return false;
		}
		
		public function UpdateBindingXML(instanceID:String, bindXML:String):Boolean
		{
			return false;
		}
		
		public function UpdateStyleXML(instanceID:String, styleXML:String):Boolean
		{
			return false;
		}
		
		public function UpdateAssetXML(instanceID:String, assetXML:String):Boolean
		{
			return false;
		}
		
		public function UpdatePersistXML(instanceID:String, persistXML:String):Boolean
		{
			return false;
		}
		
		public function HasConnectionsOfClass(className:String):Boolean
		{
			return false;
		}
		
		public function UpdateComponentXML(compXML:String):Boolean
		{
			return false;
		}
		
		public function CreateCanvasBinding(transXML:String, binding:Binding):Boolean
		{
			return false;
		}
		
		public function UpdateCanvasBinding(transXML:String, binding:Binding):Boolean
		{
			return false;
		}
		
		public function SetBinding(transXML:String, bRemoveThisBinding:Boolean=false):Boolean
		{
			return false;
		}
		
		public function GetBinding(compID:String, bindingId:String, valueXML:String):Boolean
		{
			return false;
		}
		
		public function RemoveBinding(guids:Array, id:String):Array
		{
			return null;
		}
		
		public function GetBindingsForProperty(instanceID:String, propName:String):Array
		{
			return null;
		}
		
		public function GetColorSchemeNames():Array
		{
			return null;
		}
		
		public function SaveColorSchemes(xmlMap:Dictionary):void
		{
		}
		
		public function LoadColorSchemes():void
		{
		}
		
		public function ConvertFiles(filename:String, tempFile:String, embed:Boolean=false):Boolean
		{
			return false;
		}
		
		public function SetAsset(compID:String, propName:String, fileName:String, embed:Boolean, valueXML:String):Boolean
		{
			return false;
		}
		
		public function SetCanvasAsset(propName:String, fileName:String, embed:Boolean, valueXML:String):Boolean
		{
			return false;
		}
		
		public function SetAssets(compID:String, propName:String, fileName:ArrayArgument, embed:ArrayArgument, valueXML:String):Boolean
		{
			return false;
		}
		
		public function SetEmbed(compID:String, propName:String, embed:Boolean):Boolean
		{
			return false;
		}
		
		public function GetConnectionsROL():String
		{
			return null;
		}
		
		public function CreateAssetValue(tempFile:String, origFile:String, embed:String, assetNode:XML):void
		{
		}
		
		public function CreateAssetArray(filenames:ArrayArgument, embed:ArrayArgument, lastNode:XML=null):Boolean
		{
			return false;
		}
		
		public function CreateAssetProperty(assetNode:String, lastNode:XML=null):String
		{
			return null;
		}
		
		public function GetNote():String
		{
			return null;
		}
		
		public function SetNote(csNote:String):void
		{
		}
		
		public function IsStyleProtected(styleName:String):Boolean
		{
			return false;
		}
		
		public function IsColorStyle(styleName:String):Boolean
		{
			return false;
		}
		
		public function GetAsset(compID:String):String
		{
			return null;
		}
		
		public function GetAssetsWithName(compID:String, propName:String):String
		{
			return null;
		}
		
		public function ClearCanvasStyles():Boolean
		{
			return false;
		}
		
		public function ClearAllComponentStyles():void
		{
		}
		
		public function SetDisplayName(compID:String, displayName:String):void
		{
		}
		
		public function GetColorScheme(id:String):String
		{
			return null;
		}
		
		public function GetComponentCount():int
		{
			return 0;
		}
		
		public function GetComponentID(idx:int):String
		{
			return null;
		}
		
		public function GetConnectionCount():int
		{
			return 0;
		}
		
		public function GetConnectionID(idx:int):String
		{
			return null;
		}
		
		public function ConvertTransXML(transXML:String):String
		{
			return null;
		}
		
		public function GenerateName(templateStr:String):String
		{
			return null;
		}
		
		public function IsNameUsedAlready(csName:String):Boolean
		{
			return false;
		}
		
		public function Save():String
		{
			return pXmlDOM.toXMLString();
		}
		
		public function Load(document:XML):Boolean
		{
			pXmlDOM = document.copy();
			
			var attrs:XMLList = pXmlDOM.attribute("guid")
			if(attrs.length()!=0)
			{
				DocGUID = attrs;
				return true;
			}
			
			return false;	
		}
		
		public function DeleteComponent(instanceID:String, bCompIsConnection:Boolean=false):Boolean
		{
			return false;
		}
		
		public function ShowComponent(instanceID:String, bShow:Boolean):Boolean
		{
			return false;
		}
		
		public function LockComponent(instanceID:String, bLock:Boolean):Boolean
		{
			return false;
		}
		
		public function ShowAllComponents(bShow:Boolean):Boolean
		{
			return false;
		}
		
		public function LockAllComponents(bLock:Boolean):Boolean
		{
			return false;
		}
		
		public function GetComponentProperty(compID:String, propName:String, valueXML:String):Boolean
		{
			return false;
		}
		
		public function ModifyComponentProperty(compID:String, propName:String, valueXML:String, oldValXML:String, bCompIsConn:Boolean=false):Boolean
		{
			return false;
		}
		
		public function ModifyCanvasStyle(styleName:String, valueXML:String):Boolean
		{
			return false;
		}
		
		public function ModifyComponentStyle(compID:String, styleName:String, valueXML:String, bGlobal:Boolean):Boolean
		{
			return false;
		}
		
		public function ClearComponentStyle(compID:String, style:String):Boolean
		{
			return false;
		}
		
		public function MoveComponentsUp(comps:Array):Boolean
		{
			return false;
		}
		
		public function MoveComponentsDown(comps:Array):Boolean
		{
			return false;
		}
		
		public function MoveComponentUp(compID:String):Boolean
		{
			return false;
		}
		
		public function MoveComponentDown(compID:String):Boolean
		{
			return false;
		}
		
		public function BringComponentToFront(compID:String):Boolean
		{
			return false;
		}
		
		public function SendComponentToBack(compID:String):Boolean
		{
			return false;
		}
		
		public function ChangeComponentDepth(compID:String, nLevelsUp:int):Boolean
		{
			return false;
		}
		
		public function ReparentComponent(compID:String, newParentID:String):Boolean
		{
			return false;
		}
		
		public function GroupComponents(comps:Array, newGroupID:String, newGroupName:String):void
		{
		}
		
		public function UngroupComponents(groups:Array):void
		{
		}
		
		public function GetCanvasSize(nWidth:int, nHeight:int):void
		{
		}
		
		public function SetCanvasSize(nWidth:int, nHeight:int):void
		{
		}
		
		public function CreateCSSStyleDeclaration(guid:String):void
		{
		}
		
		public function SetPersist(compID:String, xmlString:String):Boolean
		{
			return false;
		}
		
		public function GetPersist(compID:String):String
		{
			return null;
		}
		
		public function GetXmlDom():XML
		{
			return null;
		}
		
		public function SetExportSettings(bUseCurrent:Boolean, path:String):void
		{
		}
		
		public function GetExportSettings(bUseCurrent:Boolean, path:String):Boolean
		{
			return false;
		}
		
		public function InitDocument():void
		{
			DocGUID = UIDUtil.createUID();
		
			//"<?xml version="1.0" encoding="utf-8"?><CXCanvas></CXCanvas>
			//VARIANT_BOOL loadSuccess = pDom->loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas><HSlider id=\"growthRate\"><x>21</x><y>207</y><minimum>0</minimum><maximum>1</maximum><liveDragging>true</liveDragging><themeColor>haloOrange</themeColor><snapInterval>.01</snapInterval></HSlider></CXCanvas>");
			//pDom->PutpreserveWhiteSpace(VARIANT_FALSE); //cpp file commented by roy shi
			pXmlDOM.ignoreWhitespace = false;
			
		
			//add docxml as attribute
			var docxml:String = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas guid=\"";
			docxml += DocGUID;
			docxml += "\"></CXCanvas>";
		
			//Add default font
		
		//	/* VARIANT_BOOL loadSuccess = */ pDom->loadXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas></CXCanvas>");
			/* VARIANT_BOOL loadSuccess = */ 
			pXmlDOM = new XML(docxml);
		
			SetExportSettings(true, "");
		}
		
		public function GetAssets(assets:Array, save:Boolean=false):int
		{
			return 0;
		}
		
		public function GetExternalAssetCount():int
		{
			return 0;
		}
		
		public function FixAssetPaths(assetPath:Dictionary):void
		{
		}
		
		public function GetDocGUID():String
		{
			return null;
		}
		
		public function RemoveConnectionsOfClass(className:String):int
		{
			return 0;
		}
		
		public function ChangeComponentLevel(compID:String, newLevel:int):void
		{
		}
		
		public function GetLiveOfficeCuids():Array
		{
			return null;
		}
		
		public function LoadAssetMap():void
		{
		}
		
		public function SetImageWarning(msg:String):void
		{
		}
		
		public function UnbindProperty(compIds:Array, propName:String):Array
		{
			return null;
		}
		
		public function GetImageWarning():Boolean
		{
			return false;
		}
		
		public function SetWarningTitle(title:String):void
		{
		}
		
		public function buildAssetsItem(item:XML):ObjectArgument
		{
			return null;
		}
		
		public function modifyStyle(item:XML, styleName:String, valueXML:String):Boolean
		{
			return false;
		}
		
		public function setAsset(assetsNode:XML, propName:String, originalFileName:String, tmpFileName:String, embed:Boolean):void
		{
		}
		
		public function GetSize(fn:String, warningImage:String):Boolean
		{
			return false;
		}		
		
	}
}
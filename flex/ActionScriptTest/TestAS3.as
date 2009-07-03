package
{
	import flash.display.Sprite;

	public class TestAS3 extends Sprite implements MyInterface
	{

		public function TestAS3()
		{
/* 			var array:Array = [];
			array[9] = "a";
			trace(array); 
			
			var a:* = 123;
			trace(a == null);
			var b:* = String(a);
			var c:* = String("123");
			trace(String(a) == String(null) || a == null);
*/
			test();
		}
		
		public function test(b:Boolean = false):void {
			trace(b);
			
		}
	}
}




/*
		private function testStringRef(str:String):void
		{
			str += " ok!";
		}
		
			var s:String = "shirui";
			testStringRef(s);
			trace(s);

			var i:int = 0;
			i = 14/10;
			trace(Math.ceil(14/10));
			i = 19/10;
			trace(i);
			i = 21/10;
			trace(i);
			i = 25/10;
			trace(i);
			i = 5/10;
			trace(i);
			
		private function hashObject(object:Object):String
		{
			var a:Array = [1, 2, null];
			for(var i:int=0; i<5; i++)
			{
				trace(a[i]);
			}			
			var text:TextFormat = new TextFormat();
			var myObject:Object = {firstName:"Tara", age:27, city:"San Francisco"}; 

			trace(hashObject(myObject));
			var value:String= object.toString() + " => Hash \n";
			for(var key:String in object)
			{
				value += key + ": " + object[key] + "\n";
			}
			
			return value;
		}
		
			var s:String = "\\\\\\";
			trace(s);
			var loader:URLLoader = new URLLoader();
			// file://///shg-d-11-rshi/share/中国/
			// file://///shg-d-06-aemon/boost
			// \\shg-d-06-aemon\boost
//            var request:URLRequest = new URLRequest("file:///d:/中国");
            var request:URLRequest = new URLRequest("file://///shg-d-11-rshi/share/中国/");
//            try {
            	navigateToURL(request, "_self");
                loader.load(request);
              } catch (error:Error) {
                trace("Unable to load requested document.");
            } 	 	
            
            var loader:URLLoader = new URLLoader();

			// \\\\shg-d-11-rshi\\share
			// file:///shg-d-11-rshi/share/中国
			// file://shg-d-11-rshi/share/中国
			// file:///d:/中国
//            var request:URLRequest = new URLRequest("file:///d:/中国");
            var request:URLRequest = new URLRequest("file://///shg-d-11-rshi/share/中国/");
//            try {
            	navigateToURL(request, "_self");
                loader.load(request);
             } catch (error:Error) {
                trace("Unable to load requested document.");
            } 
			
			var a:Array = [null, "", " "];
			for(var i:int=0; i<a.length; i++)
			{
				trace(i);
				var o:* = a[i];
				if(o!=null && o.length >= 0)
				{
					trace("OK");
				}
			}

   var xml:XML = <component className="xcelsius.controls.Label" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <properties>
   <property>
   <name>
   <string>
   width
   </string>
   </name>
   <value>
   <number>
   115
   </number>
   </value>
   </property>
   <property>
   <name>
   <string>
   height
   </string>
   </name>
   <value>
   <number>
   50
   </number>
   </value>
   </property>
   <property>
   <name>
   <string>
   defaultText
   </string>
   </name>
   <value>
   <string>
   <locElement>
   <locID>
   LABEL_TEXT
   </locID>
   <locLabel>
   Label Text
   </locLabel>
   </locElement>
   </string>
   </value>
   </property>
   </properties>
   </component>;

   //find elements, override name?
   var node:XML = xml.name..locElement..locLabel[0];

   if(!node)
   node = nodePtr.name..locElement..locString[0];

   if(node)
   uiComp.labelName = node.toString();

   var s:String = "color1";
   var t:String = "color10";

   var xml:XML = <root><color1>color1</color1><color1>1</color1></root>;
   trace(xml);
   var c:XML = xml..*.(* == "color1")[0];
   trace(x);



   var s:String;
   trace(s);
   trace(s == null);
   s = "";
   trace(s);
   trace(s == null);


   var xml:XML = new XML("<Root>\n<a/><string>℃ ©</string><b/></Root>");
   xml.a = null;
   trace(xml.toXMLString());


   var xml:XML = <Root><b/><a/><b/><a/><b/><a/><b/><a/><b/><a/><b/><a/></Root>;
   //			var xml2:XML = <Root><a/><b/></Root>;
   var list:XMLList = xml.b;
   var a:XML = xml.a[0];
   trace(a.parent() == xml);
   trace(xml.toXMLString());


   var a:Array = [1, 2, 3, 4];
   a.splice(0, 2);
   trace(a);


   var s:String = "<array>\n  <property id=\"0\">\n    <number>6777429.000000</number>\n  </property>\n  <property id=\"1\">\n    <number>6777429.000000</number>\n  </property>\n</array>";
   var xml:XML = <Root><a/></Root>;
   delete xml.*;

   trace(xml.toXMLString());


   //			xml.appendChild(s);
   xml.appendChild(<h/>);
   xml.appendChild(<h/>);
   xml.appendChild(<h/>);
   xml.h[0].name = "shirui";
   xml.h[1].call = "jiangsu";
   xml.appendChild(<h>{XML(s)}</h>);


   <Root><home/><apple/></Root>;
   var h:XML = xml.home[0].copy();
   delete xml.children()[xml.home[0].childIndex()];
   xml.appendChild(h);
   xml.a.b.c = "shirui";


   var h:XML = xml.home[0];
   delete xml.children()[0];
   var x:XML = xml.appendChild(<home/>);
   trace(xml.toXMLString());
   trace(h.toXMLString());
   trace(h.childIndex());

   xml.appendChild(<home/>);
   h = xml.home[0];
   xml.insertChildAfter(h, <apple/>);


   var xml:XML = <Root ID="BOE1C30A443E1D4B8C82AE06BA8D9E804A" name="Flow">
   <Colors>
   <color1>
   0x000000
   </color1>
   <color2>
   0xFFFFFF
   </color2>
   <color3>
   0x04617B
   </color3>
   <color4>
   0xDBF5F9
   </color4>
   <color5>
   0x0F6FC6
   </color5>
   <color6>
   0x009DD9
   </color6>
   <color7>
   0x0BD0D9
   </color7>
   <color8>
   0x10CF9B
   </color8>
   <color9>
   0x7CCA62
   </color9>
   <color10>
   0xCCD6A0
   </color10>
   </Colors>
   <StyleDeclarations/>
   </Root>;

   trace(xml.toXMLString());
   trace(xml.@ID);
   trace(xml.@UID);


   var i:int = 999;
   var s:String = i.toString(16);
   trace(s.length);
   var l:int = s.length;
   for(var j:int=0; j<6-l; j++) {
   s = "0" + s;
   }
   s = "0x" + s;
   var n:Number = new Number(s);
   trace(s);
   trace(n);

   var str:String = "rudong rudong jiangsu";
   var str2:String = str.replace(new RegExp("rudong", "g"), "china");
   trace(str);
   trace(str2);


   var str:String = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Root><Colors>jiangsu</Colors></Root>";
   var xml:XML = new XML(str);
   trace(xml.toXMLString());


   var res:String = str.match(/\/\w+\//)[0];
   trace(res.substr(1, res.length-2));

   var arr:Array = [];
   arr[2] = "shirui";
   trace(arr.length);
   trace(arr);



   var str:String = "0x532b21";
   var num:Number = new Number(str);
   trace(num);
   trace(num.toString(16));


   var text:String = "shirui";
   var xml:XML = <root><![CDATA[{ }]]></root>;
   // <![CDATA[{ }]]>
   trace(xml);

   trace(arguments.length);
   trace(arguments);
   -------------------------------------

   var test:Function = function(i:int):void { trace("test print: "+i); }
   test(5);
   for each(var object:Object in test.prototype) {
   trace("--"+object);
   }
   trace(test.prototype);
   -------------------------------------
   var xml:XML = <root><string/><battery/></root>;
   var node:XML = xml.string[0];

   node.* = "shirui";
   trace(xml);
   xml.replace(node.childIndex(), <hello/>);
   trace(xml);

   ----------------------------------------

   trace(xml);

   var node:XML = xml.good[0];

   var clone:XML = node.copy();
   var parent:XML = node.parent();
   parent.prependChild(clone);
   delete parent.children()[node.childIndex()];

   trace(xml);



   className="canvas"
   var pDOM2:XML = <root><child>123</child></root>;
   var node:XML = pDOM2.(hasOwnProperty("@className") && @className == "canvas").child[0];
   trace(pDOM2.toXMLString());
   trace(node);

   var xml:XML = <root><one>hello</one><two>1</two><two>2</two></root>;
   trace(xml);
   var node:XML = xml.two[0];
   var parent:XML = node.parent();
   var children:XMLList = parent.children();

   for(var i:int = 0; i < children.length(); i++) {
   if(children[i]==node)
   delete children[i];
   }

   trace();
   trace(xml);



   //			var xml:XML = <root><childs/><childs/></root>;
   //			trace(xml.toXMLString());
   //			delete xml.childs;
   //			trace(xml.toXMLString());
   //			var label:String = "jiangsu";
   //			var xml:XML = new XML("<root><child><![CDATA[ rudong ]]></child></root>");
   //			var node:XML = xml.child[0];
   //			xml.replace("child", XML("<Notes><![CDATA["+ label +"]]></Notes>"));
   //			trace(xml);
   //			trace(xml);
   //			xml.dog.data = <![CDATA[{label}]]>;
   //			var node:XML = xml.dog[0];
   //			trace("------------------"+node);
   //			node.text()[0] = "rudong";
   //			trace("------------------"+node);
   //			xml.pig = "hello";
   //			xml.peak = label;
   //			xml.apple = <a>{label}</a>;
   //			xml.jack = new XMLNode(XMLNodeType.ELEMENT_NODE, "abc");
   //			xml.jack1 = new XMLNode(1, "abc");
   //			xml.jack2 = new XMLNode(2, "abc");
   //			xml.jack3 = new XMLNode(3, "abc");
   //			xml.jack4 = new XMLNode(4, "abc");
   //			xml.jack5 = new XMLNode(5, "abc");
   //			xml.jack6 = new XMLNode(6, "abc");
   //			trace(xml);

   // ----------------------------------------------

   var xml:XML = <root id="5"><binding>binding1</binding><binding><info>hello</info>binding2</binding></root>;

   var xml:XML = <string>
   <locElement>
   <locID>LINE_CHART</locID>
   <locLabel>Line Chart</locLabel>
   </locElement>
   </string>;
   trace(xml);
   var value:String = xml.locElement.locLabel;
   delete xml.locElement;
   xml.* = value + " jiangsu";
   trace(xml.toXMLString());
   }

   public function size(dict:Dictionary):int {
   var size:int = 0;
   for each(var i:Object in dict) {
   size++;
   }

   return size;
   }


   // ----------------------------------------------


   var dict:Dictionary = new Dictionary();
   dict["1"] = "a";
   dict["2"] = "b";
   dict["3"] = "c";
   dict["4"] = "d";
   trace(dict);
   trace(dict.keys);
   trace(size(dict));
   for(var i:Object in dict) {
   trace(i + ": " + dict[i]);
   }

   for each(var j:Object in dict) {
   trace(j);
   }

   --------------------------------------

   var xml:XML = <root/>;
   // <root><ranges><range>range1</range><range>range2</range></ranges></root>;
   var node:XML = <shirui/>;
   node.text()[0] = "jiangsu";
   node.text()[1] = "rudong";

   trace(node);
   trace(node.toXMLString());
   trace(xml);
   xml.appendChild(node);
   trace(xml);

 */
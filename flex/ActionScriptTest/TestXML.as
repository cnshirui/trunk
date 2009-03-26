package
{
	import flash.display.Sprite;	
	public class TestXML extends Sprite
	{
		public function TestXML()
		{
			trace("start...");
//	    	var extractInfo:String = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CXCanvas guid=\"2202450108-26755-4573-184-180-23921125186053\" width=\"450\" height=\"320\">nantong</CXCanvas>";
//	    	var infoXML:XML = new XML(extractInfo);
//	    	var attrs:XMLList = infoXML.attribute("guid")
//	    	trace(attrs.length()==0);
//	    	trace(attrs);

			var title:String = "Hello World!";
			trace(title);
			setTitle(title);
			trace(title);
	    	trace("over...");
		}
		
		public function setTitle(str:String):Boolean
		{
			str = "set successful!";
			return true;
		}

	}
}
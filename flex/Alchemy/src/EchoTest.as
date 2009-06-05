package {
	import flash.display.Sprite;
	
	import cmodule.stringecho.CLibInit;
	
	public class EchoTest extends Sprite
	{
		public function EchoTest()
		{
			var loader:CLibInit = new CLibInit;
			var lib:Object = loader.init();
			trace(lib.echo("foo"));
			trace("OK");
		}
	}
}

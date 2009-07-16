package
{
	import flash.display.Sprite;

	public class ActionScriptTest extends Sprite
	{
		public function ActionScriptTest()
		{
/* 			var x:Base = new Child();
			x.run(); */
			var s:String = "july";
			var i:Number = Number(s);
			trace(i);
			trace(isNaN(i));
			trace(isNaN(9));
		}
	}
}

package
{
	public class Child extends Base
	{
		public static const NAME:String = Base.NAME;
		public function Child()
		{
			super(this);
		}
		
		public override function run():void {
			trace("Child->run()");
		}

	}
}
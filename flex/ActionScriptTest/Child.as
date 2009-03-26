package
{
	public class Child extends Base
	{
		public static const NAME:String = Base.NAME;
		public function Child()
		{
			super(this);
		}

	}
}
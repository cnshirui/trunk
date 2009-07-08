package
{
	import flash.errors.IllegalOperationError;
	
	public class Base
	{
		public static const NAME:String = "Base";
		
		public function Base(self:Base)
		{
			if(self != this)
			{
				throw new IllegalOperationError("Abstract Class, so please don't call me!");
			}
		}
		
		public function run():void {
			trace("Base->run()");
		}
		

	}
}
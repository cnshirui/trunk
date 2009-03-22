package
{

	public class Pagination
	{
		private var _pages:Array = null;
		private var _pageIndex:int = -1;

		public function Pagination(total:int, pageSize:int)
		{
			_pages = [];
			var index:int = 0;

			while(index < total)
			{
				var values:Array = [];
				var max:int = Math.min(index + pageSize, total)
				for(var i:int = index; i < max; i++)
				{
					values.push(index);
					index++;
				}
				_pages.push(values);
			}
		}

		public function getNext():Array
		{
			return nextAvailable() ? _pages[++_pageIndex] : null;
		}

		public function getBefore():Array
		{
			return beforeAvaiable() ? _pages[--_pageIndex] : null;
		}

		public function nextAvailable():Boolean
		{
			return _pageIndex < _pages.length - 1;
		}

		public function beforeAvaiable():Boolean
		{
			return _pageIndex > 0;
		}
	}
}
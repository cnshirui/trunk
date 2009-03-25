package commands
{
	import mx.rpc.http.HTTPService;
	import mx.rpc.events.ResultEvent;

	public class CommandProcessor extends HTTPService
	{
		private var host_:String = "";
		private var cmd_:String;
		private var servUrl_:String = "DemoServer?action=";
		
		public function CommandProcessor():void
		{
		}

		public function get host():String
		{
			return host_;
		}
		
		public function subscribe(callback:Function):void
		{
			this.addEventListener(ResultEvent.RESULT, callback);
		}
				
		public function processCommand(cmd:String):void 
		{
			servUrl_ = host_ + "DemoServer?action=";		
			cmd_ = cmd;
			this.url = servUrl_ + cmd_;
			this.send();
		}
		
		public function retry(host:String):void
		{
			host_ = host;
			servUrl_ = host_ + "DemoServer?action=";		
			this.url = servUrl_ + cmd_;
			this.send();
		}
		
		public function stopListening(callback:Function ):void 
		{
			this.removeEventListener(ResultEvent.RESULT, callback);
		}
	}
}
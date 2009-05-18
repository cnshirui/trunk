package custom


{import fl.video.*;
import flash.net.LocalConnection;
	public class CustomFLVPlayer extends FLVPlayback
	{
		
		public function CustomFLVPlayer()
		{
			
		}
override public function seekToNextNavCuePoint(time:Number=NaN):void {
	
			var toFlex_lc2:LocalConnection;
		toFlex_lc2 = new LocalConnection();
			toFlex_lc2.send('lc_from_flash', 'seekToNext');
	
}
override public function seekToPrevNavCuePoint(time:Number=NaN):void {
	
			var toFlex_lc3:LocalConnection;
		toFlex_lc3 = new LocalConnection();
			toFlex_lc3.send('lc_from_flash', 'seekToPrev');
	
}
}
	}

package {
	
	import flash.display.*;
	import flash.external.ExternalInterface;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextFieldAutoSize;
	import flash.filters.DropShadowFilter;

	public class RightClick extends Sprite
	{
		
		private var canvas:Sprite = new Sprite();
		private var title:TextField = new TextField();
		private var f1:TextFormat = new TextFormat();
		
		public function RightClick()
		{
			stage.scaleMode = StageScaleMode.NO_SCALE;
			stage.align = StageAlign.TOP_LEFT;
			
			f1.font = "_sans";
            f1.color = 0xff8c00;
			f1.bold = true;
            f1.size = 14;
			
			title.type = "dynamic";
			title.defaultTextFormat = f1;
			title.autoSize = TextFieldAutoSize.LEFT;
			title.text = "Right Click Me";
			title.selectable = false;
			title.x = stage.stageWidth / 2 - title.textWidth / 2;
			title.y = stage.stageHeight / 2;
			
			title.filters = [new DropShadowFilter(1,45,0,0.9,0,0)];
			
			addChild(canvas);
			addChild(title);
			
			var methodName:String = "rightClick";
			var method:Function = onRightClick;
			ExternalInterface.addCallback(methodName, method);
		}
		
		private function onRightClick():void {
			for(var i:int = 0; i<5; i++) {
				drawRandomBlob(i);
			}
		}
		
		private function drawRandomBlob(value:int):void {
			var mx:int = stage.mouseX;
			var my:int = stage.mouseY;
			if(my > 0 && my < stage.stageHeight && mx > 0 && mx < stage.stageWidth) {
				with(canvas.graphics) {
					beginFill(0x000000, value / 10);
					drawCircle(mx,my,10*Math.random()+value);
					endFill();
				}
			}
		}
	}
}

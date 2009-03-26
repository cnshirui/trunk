package {
    import flash.display.Sprite;
    import flash.text.TextField;
    import flash.text.TextFieldType;
    import flash.text.TextFieldAutoSize;
    import flash.utils.Timer;
    import flash.events.TimerEvent;
    import flash.events.Event;

    public class Timer_constructorExample extends Sprite {
            private var statusTextField:TextField = new TextField();        
            private var inputTextField:TextField = new TextField();
            private var delay:uint = 10000;
//            private var repeat:uint = 3;
            private var myTimer:Timer = new Timer(delay, 1);
            
        public function Timer_constructorExample() {
            inputTextField.x = 10;
            inputTextField.y = 10;
            inputTextField.border = true;
            inputTextField.background = true;
            inputTextField.height = 200;
            inputTextField.width = 200;
            inputTextField.multiline = true;
            inputTextField.wordWrap = true;
            inputTextField.type = TextFieldType.INPUT;

            statusTextField.x = 10;
            statusTextField.y = 220;
            statusTextField.background = true;
            statusTextField.autoSize = TextFieldAutoSize.LEFT;   

            myTimer.start(); 
            statusTextField.text = "You have " + ((delay) / 1000) 
                                 + " seconds to write your response.";

            myTimer.addEventListener(TimerEvent.TIMER, timerHandler);
            myTimer.addEventListener(TimerEvent.TIMER_COMPLETE, completeHandler);

            addChild(inputTextField);
            addChild(statusTextField);
        }

        private function timerHandler(e:TimerEvent):void{
//            repeat--;
            statusTextField.text = ((delay) / 1000) + " seconds left.";
        }

        private function completeHandler(e:TimerEvent):void {
            statusTextField.text = "Times Up.";
            inputTextField.type = TextFieldType.DYNAMIC;    
        }
    }
}

#Objective of tutorial 1:
#Create a textfield.  On each frame, increment counter
#Access the native Object (Date)

@counter = 0;
#AS::Date refers to the native Date object
#Use Object.nativeNew method when you do not want the closure object/placeholder to be passed in.  Refer to the Api for more details
@now = AS::Date.new();
@dateString = String.new(@now.toString());
#textbox
@txtField = TextField.createInstance(self);
@txtField._x = 0;
@txtField._y = 0;
@txtField._width = 550;
@txtField._height = 400;
@txtField.border = true;
@txtField.type = "input";

def onEnterFrame()
	@txtField.text = (@dateString + "\nCounter:" + @counter.to_s()).toString();
	@counter += 1;
end
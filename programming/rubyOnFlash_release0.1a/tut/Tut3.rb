load "DrawingApi.rb"
#Objective: Use of Sprites and ImageSprite, using Keys

#Note that this DummyImageSprite class knows that its underlying image is called Dummy, cos its name is DummyImageSprite.  
#Naming convention: [NameOfImageInResourceFile]ImageSprite.  To override, set @@imageId attribute
class DummyImageSprite < ImageSprite
	def initialize()
		self.setX(70);
		self.setY(100);
	end
	
	def onKeyUp()
		if(Key.getCode() == Key.RIGHT) then
			self.setX(self.getX()+10);
		elsif (Key.getCode() == Key.UP) then
			self.setY(self.getY()-10);
		elsif (Key.getCode() == Key.LEFT) then
			self.setX(self.getX()-10);
		elsif (Key.getCode() == Key.DOWN) then
			self.setY(self.getY()+10);
		end
	end
end

class Dummy2ImageSprite < ImageSprite
	#example of how to override the naming convention
	@@imageId = "Dummy";
	
	def initialize()
		self.setX(350);
		self.setY(100);
	end
	
	def onKeyUp()
		if(Key.getCode() == 68) then
			self.setX(self.getX()+10);
		elsif (Key.getCode() == 87) then
			self.setY(self.getY()-10);
		elsif (Key.getCode() == 65) then
			self.setX(self.getX()-10);
		elsif (Key.getCode() == 83) then
			self.setY(self.getY()+10);
		end
	end
end

class CustomSprite < Sprite
	def initialize()
		self.setX(100);
		self.setY(100);
	end
	
	def onEnterFrame()
		self.clear();
		self.lineStyle(0, "0x0000FF", 100);
		self.beginFill("0xFFFF00",100);
		self.drawSector(0,0,0,360,50);
		self.endFill();
	end
end

dummySprite = DummyImageSprite.createInstance(self);
dummy2Sprite = Dummy2ImageSprite.createInstance(self);

#You can create an image sprite without defining a class!  Just load a jpg file
frogSprite = ImageSprite.createInstanceFromFile('tut/frog.jpg',self);
frogSprite.setX(200);
frogSprite.setY(200);

#example of how you can define a method for a particular object, instead of to its class
def frogSprite.onKeyUp()
	if(Key.getCode() == Key.SPACE)
		self.setY(self.getY() - 10);
	end
end

@customSprite = CustomSprite.createInstance(self);

def onMouseMove()
	@customSprite.setX(self.getXMouse());
	@customSprite.setY(self.getYMouse());
end
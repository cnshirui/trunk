class Key
	def self.addListener(listener)
		AS::Key.addListener(listener);
	end
	
	def self.getAscii()
		return Integer.new(AS::Key.getAscii());
	end
	
	def self.getCode()
		return Integer.new(AS::Key.getCode());
	end
	
	def self.isDown(keyCode)
		temp = AS::Key.isDown(keyCode.valueOf());
		return Object.toObject(temp);
	end
	
	def self.isToggled(keyCode)
		temp = AS::Key.isToggled(keyCode.valueOf());
		return Object.toObject(temp);
	end
	
	def self.removeListener(obj)
		temp = AS::Key.removeListener(obj);
		return Object.toObject(temp);
	end
	
	BACKSPACE = 27;
	CAPSLOCK = 20;
	CONTROL = 17;
	DELETEKEY = 46;
	DOWN = 40;
	END = 35;
	ENTER = 13;
	ESCAPE = 27;
	HOME = 36;
	INSERT = 45;
	LEFT = 37;
	PGDN = 34;
	PGUP = 33;
	RIGHT = 39;
	SHIFT = 16;
	SPACE = 32;
	TAB = 9;
	UP = 38;
end
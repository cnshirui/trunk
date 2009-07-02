class Mouse
	def self.hide()
		temp = self.nativeHide();
		return Object.toObject(temp);
	end
	
	def self.removeListener(obj);
		temp = self.nativeRemoveListener(obj);
		return Object.toObject(temp);
	end
	
	def self.show()
		temp = self.nativeShow();
		return Object.toObject(temp);
	end
end
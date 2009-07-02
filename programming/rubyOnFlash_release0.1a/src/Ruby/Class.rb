class Class
	def self.superclass()
		if(defined? self.__proto__)
			return self.__proto__;
		else
			return nil;
		end
	end
	
	def subclass_of?(aClass)
		if(self.equal?(aClass))then
			return true;
		else
			if(self.equal?(Object))then
				return false;
			else
				return self.__proto__.subclass_of?(aClass);
			end
		end
	end
end
class NilClass
	#anObject would be evaluated before this method is called
	def &(anObject)
		return false;
	end
	
	def |(anObject)
		if(anObject) then
			return true;
		else
			return false;
		end
	end
	
	def ^(anObject)
		if(anObject) then
			return true;
		else
			return false;
		end
	end
	
	def ==(anObj)
		if(anObj.instance_of?(NilClass))
			return true;
		else
			return false;
		end
	end
	
	def nil?()
		return true;
	end
	
	def to_a()
		return [];
	end
	
	def to_i()
		return 0;
	end
	
	def to_s()
		return "";
	end
end
class TrueClass
	#anObject would be evaluated before this method is called
	def |(anObject)
		return true;
	end
	
	def &(anObject)
		if(anObject) then
			return true;
		else
			return false;
		end
	end
	
	def ^(anObject)
		if(anObject) then
			return false;
		else
			return true;
		end
	end
	
	def ==(anObj)
		if(anObj.instance_of?(TrueClass))
			return true;
		else
			return false;
		end
	end
end
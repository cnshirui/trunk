class FalseClass
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
		if(anObj.instance_of?(FalseClass))
			return true;
		else
			return false;
		end
	end
end
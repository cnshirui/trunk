class Numeric
	def nonzero?()
		if(@value==0)
			return nil;
		else
			return self;
		end
	end
	
	def zero?()
		if(@value==0)
			return true;
		else
			return false;
		end
	end
	
	def coerce(aNumeric)
		anArray = [];
		if(self.constructor.equal?(aNumeric.constructor))
			anArray[0] = aNumeric;
			anArray[1] = self;
		else
			anArray[0] = aNumeric.to_f();
			anArray[1] = self.to_f();
		end
		return anArray;
	end
	
	def divmod(aNumeric)
		q = floor(self/aNumeric);
		r = self - q * aNumeric;
		anArray = [];
		anArray[0] = q;
		anArray[1] = r;
		
		return anArray;
	end
	
	def modulo(aNumeric)
		anArray = self.divmod(aNumeric);
		return anArray[1];
	end
	
	def remainer(aNumeric)
		modVal = self.modulo(aNumeric);
		if((self > 0 && aNumeric < 0) || (self < 0 && aNumeric > 0))
			return modVal - aNumeric;
		else
			return modVal;
		end
	end
	
	def div(aNumeric)
		result = self/aNumeric;
		result = result.to_i();
		return result;
	end
end
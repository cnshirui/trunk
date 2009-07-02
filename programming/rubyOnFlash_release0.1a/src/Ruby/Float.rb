class Float
	def abs()
		absVal = AS::Math.abs(@value);
		return Float.new(absVal);
	end
	
	def integer?()
		return false;
	end
	
	def ceil()
		temp = AS::Math.ceil(@value);
		return Integer.new(temp);
	end
	
	def finite?()
		flag = (self.infinite?() || self.nan?());
		return (!flag);
	end
	
	def floor()
		temp = AS::Math.floor(@value);
		return Integer.new(temp);
	end
	
	def infinite?()
		if(Object.primitiveEqual?(AS::Number(@value),AS::Number.NEGATIVE_INFINITY))
			return -1;
		elsif(Object.primitiveEqual?(AS::Number(@value),AS::Number.POSITIVE_INFINITY))
			return 1;
		else
			return nil;
		end
	end
	
	def nan?()
		flag = Object.toObject(AS::isNan(@value));
		return flag;
	end
	
	def round()
		temp = AS::Math.round(@value);
		return Integer.new(temp);
	end
	
	def to_f()
		return self;
	end
	
	def to_i()
		return self.floor();
	end
	
	alias to_int to_i;
	
	alias truncate to_i;
	
	#step disabled
end
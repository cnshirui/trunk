class Object
	def self.superclass()
		return nil;
	end
	
	def ===(other)
		return (self == other);
	end
	
	def class()
		return self.constructor;
	end
	
	def eql?(other)
		return (self == other);
	end
	
	#not used
	def freeze()
		@frozen = true;
	end
	
	#not used
	def frozen?()
		return @frozen;
	end
	
	def inspect()
		return self.to_s();
	end
	
	def nil?()
		return false;
	end
	
	def to_a()
		return [self];
	end
	
	def kind_of?(aClass)
		return self.constructor.subclass_of?(aClass);
	end
	
	alias is_a? kind_of?
	
	def rand(max=0)
		max = max.to_i().abs();
		if(max == 0)then
			max = 1;
		end
		return Float.new(AS::Math.random()) * max;
	end
end
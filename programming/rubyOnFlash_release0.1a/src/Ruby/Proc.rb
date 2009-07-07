class Proc
	def self.new(&p)
		return p;
	end
	
	def [](*args)
		return self.call(*args);
	end
	
	def ==(other)
		return self.equal?(other);
	end
	
	def arity()
		return @arity;
	end
	
	def binding()
		return @binding;
	end
	
	def to_proc()
		return self;
	end
	
	#Shows the unique identifier for this proc, along with an indication of where the proc was defined.
	#however, we do not have a unique identifier
	def to_s()
		"proc instance";
	end
end
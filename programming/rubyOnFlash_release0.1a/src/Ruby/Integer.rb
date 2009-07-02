class Integer
	def abs()
		absVal = AS::Math.abs(@value);
		return Integer.new(absVal);
	end
	
	def integer?()
		return true;
	end
	
	def to_i()
		return self;
	end
	
	def to_int()
		return self;
	end
	
	def to_f()
		return Float.new(@value);
	end
	
	def upto(i)
		j = self;
		while(j <= i)
			yield(j);
			j = j + 1;
		end
	end
	
	def downto(i)
		j = self;
		while(j >= i)
			yield(j);
			j = j - 1;
		end
	end

	def times()
		j = 0;
		while(j < self)
			yield(j);
			j = j + 1;
		end
	end
	
	def succ()
		return self + 1;
	end
	
	alias next succ;
	
	def step(endNum, inc)
		if(inc > 0) then
			j = self;
			while(j < endNum)
				yield(j);
				j = j + inc;
			end
		elsif(inc < 0) then
			j = self;
			while(j > endNum)
				yield(j);
				j = j + inc;
			end
		end
	end
	
	def[](arg)
		mask = 2 ** (arg);
		newValue = self & mask;
		newValue = newValue >> arg;
		return newValue;
	end
	
	def /(other)
		temp = Float.new(@value)/other;
		if(other.instance_of?(Integer))
			return temp.to_i();
		else
			return temp;
		end
	end
	
	alias div /;
	
	def ceil()
		return self;
	end
	
	def floor()
		return self;
	end
	
	def round()
		return self;
	end
	
	def truncate()
		return self;
	end
	
	def chr()
		return String.new(AS::String.fromCharCode(@value));
	end
	
	def denominator()
		return 1;
	end
	
	def numerator()
		return self;
	end
end
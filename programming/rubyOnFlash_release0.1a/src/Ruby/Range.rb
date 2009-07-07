class Range
	def initialize(first,last,exclusive)
		@first = first;
		@last = last;
		if !(defined? exclusive)
			@exclusive = false;
		else
			@exclusive = exclusive;
		end
	end
	
	def ==(other)
		if(other.instance_of?(Range))
			if(other.first() == self.first() && other.last == self.last() && other.exclude_end?()==self.exclude_end?())
				return true;
			else
				return false;
			end
		else
			return false;
		end
	end
	
	def eql?(other)
		return (self == other);
	end
	
	def ===(anObj)
		if(@exclusive)
			if(((anObj <=> @first) >= 0) && ((anObj <=> @last) < 0))
				return true;
			else
				return false;
			end
		else
			if(((anObj <=> @first) >= 0) && ((anObj <=> @last) <= 0))
				return true;
			else
				return false;
			end
		end
	end
	
	alias member? ===;
	
	alias include? ===;
	
	def begin()
		return @first;
	end
	
	def each()
		curr = @first;
		if(@exclusive)
			while((curr <=> @last) < 0)
				yield(curr);
				curr = curr.succ();
			end
		else
			while((curr <=> @last) <= 0)
				yield(curr);
				curr = curr.succ();
			end
		end
		return self;
	end
	
	def end()
		return @last;
	end
		
	def exclude_end?()
		return @exclusive;
	end
	
	def first()
		return @first;
	end
	
	alias last end;
	
	def step(n)
		curr = @first;
		if(@exclusive)
			while((curr <=> @last) < 0)
				yield(curr);
				j = 0;
				while(j < n)
					curr = curr.succ();
					j += 1;
				end
			end
		else
			while((curr <=> @last) <= 0)
				yield(curr);
				while(j < n)
					curr = curr.succ();
					j += 1;
				end
			end
		end
		return self;
	end
	
	def to_s()
		res = @first.to_s();
		if(@exclusive)
			res += "...";
		else
			res += "..";
		end
		res += @last.to_s();
		return res;
	end
end
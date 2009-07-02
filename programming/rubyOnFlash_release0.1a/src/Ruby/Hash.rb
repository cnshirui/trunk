class Hash
	def self.[](*args)
		x = (args.size()/2);
		i = 0;
		newHash = self.new();
		while(i < x)
			newHash[args[i*2]] = args[i*2+1];
			i += 1;
		end
		
		return newHash;
	end
	
	def ==(other)
		if(@keys == other.keys())
			i = 0;
			while(i < @keys.size())
				if(self[@keys[i]] != other[@keys[i]])
					return false;
				end
				i += 1;
			end
			return true;
		else
			return false;
		end
	end
	
	alias store []=;
	
	def clear()
		@keys.clear();
	end
	
	def get_default(key)
		if(defined? key)
			return self[key];
		else
			return @defaultObj;
		end
	end
	
	def set_default(obj)
		@defaultObj = obj;
		return self;
	end
	
	def default_proc()
		return @proc;
	end
	
	def delete(key)
		bExists = @keys.include?(key);
		if(bExists)
			@keys.delete(key);
		else
			if(defined? yield)
				return yield(key);
			else
				return @defaultObj;
			end
		end
	end
	
	def delete_if()
		i = 0;
		toBeDeleted = [];
		while(i < @keys.size())
			if(yield(@keys[i],self[@keys[i]]))
				toBeDeleted << @keys[i];
			end
			i += 1;
		end
		
		i = 0;
		while(i < toBeDeleted.size())
			self.delete(toBeDeleted[i]);
			i+=1;
		end
		
		return self;
	end
	
	def each()
		i = 0;
		while(i < @keys.size())
			yield(@keys[i],self[@keys[i]]);
			i+=1;
		end
		return self;
	end
	
	def each_key()
		i = 0;
		while(i < @keys.size())
			yield(@keys[i]);
			i+=1;
		end
		return self;
	end
	
	def each_pair()
		i = 0;
		while(i < @keys.size())
			yield([@keys[i],self[@keys[i]]]);
			i+=1;
		end
		return self;
	end
	
	def each_value()
		i = 0;
		while(i < @keys.size())
			yield(self[@keys[i]]);
			i+=1;
		end
		return self;
	end
	
	def empty?()
		return @keys.empty?();
	end
	
	#NOTE: differs from Ruby standard, as an IndexError is not thrown.  Instead, nil is returned
	def fetch(key,obj)
		bExists = @keys.include?(key);
		if(bExists)
			return self[key];
		else
			if(defined? obj)
				return obj;
			else
				if(defined? yield)
					return yield(key);
				else
					#nil is returned instead of IndexError
					return nil;
				end
			end
		end
	end
	
	def has_key?(key)
		return @keys.include?(key);
	end
	
	alias include? has_key?;
	alias key? has_key?;
	alias member? has_key?;
	
	def has_value?(value)
		i = 0;
		while(i < @keys.size())
			if(self[@keys[i]] == value)
				return true;
			end
			i += 1;
		end
		return false;
	end
	
	def index(value)
		i = 0;
		while(i < @keys.size())
			if(self[@keys[i]] == value)
				return @keys[i];
			end
			i += 1;
		end
		return nil;
	end
	
	#this is a private method.  Do not use this method
	def _getInnerHash()
		return @hash;
	end
	
	def replace(other)
		@keys = [];
		@hash = Object.new();
		
		otherKeys = other.keys();
		i = 0;
		while(i < otherKeys.size())
			self[otherKeys[i]] = other[otherKeys[i]];
			i += 1;
		end
		return self;
	end
	
	def keys()
		return @keys;
	end
	
	def length()
		return @keys.size();
	end
	
	alias size length;
	
	def merge(other)
		newHash = Hash.new();
		i = 0;
		while(i < @keys.size())
			newHash[@keys[i]] = self[@keys[i]];
			i += 1;
		end
		
		i=0;
		otherKeys = other.keys();
		while(i < otherKeys.size())
			newHash[otherKeys[i]] = other[otherKeys[i]];
			i += 1;
		end
		
		return newHash;
	end
	
	def merge!(other)
		i=0;
		otherKeys = other.keys();
		while(i < otherKeys.size())
			self[otherKeys[i]] = other[otherKeys[i]];
			i += 1;
		end
		
		return self;
	end
	
	alias update merge!;
	
	def dup()
		newHash = Hash.new();
		i = 0;
		while(i < @keys.size())
			newHash[@keys[i]] = self[@keys[i]];
			i += 1;
		end
		return newHash;
	end
	
	def reject(&p)
		newHash = self.dup();
		newHash.delete_if(&p);
		return newHash;
	end
	
	def reject!()
		i = 0;
		toBeDeleted = [];
		while(i < @keys.size())
			if(yield(@keys[i],self[@keys[i]]))
				toBeDeleted << @keys[i];
			end
			i += 1;
		end
		
		i = 0;
		while(i < toBeDeleted.size())
			self.delete(toBeDeleted[i]);
			i+=1;
		end
		
		if(toBeDeleted.size()==0)
			return nil;
		else
			return self;
		end
	end
	
	def select()
		newArray = [];
		i = 0;
		while(i < @keys.size())
			if(yield(@keys[i],self[@keys[i]]))
				newArray << [@keys[i],self[@keys[i]]];
			end
			i+=1;
		end
		return newArray;
	end
	
	def shift()
		if(self.length()==0)
			return @defaultObj;
		else
			firstKey = @keys[0];
			result = [firstKey,self[firstKey]];
			self.delete(firstKey);
			return result;
		end
	end
	
	def sort(&p)
		newArray = self.to_a();
		newArray.sort(&p);
		return newArray;
	end
	
	def to_a()
		newArray = [];
		i = 0;
		while(i < @keys.size())
			newArray << [@keys[i],self[@keys[i]]];
			i+=1;
		end
		return newArray;
	end
	
	def to_hash()
		return self;
	end
	
	def to_s()
		newArray = self.to_a();
		return newArray.join();
	end
	
	def values()
		newArray = [];
		i = 0;
		while(i < @keys.size())
			newArray << self[@keys[i]];
			i+=1;
		end
		return newArray;
	end
	
	def values_at(*args)
		newArray = [];
		i = 0;
		while(i < args.size())
			newArray << self[args[i]];
			i += 1;
		end
		return newArray;
	end
	
	def invert()
		newHash = Hash.new();
		i = 0;
		while(i < @keys.size())
			newHash[self[@keys[i]]] = @keys[i];
			i += 1;
		end
		return newHash;
	end
end
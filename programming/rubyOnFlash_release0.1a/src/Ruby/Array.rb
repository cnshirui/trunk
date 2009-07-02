class Array
	#pack is not supported, since it packs this array into a binary sequence, which does not make sense to flash virtual machine
	
	def self.[](*args)
		arr = Array.new();
		i = 0;
		while(i<args.size())
			arr[i] = args[i];
			i += 1;
		end
		
		return arr;
	end
	
	def initialize(*args)
		argSize = Integer.new(args.length);
		if(argSize==1)
			if(args[0].instance_of?(Integer))
				if(defined? yield)
					i = 0;
					while(i < args[0])
						self[i] = yield(i);
						i +=1;
					end
				else
					self.length = args[0].valueOf();
				end
			else
				#assume array
				otherArray = args[0];
				i = 0;
				while(i < otherArray.size())
					self[i] = otherArray[i];
					i += 1;
				end
			end
		end
	end
	
	def &(anArray)
		array1 = self.uniq();
		array2 = anArray.uniq();
		
		res = Array.new();		
		i = 0;
		while(i<array1.size())
			key = array1[i];
			j = 0;
			exists = false;
			while(j<array2.size())
				if(array2[j]==key)
					exists = true;
					break;
				end
				j += 1;
			end
			if(exists)
				res.push(key);
			end
			i += 1;
		end
		
		return res;
	end
	
	def dup()
		newArray = [];
		i = 0;
		while(i < self.size())
			newArray << self[i];
			i +=1;
		end
		return newArray;
	end
	
	def +(anArray)
		#clone this array
		newArray = self.dup();
		i = 0;
		array2Size = anArray.size();
		while(i<array2Size)
			newArray << anArray[i];
			i = i + 1;
		end
		
		return newArray;
	end
	
	def * (numOrStr)
		if(numOrStr.instance_of?(Integer))
			newArray = [];
			i = 0;
			while(i<numOrStr)
				j = 0;
				while(j<self.size())
					newArray << self[j];
					j = j+1;
				end
				i = i + 1;
			end
			return newArray;
		else
			#assume String
			return self.join(numOrStr);
		end
	end
	
	def -(anArray)
		array1 = self.uniq();
		array2 = anArray.uniq();
		res = Array.new();
		i = 0;
		while(i<array1.size())
			j = 0;
			key = array1[i];
			exists = false;
			while(j<array2.size())
				if(key==array2[j])
					exists = true;
					break;
				end
				j +=1;
			end
			
			if(!exists)
				res.push(key);
			end
			i+=1;
		end
	end
	
	def <=>(anArray)
		if(self.size()<anArray.size())
			smallerArraySize = self.size();
		else
			smallerArraySize = anArray.size();
		end
		
		i = 0;
		while(i<smallerArraySize)
			comp = self[i] <=> anArray[i];
			if(comp != 0)
				return comp;
			end
			i += 1;
		end
		
		if(self.size() < anArray.size())
			return -1;
		elsif(self.size() > anArray.size())
			return 1;
		else
			return 0;
		end
	end
	
	def ==(anArray)
		if((self<=>anArray)==0)
			return true;
		else
			return false;
		end
	end
	
	alias eql? ==;
	
	def ===(anArray)
		return (self==anArray);
	end
	
	def |(anArray)
		array1 = self.uniq();
		array2 = anArray.uniq();
		
		array1Size = array1.size();
		array2Size = array2.size();
		i=0
		while(i<array2Size)
			key = array2[i];
			j = 0;
			exists = false;
			while(j<array1Size)
				if(key==array1[j])
					exists = true;
					break;
				end
				j += 1;
			end
			if(!exists)
				array1.push(key);
			end
			i +=1;
		end
		return array1;
	end
	
	def [](*args)
		if(args.size()==1)
			if(args.getAt(0).instance_of?(Range))
				first = args.getAt(0).first();
				if(first < 0)
					first = self.size() + first;
				end
				if(first >= self.size())
					return nil;
				end
				last = args.getAt(0).last();
				if(last < 0)
					last = self.size() + last;
				end
				newArray = [];
				i = first;
				while(i <= last)
					if(i < self.size())
						newArray << self.getAt(i);
						i += 1;
					else
						break;
					end
				end
				return newArray;
			else
				return self.at(args.getAt(0));
			end
		else
			first = args.getAt(0);
			if(first < 0)
				first = self.size() + first;
			end
			length = args.getAt(1);
			if(length < 0)
				return nil;
			end
			newArray = [];
			i = 0;
			while(i < length)
				currIndex = first + i;
				if(currIndex < self.size())
					newArray << self.getAt(currIndex);
					i += 1;
				else
					break;
				end
			end
			return newArray;
		end
	end
	
	def []=(*args)
		if(args.size()==2)
			if(args.getAt(0).instance_of?(Range))
				#range			
				first = args.getAt(0).first();
				if(first < 0)
					first = self.size() + first;
				end
				last = args.getAt(0).last();
				if(last < 0)
					last = self.size() + last;
				end
				if(args.getAt(1).instance_of?(Array))
					space = last - first + 1;
					inputSize = args.getAt(1).size()
					if(inputSize <= space)
						i = 0;
						while(i<args.getAt(1).size())
							self.setAt((first + i),args.getAt(1).getAt(i));
							i += 1;
						end
						#shift the rest left
						diff = space - inputSize;
						i = last + 1;
						while(i < self.size())
							self.setAt((i-diff),self.getAt(i));
							i += 1;
						end
						self.length = (self.size() + inputSize - space).valueOf();
					else
						diff = inputSize - space;
						i = self.size() - 1;
						while(i >= last)
							self.setAt((i + diff),self.getAt(i));
							i -= 1;
						end
						i = 0;
						while(i<inputSize)
							self.setAt((i + first),args[1].getAt(i));
							i += 1;
						end
					end
				else
					self.setAt(first,args.getAt(1));
					if(last >= self.size()-1)
						self.length = first + 1;
					else
						diff = last - first;
						i = last+1;
						while(i<self.size())
							self.setAt((i-diff),self.getAt(i));
							i += 1;
						end
						newSize = self.size() - diff;
						self.length = newSize.valueOf();
					end
				end
				return args.getAt(1);
			else
				index = args.getAt(0);
				if(index < 0)
					index = self.size() + index;
				end
				self.setAt(index,args.getAt(1));
				return args.getAt(1);
			end
		else
			first = args.getAt(0);
			if(first < 0)
				first = self.size() + first;
			end
			length = args.getAt(1);
			if(length < 0)
				return nil;
			end
			
			rangeObj = Range.new(first,first + length);
			return (self[rangeObj] = args.getAt(2));
		end
	end
	
	def assoc(anObj)
		i = 0;
		while(i<self.size())
			if(self[i].instance_of?(Array))
				temp = self[i];
				if(temp[0]==anObj)
					return temp;
				end
			end
			i += 1;
		end
		return nil;
	end
	
	def at(offset)
		if(offset < 0)
			offset = self.size() + offset;
		end
		return self.getAt(offset);
	end
	
	def clear()
		self.length = 0.valueOf();
	end
	
	def collect()
		newArray = [];
		i = 0;
		while(i<self.size())
			temp = yield(self[i]);
			newArray << temp;
			i += 1;
		end
		
		return newArray;
	end
	
	def collect!()
		i = 0;
		while(i<self.size())
			temp = yield(self[i]);
			self[i] = temp;
			i += 1;
		end
		
		return self;
	end
	
	def compact()
		newArray = [];
		i = 0;
		while(i<self.size())
			if(self[i] != nil && (defined? self[i]))
				newArray << self[i];
			end
			i+= 1;
		end
		return newArray;
	end
	
	def compact!()
		newArray = self.compact();
		self.length = 0.valueOf();
		i = 0;
		while(i<newArray.size())
			self << newArray[i];
			i += 1;
		end
	end
	
	def concat(anArray)
		i = 0;
		while(i<anArray.size())
			self << anArray[i];
			i += 1;
		end
		
		return self;
	end
	
	def delete(anObj)
		i = 0;
		while(i < self.size())
			if(self[i] == anObj)
				j = i + 1;
				while(j < self.size())
					self[j-1] = self[j];
					j += 1;
				end
				self.length = (self.size()-1).valueOf();
				return anObj;
			end
			i += 1;
		end
		
		if(defined? yield)
			return yield();
		else
			return nil;
		end
	end
	
	def delete_at(index)
		if(index >= self.size())
			return nil;
		end
		temp = self[index];
		i = index + 1;
		while(i<self.size())
			self[i-1] = self[i];
			i += 1;
		end
		self.length = (self.size() - 1).valueOf();
		return temp;
	end
	
	def delete_if()
		i = 0;
		while(i<self.size())
			if(yield(self[i]))
				self.delete_at(i);
				#don't increment counter, cos after deletion, all elements shifted left
			else
				i += 1;
			end
		end
		return self;
	end
	
	def each()
		i = 0;
		while(i<self.size())
			yield(self[i]);
			i += 1;
		end
		
		return self;
	end
	
	def each_index()
		i = 0;
		while(i<self.size())
			yield(i);
			i += 1;
		end
		
		return self;
	end
	
	def empty?()
		if(self.size()==0)
			return true;
		else
			return false;
		end
	end
	
	#differs from official ruby implementation in that this does not throw an error, but returns nil instead
	def fetch(index, default)
		result = self[index];
		if(result)
			return result;
		else
			if(defined? default)
				return default;
			else
				if(defined? yield)
					return yield(index);
				else
					return nil;
				end
			end
		end
	end
	
	def fill(*args)
		if(args.size() ==0)
			i = 0;
			while(i < args.size())
				self[i] = yield(i);
				i += 1;
			end
		elsif(args.size()==1)
			if(defined? yield)
				if(args[0].instance_of?(Integer))
					i = args[0];
					while(i < self.size())
						self[i] = yield(i);
						i += 1;
					end
				else
					#assume range
					i = args[0].first();
					while(i <= args[0].last())
						self[i] = yield(i);
						i += 1;
					end
				end
			else
				#entire array
				i = 0;
				while(i<self.size())
					self[i] = args[0];
					i += 1;
				end
			end
		elsif(args.size()==2)
			if(defined? yield)
				first = args[0];
				last = args[0] + args[1] - 1;
				i = first;
				while(i <= last)
					self[i] = yield(i);
					i += 1;
				end
			else
				if(args[1].instance_of?(Integer))
					i = args[1];
					while(i < self.size())
						self[i] = yield(i);
						i += 1;
					end
				else
					#a range
					i = args[1].first();
					last = args[1].last();
					while(i<=last)
						self[i] = args[0];
						i += 1;
					end
				end
			end
		else
			# start and end
			i = args[1];
			if(i==nil)
				i = 0;
			end
			length = args[2];
			if(length==nil)
				last = self.size();
			else
				last = length + i;
			end
			while(i<last)
				self[i] = args[0];
				i += 1;
			end
		end
		
		return self;
	end
	
	def first(n)
		if(defined? n)
			arr = [];
			i = 0;
			while(i<self.size() && i<n)
				arr[i] = self[i];
				i += 1;
			end
			return arr;
		else
			if(self.size() < 1)
				return nil;
			else
				return self[0];
			end
		end
	end
	
	def flatten()
		newArray = [];
		i = 0;
		while(i<self.size())
			if(self[i].instance_of?(Array))
				innerArray = self[i].flatten();
				j = 0;
				while(j<innerArray.size())
					newArray << innerArray[j];
					j += 1;
				end
			else
				newArray << self[i];
			end
			i += 1;
		end
		
		return newArray;
	end
	
	def flatten!()
		newArray = self.flatten();
		i = 0;
		while(i<newArray.size())
			self[i] = newArray[i];
			i += 1;
		end
		
		return self;
	end
	
	def include?(anObj)
		i = 0;
		while(i < self.size())
			if(self[i]==anObj)
				return true;
			end
			i += 1;
		end
		
		return false;
	end
	
	def index(anObj)
		i = 0;
		while(i < self.size())
			if(self[i]==anObj)
				return i;
			end
			i += 1;
		end
		
		return nil;
	end
	
	def indexes(*args)
		newArray = [];
		i = 0;
		while(i<args.size())
			newArray << self[args[i]];
			i += 1;
		end
		
		return newArray;
	end
	
	alias indices indexes;
	
	def last(n)
		if(defined? n)
			arr = [];
			i = self.size() - n + 1;
			if(i < 0)
				i = 0;
			end
			while(i < self.size())
				arr[i] = self[i];
				i += 1;
			end
			return arr;
		else
			if(self.size() < 1)
				return nil;
			else
				return self[(self.size()-1)];
			end
		end
	end
	
	alias map! collect!;
	
	def nitems()
		i = 0;
		count = 0;
		while(i < self.size())
			if(self[i] != nil)
				count += 1;
			end
			i += 1;
		end
		
		return count;
	end
	
	#the original push was already renamed as "native Push"
	#DO NOT USE A CLOSURE WITH THIS METHOD
	def push(*items,&p)
		num = items.size();
		
		i = 0;
		while(i<num)
			self << items[i];
			i = i+1;
		end
		
		#unfortunately, some native actionscript object uses this function, thus assume that the last argument (supposedly the closure, but actionscript does not have a closure placeholder)
		if(defined? p)
			self << p;
			return self;
		end
		
		return self;
	end
	
	def rassoc(anObj)
		i = 0;
		while(i<self.size())
			if(self[i].instance_of?(Array))
				temp = self[i];
				if(temp[1]==anObj)
					return temp;
				end
			end
			i += 1;
		end
		return nil;
	end
	
	def reject!()
		i = 0;
		flag = false;
		while(i<self.size())
			if(yield(self[i]))
				self.delete(i);
				flag = true;
				#don't increment counter, cos after deletion, all elements shifted left
			else
				i += 1;
			end
		end
		if(flag)
			return self;
		else
			return nil;
		end
	end
	
	def replace(anArray)
		self.length = 0.valueOf();
		i = 0;
		while(i < anArray.size())
			self[i] = anArray[i];
			i += 1;
		end
		return self;
	end
	
	alias nativeReverse reverse;
	def reverse()
		newArray = self.dup();
		newArray.nativeReverse();
		return newArray;
	end
	
	def reverse!()
		self.nativeReverse();
		if(self.size() < 2)
			return nil;
		else
			return self;
		end
	end
	
	def reverse_each()
		i = self.size()-1;
		while(i>=0)
			yield(self[i]);
			i -= 1;
		end
		
		return self;
	end
	
	def rindex(anObj)
		i = self.size()-1;
		while(i >= 0)
			if(self[i]==anObj)
				return i;
			end
			i -= 1;
		end
		
		return nil;
	end
	
	def select()
		arr = []
		i = 0;
		while(i < self.size())
			if(yield(self[i]))
				arr << self[i];
			end
			i += 1;
		end
		return arr;
	end
	
	#length cannot be used
	def size()
		return Integer.new(self.length);
	end
	
	def uniq()
		newArray = Array.new();
		i = 0;
		while(i<self.size())
			key = self[i];
			j=0;
			newArraySize = newArray.size();
			exists = false;
			while(j<newArraySize)
				if(newArray[j]==key)
					exists = true;
					break;
				end
				j += 1;
			end
			if(!exists)
				newArray.push(key);
			end
			i += 1;
		end
		
		return newArray;
	end
	
	alias slice [];
	
	alias nativeSort sort;
	def sort(&p)
		if(defined? p)
			return self.selectionsort(&p);
		else
			return self.selectionsort(){|x,y| return x <=> y;};
		end
	end
	
	def selectionsort()
		newArray = self.dup();
		i = 0;
		while(i < newArray.size()-1)
			smallestIndex = i;
			j = i + 1;
			while(j < newArray.size())
				if(newArray[j] < newArray[smallestIndex])
					smallestIndex = j;
				end
				j += 1;
			end
			if(smallestIndex != i)
				temp = newArray[smallestIndex];
				newArray[smallestIndex] = newArray[i];
				newArray[i] = temp;
			end
			i += 1;
		end
		return newArray;
	end
	
	#not working yet
	def mergesort(l,r,&p)
		if(l > r)
			return [];
		elsif(r==l)
			return [self[r]];
		elsif(r==l+1)
			if(p.call(self[l],self[r]) < 0)
				return [self[r],self[l]];
			else
				return [self[l],self[r]];
			end
		else
			m = ((l + r)/2).to_i();
			leftPart = self.mergesort(l,m-1,&p);
			rightPart = self.mergesort(m,r,&p);
			i = 0;
			j = 0;
			newArray = [];
			while(i<leftPart.size() && j < rightPart.size())
				if(p.call(leftPart[i],rightPart[j]) < 0)
					newArray << leftPart[i];
					i +=1;
				else
					newArray << rightPart[j];
					j +=1;
				end
			end
			
			while(i < leftPart.size())
				newArray << leftPart[i];
				i += 1;
			end
			
			while(j < rightPart.size())
				newArray << rightPart[j];
				j += 1;
			end
			
			return newArray;
		end
	end
	
	def sort!()
		newArray = self.sort();
		i = 0;
		while(i<self.size())
			self[i] = newArray[i];
			i += 1;
		end
		return self;
	end
	
	def to_ary()
		return self;
	end
	
	alias to_a to_ary;
	
	alias nativeJoin join;
	
	def join(sep="")
		if(self.size()==0)
			return "";
		end
		
		str = "";
		i = 0;
		while(i < self.size()-1)
			str += self[i].to_s();
			str += sep;
			i += 1;
		end
		str += self[self.size()-1].to_s();
		return str;
	end
	
	def to_s()
		return self.join();
	end
	
	alias nativeUnshift unshift;
	def unshift(*args)
		i = self.size()+args.size()-1;
		while(i>=args.size())
			self[i] = self[i-1];
			i -= 1;
		end
		i = 0
		while(i < args.size())
			self[i] = args[i];
			i += 1;
		end
		return self;
	end
	
	def uniq!()
		newArray = self.uniq();
		if(newArray.size() == newArray.size())
			return nil;
		else
			self.length = 0.valueOf();
			i = 0;
			newArraySize = newArray.size();
			while(i<newArraySize)
				self.push(newArray[i]);
				i += 1;
			end
			return self;
		end
	end
end
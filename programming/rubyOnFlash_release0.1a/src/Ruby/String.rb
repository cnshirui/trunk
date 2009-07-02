class String
	#not implemented:
	#	crypt
	#	dump
	# 	each_byte
	#	end_regex
	#	gsub
	#	gsub!
	#	hash
	# 	hex
	#	inspect
	#	intern
	#	to_sym
	#	is_binary_data?
	#	is_complex_yaml?
	#	String#iseuc
	#	String#issjis
	#	String#isutf8
	#	jcount
	#	jlength
	#	jsize
	#	String#kconv
	#	match
	#	mbchar?
	#	oct
	
	def *(x)
		counter = 0;
		str = "";
		while(counter < x)do
			str = str + self;
			counter = counter + 1;
		end
		return str;
	end
	
	def <<(anObj)
		if(anObj.instance_of?(Integer))
			char = String.new(AS::String.fromCharCode(anObj.valueOf()));
			return self + char;
		else
			return self + anObj.to_s();
		end
	end
	
	def concat(anObj)
		if(anObj.instance_of?(Integer))
			char = String.new(AS::String.fromCharCode(anObj.valueOf()));
			return self + char;
		else
			return self + anObj.to_s();
		end
	end
	
	def <=>(anObj)
		if(self < anObj)then
			return -1;
		elsif(self == anObj)then
			return 0;
		else
			return 1;
		end
	end
	
	def ===(anObj)
		return (self == anObj);
	end
	
	def [](offset1,offset2)
		if(defined? offset2)then
			first = offset1;
			length = offset2;
			
			if(first < 0)
					first = self.length() + first;
			end
			if(length < 0)
				return nil;
			end
			if(first <0 || first >= self.length())
				return nil;
			end
			if(first+length > self.length())
				length = self.length() - first;
			end
			newStr = @value.substr(first.valueOf(),length.valueOf());
			return String.new(newStr);
		else
			if(offset1.instance_of?(Integer))then
				if(offset1 < 0)
					offset1 = self.length() + offset1;
				end
				#the offset might too negative
				if(offset1 < 0 || offset1 >= self.length())
					return nil;
				end
				return String.new(@value.charAt(offset1.valueOf()));
			elsif(offset1.instance_of?(String))
				index = Integer.new(@value.indexOf(offset1.toString(),0));
				if(index < 0)
					return nil;
				else
					return offset1;
				end
			else
				#assume range
				first = offset1.first();
				last = offset1.last();
				if(first < 0)
					first = self.length() + first;
				end
				if(last < 0)
					last = self.length() + last;
				end
				if(first <0 || first >= self.length())
					return nil;
				end
				if(last < 0)
					return nil;
				end
				if(last >= self.length())
					last = self.length() -1;
				end
				newStr = @value.substring(first.valueOf(),(last+1).valueOf());
				return String.new(newStr);
			end
		end
	end
	
	def slice(offset1,offset2)
		if(defined? offset2)then
			first = offset1;
			length = offset2;
			
			if(first < 0)
					first = self.length() + first;
			end
			if(length < 0)
				return nil;
			end
			if(first <0 || first >= self.length())
				return nil;
			end
			if(first+length > self.length())
				length = self.length() - first;
			end
			newStr = @value.substr(first.valueOf(),length.valueOf());
			return String.new(newStr);
		else
			if(offset1.instance_of?(Integer))then
				if(offset1 < 0)
					offset1 = self.length() + offset1;
				end
				#the offset might too negative
				if(offset1 < 0 || offset1 >= self.length())
					return nil;
				end
				return String.new(@value.charAt(offset1.valueOf()));
			elsif(offset1.instance_of?(String))
				index = Integer.new(@value.indexOf(offset1.toString(),0));
				if(index < 0)
					return nil;
				else
					return offset1;
				end
			else
				#assume range
				first = offset1.first();
				last = offset1.last();
				if(first < 0)
					first = self.length() + first;
				end
				if(last < 0)
					last = self.length() + last;
				end
				if(first <0 || first >= self.length())
					return nil;
				end
				if(last < 0)
					return nil;
				end
				if(first > last)
					return nil;
				end
				if(last >= self.length())
					last = self.length() -1;
				end
				newStr = @value.substring(first.valueOf(),(last+1).valueOf());
				return String.new(newStr);
			end
		end
	end
	
	def []=(arg1,arg2,arg3)
		if(defined? arg3)
			value = arg3;
			first = arg1;
			last = arg1 + arg2 - 1;
			
			if(first < 0)
				first = first + self.length();
			end
			if(last < 0)
				last = last + self.length();
			end
			if(first < 0 || first >=self.length())
				return self;
			end
			if(first > last)
				return self;
			end
			tail = self.slice(last,self.length()-last);
			if(tail==nil)
				tail = "";
			end
			@value = (self.slice(0,first) + value + tail).toString();
			return self;
		else
			#String[arg] = value
			value = arg2;
			if(arg1.instance_of?(Integer))
				index = arg1;
				if(index < 0)
					index = index + self.length();
				end
				if(index < 0 || index >= self.length())
					return self;
				end
				if(value.instance_of?(Integer))
					char = String.new(AS::String.fromCharCode(value.valueOf()));
					tail = self.slice(index+1,self.length()-index);
					if(tail==nil)
						tail = "";
					end
					@value = (slice(0,index) + char + tail).toString();
					return self;
				else
					tail = self.slice(index+1,self.length()-index);
					if(tail==nil)
						tail = "";
					end
					@value = (self.slice(0,index) + value + tail).toString();
					return self;
				end
			elsif(index.instance_of?(String))
				subIndex = Integer.new(@value.indexOf(index.toString()));
				if(subIndex < 0)
					return self;
				else
					lastIndex = subIndex + index.length() - 1;
					tail = self.slice(lastIndex,self.length()-lastIndex);
					if(tail==nil)
						tail = "";
					end
					@value = (self.slice(0,subIndex) + value + tail).toString();
					return self;
				end
			else
				first = arg1.first();
				last = arg1.last();
				if(first < 0)
					first = first + self.length();
				end
				if(last < 0)
					last = last + self.length();
				end
				if(first < 0 || first >=self.length())
					return self;
				end
				if(first > last)
					return self;
				end
				tail = self.slice(last+1,self.length()-last-1);
				if(tail==nil)
					tail = "";
				end
				@value = (self.slice(0,first) + value + tail).toString();
				return self;
			end
		end
	end
	
	def capitalize()
		if(self.length() == 0)
			return String.new("");
		end
		head = @value.charAt(0.valueOf());
		head = head.toUpperCase();
		tail = @value.substr(1.valueOf(),(self.length()-1).valueOf());
		tail = tail.toLowerCase();
		return String.new(head) + String.new(tail);
	end
	
	def capitalize!()
		newStr = self.capitalize();
		@value = newStr.toString();
		return self;
	end
	
	def casecmp(other)
		str1 = self.capitalize();
		str2 = other.capitalize();
		
		return str1 <=> str2;
	end
	
	def center(length,pad)
		if(length > self.length())
			leftPadLength = ((length - self.length())/2);
			rightPadLength = length - self.length() - leftPadLength;
			if(defined? pad)
				return (pad * (leftPadLength.to_f()/pad.length()).ceil()).slice(0,leftPadLength) + self + (pad * (rightPadLength.to_f()/pad.length()).ceil()).slice(0,rightPadLength);
			else
				return (" " * leftPadLength) + self + (" " * rightPadLength);
			end
		else
			return String.new(@value);
		end
	end
	
	def chomp(str)
		if(self.length() == 0)
			return String.new("");
		end
		if(defined? str)
			#\n, \r, \r\n
			lastCharCode = Integer.new(@value.charCodeAt((self.length()-1).valueOf()));
			if(lastCharCode==10)
				# \n
				if(Integer.new(@value.charCodeAt((self.length()-2).valueOf()))==13)
					# \r\n
					return self.slice(0,self.length()-2);
				else
					return self.slice(0,self.length()-1);
				end
			elsif(lastCharCode==13)
				# \r
				return self.slice(0,self.length()-1);
			else
				return String.new(@value);
			end
		else
			if(self.slice(self.length()-str.length()) == str)
				return self.slice(0,self.length()-str.length());
			else
				return String.new(@value);
			end
		end
	end
	
	def chomp!(str)
		temp = self.chomp();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	def chop()
		if(self.length() == 0)
			return String.new("");
		end
		lastCharCode = Integer.new(@value.charCodeAt((self.length()-1).valueOf()));
		if(lastCharCode==10)
			# \n
			if(Integer.new(@value.charCodeAt((self.length()-2).valueOf()))==13)
				# \r\n
				return self.slice(0,self.length()-2);
			else
				return self.slice(0,self.length()-1);
			end
		else
			return self.slice(0,self.length()-1);
		end
	end
	
	def chop!()
		temp = self.chop();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	#NOTE: this method can only count the occurrences of a substring.  
	#This method does not support regex, nor does it support the intersection of arguments
	def count(arg)
		currIndex = 0;
		count = 0;
		while(currIndex < self.length())
			foundIndex = Integer.new(@value.indexOf(arg.toString(),currIndex.valueOf()));
			if(foundIndex < 0)
				break;
			else
				count += 1;
				currIndex = foundIndex + arg.length();
			end
		end
		
		return count;
	end
	
	#This method does not support regex, nor does it support the intersection of arguments
	def delete(arg)
		temp = String.new(@value);
		currIndex = 0;
		while(currIndex < temp.length())
			foundIndex = Integer.new(temp.toString().indexOf(arg.toString(),currIndex.valueOf()));
			if(foundIndex < 0)
				break;
			else
				temp = temp.slice(0,foundIndex) + temp.slice(foundIndex+arg.length(),temp.length()-foundIndex-arg.length());
				currIndex = foundIndex;
			end
		end
		
		return temp;
	end
	
	def delete!(arg)
		temp = self.delete();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	def downcase()
		return String.new(@value.toLowerCase());
	end
	
	def downcase!()
		temp = self.downcase();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	def each(sep)
		if ((defined? sep) && sep.length() >0)
			currIndex = 0;
			while(currIndex < self.length())
				foundIndex = Integer.new(@value.indexOf(sep.toString(),currIndex.valueOf()));
				if(foundIndex < 0)
					yield(self.slice(currIndex,self.length()-currIndex));
					break;
				else
					yield(self.slice(currIndex,foundIndex-currIndex+sep.length()));
					currIndex = foundIndex + sep.length();
				end
			end
		else
			#use "\n"
			sep = "\n"
			currIndex = 0;
			while(currIndex < self.length())
				foundIndex = Integer.new(@value.indexOf(sep.toString(),currIndex.valueOf()));
				if(foundIndex < 0)
					yield(self.slice(currIndex,self.length()-currIndex));
					break;
				else
					while(foundIndex + 1 < self.length())
						if(Integer.new(@value.charCodeAt((foundIndex +1).valueOf())) == 10)
							foundIndex += 1;
						else
							break;
						end
					end
					
					yield(self.slice(currIndex,foundIndex-currIndex+1));
					currIndex = foundIndex + 1;
				end
			end
		end
		return self;
	end
	
	alias each_line each;
	
	def each_char()
		currIndex = 0;
		while(currIndex < self.length())
			yield(String.new(@value.charAt(currIndex.valueOf())));
			currIndex += 1;
		end
		return self;
	end
	
	def empty?()
		if(slef.length()==0)
			return true;
		else
			return false;
		end
	end
	
	def eql?(other)
		return (self == other);
	end
	
	def include?(str)
		if(str.instance_of?(Integer))
			str = String.new(AS::String.fromCharCode(str.valueOf()));
		end
		index = Integer.new(@value.indexOf(str.toString()));
		if(index < 0)
			return false;
		else
			return true;
		end
	end
	
	#does not support regular expression
	def index(arg1,offset)
		if !(defined? offset)
			offset = 0;
		end
		
		if(arg1.instance_of?(Integer))
			sub = String.new(AS::String.fromCharCode(arg1.valueOf()));
		else
			sub = arg1;
		end
		
		foundIndex = Integer.new(@value.indexOf(sub.toString(),offset.valueOf()));
		if(foundIndex < 0)
			return nil;
		else
			return foundIndex;
		end
	end
	
	def replace(other)
		@value = other.toString();
	end
	
	def insert(index,other)
		if(index < 0)
			index = self.length() + index;
		end
		
		temp = self.slice(0,index) + other + self.slice(index,self.length()-index);
		@value = temp.toString();
		
		return self;
	end
	
	def length()
		return Integer.new(@value.length);
	end
	
	def ljust(length,padstr=' ')
		if(length > self.length())
			diff = length - self.length();
			right = (padstr * (diff.to_f()/padstr.length()).ceil()).slice(0,diff);
			return (self + right);
		else
			return String.new(@value);
		end
	end
	
	def lstrip()
		nonWhitespace = 0;
		while(nonWhitespace < self.length())
			temp = String.new(@value.charAt(nonWhitespace.valueOf()));
			if(temp==" " || temp=="\t")
				nonWhitespace += 1;
			else
				break;
			end
		end
		
		if(nonWhitespace <self.length())
			return self.slice(nonWhitespace,self.length()-nonWhitespace);
		else
			return "";
		end
	end
	
	def lstrip!()
		temp = self.lstrip();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	def quote()
		if(self.length() < 2)
			return String.new(@value);
		else
			if(self[0]=='"' && self[self.length()-1] == '"')
				return String.new(@value);
			else
				return '"' + self + '"';
			end
		end
	end
	
	def reverse()
		result = "";
		i = self.length() - 1;
		while(i >= 0)
			result += self[i];
			i -= 1;
		end
		
		return result;
	end
	
	def reverse!()
		temp = self.reverse();
		@value = temp.toString();
		return self;
	end
	
	#does not support regex
	def rindex(arg,offset)
		if(self.length() == 0)
			return nil;
		end
		if !(defined? offset)
			offset = self.length()-1;
		end
		if(offset < 0)
			offset = self.length() + offset;
		end
		if(arg.instance_of?(Integer))
			sub = String.new(AS::String.fromCharCode(arg));
		else
			sub = arg;
		end
		
		foundIndex = Integer.new(@value.lastIndexOf(sub.toString(),offset));
		if(foundIndex < 0)
			return nil;
		else
			return foundIndex;
		end
	end
	
	def rjust(length,padstr=' ')
		if(length > self.length())
			diff = length - self.length();
			left = (padstr * (diff.to_f()/padstr.length()).ceil()).slice(0,diff);
			return (left + self);
		else
			return String.new(@value);
		end
	end
	
	def rstrip()
		nonWhitespace = self.length() - 1;
		while(nonWhitespace >= 0)
			temp = String.new(@value.charAt(nonWhitespace.valueOf()));
			if(temp==" " || temp=="\t")
				nonWhitespace -= 1;
			else
				break;
			end
		end
		
		if(nonWhitespace >= 0)
			return self.slice(0,nonWhitespace+1);
		else
			return "";
		end
	end
	
	def rstrip!()
		temp = self.rstrip();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	#does not support regex
	def scan(str)
		arr = [];
		currIndex = 0;
		while(currIndex < self.length())
			foundIndex = Integer.new(@value.indexOf(str.toString(),currIndex.valueOf()));
			if(foundIndex < 0)
				break;
			else
				arr << String.new(str.toString());
				currIndex = foundIndex + str.length();
			end
		end
		
		if(defined? yield)
			yield(*arr);
			return self;
		else
			return arr;
		end
	end
	
	def strip()
		result = self.lstrip();
		result = result.rstrip();
		return result;
	end
	
	def strip!()
		temp = self.strip();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	#does not support regex
	def split(pattern,limit)
		if !(defined? pattern)
			pattern = ' ';
		end
		if(defined? limit)
			if(limit < 0)
				arr = @value.split2(pattern.toString());
			else
				arr = @value.split2(pattern.toString(),limit.valueOf());
			end
		else
			arr = @value.split2(pattern.toString());
			if(Integer.new(arr[arr.size()-1].length)==0)
				arr.pop();
			end
		end
		
		ret = [];
		i = 0;
		while(i < arr.size())
			ret << String.new(arr.getAt(i));
			i += 1;
		end
		return ret;
	end
	
	#does not support regex, thus no arguments
	def squeeze()
		currIndex = 0;
		result = "";
		while(currIndex < self.length())
			charCode = Integer.new(@value.charCodeAt(currIndex.valueOf()));
			j = currIndex + 1;
			while(j < self.length())
				if(charCode == Integer.new(@value.charCodeAt(currIndex.valueOf())))
					j += 1;
				else
					break;
				end
			end
			result += String.new(AS::String.fromCharCode(charCode.valueOf()));
			currIndex = j;
		end
		return result;
	end
	
	#always base 10
	def to_i()
		temp = self.to_f();
		return temp.to_i();
	end
	
	def to_f()
		if(Object.toObject(AS::isNan(AS::Number(@value))))
			return 0;
		else
			return Float.new(AS::Number(@value));
		end
	end
	
	alias to_str to_s;
	
	def swapcase()
		result = "";
		i = 0;
		while(i < self.length())
			charCode = Integer.new(@value.charCodeAt(i.valueOf()));
			if(charCode >=65 && charCode<=90)
				result += String.new(AS::String.fromCharCode((charCode + 32).valueOf()));
			elsif(charCode >=97 && charCode <=122)
				result += String.new(AS::String.fromCharCode((charCode - 32).valueOf()));
			else
				result += String.new(AS::String.fromCharCode(charCode.valueOf()));
			end
			i += 1;
		end
	end
	
	def swapcase!()
		temp = self.swapcase();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
	
	def succ()
		if(self.length()==0)
			return "";
		end
		result = "";
		currIndex = self.length() -1;

		while(currIndex >= 0)
			charCode = Integer.new(@value.charCodeAt(currIndex.valueOf()));
			if(charCode==90)
				newChar = "A";
				carry = true;
			elsif(charCode==122)
				newChar = "a";
				carry = true;
			elsif(charCode==57)
				newChar = "0";
				carry = true;
			elsif(charCode==47)
				newChar = String.new(AS::String.fromCharCode(58.valueOf()));
				carry = false;
			elsif(charCode==64)
				newChar = String.new(AS::String.fromCharCode(91.valueOf()));
				carry = false;
			elsif(charCode==96)
				newChar = String.new(AS::String.fromCharCode(123.valueOf()));
				carry = false;
			elsif(charCode==126)
				newChar = String.new(AS::String.fromCharCode(32.valueOf()));
				carry = true;
			else
				newChar = String.new(AS::String.fromCharCode((charCode + 1).valueOf()));
				carry = false;
			end
			
			result = newChar + result;
			
			if(carry)
				currIndex -= 1;
			else
				break;
			end
		end
		
		if(currIndex<0)
			return newChar + result;
		elsif(currIndex==0)
			return result;
		else
			return self.slice(0,currIndex)+result;
		end
		
	end
	
	def succ!()
		temp = self.succ();
		@value = temp.toString();
		return self;
	end
	
	alias next succ;
	
	alias next! succ!;
	
	def upto(str)
		temp = self;
		while(temp <= str)
			yield(temp);
			temp = temp.succ();
		end
		return self
	end
	
	def upcase()
		return String.new(@value.toUpperCase());
	end
	
	def upcase!()
		temp = self.upcase();
		if(temp == self)
			return nil;
		else
			@value = temp.toString();
			return self;
		end
	end
end
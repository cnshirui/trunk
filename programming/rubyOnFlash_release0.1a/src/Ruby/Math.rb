module Math
	self.E = Float.new(AS::Math.E);
	self.PI = Float.new(AS::Math.PI);
	
	def self.acos(aNumeric)
		return Float.new(AS::Math.acos(aNumeric.valueOf()));
	end
	
	def self.asin(aNumeric)
		return Float.new(AS::Math.asin(aNumeric.valueOf()));
	end
	
	def self.atan(aNumeric)
		return Float.new(AS::Math.atan(aNumeric.valueOf()));
	end
	
	def self.atan2(y,x)
		return Float.new(AS::Math.atan2(y.valueOf(),x.valueOf()));
	end
	
	def self.cos(aNumeric)
		return Float.new(AS::Math.cos(aNumeric.valueOf()));
	end
	
	def self.exp(aNumeric)
		return Float.new(AS::Math.exp(aNumeric.valueOf()));
	end
	
	def self.hypot(x,y)
		return Float.new(AS::Math.sqrt(((x**2) + (y**2)).valueOf()));
	end
	
	def self.ldexp(aFloat,anInteger)
		return aFloat * (2**anInteger);
	end
	
	def self.log(aNumeric)
		return Float.new(AS::Math.log(aNumeric.valueOf()));
	end
	
	def self.log10(aNumeric)
		return Float.new(AS::Math.log(aNumeric.valueOf())) / Float.new(AS::Math.LN10);
	end
	
	def self.sin(aNumeric)
		return Float.new(AS::Math.sin(aNumeric.valueOf()));
	end
	
	def self.sqrt(aNumeric)
		return Float.new(AS::Math.sqrt(aNumeric.valueOf()));
	end
	
	def self.tan(aNumeric)
		return Float.new(AS::Math.tan(aNumeric.valueOf()));
	end
	
	def self.cosh(x)
		return ((self.exp(x) + self.exp(-x))/2.0);
	end
	
	def self.sinh(x)
		return ((self.exp(x) - self.exp(-x))/2.0);
	end
	
	def self.tanh(x)
		return (self.sinh(x)/self.cosh(x));
	end
	
	def self.acosh(x)
		return self.log(x + self.sqrt(x+1) * self.sqrt(x-1));
	end
	
	def self.asinh(x)
		return self.log(x + self.sqrt(x*x+1));
	end
	
	def self.atanh(x)
		return (self.log(1+x) - self.log(1-x))/2.0;
	end
	
	def self.frexp(aNumeric)
		if(aNumeric == 0)
			return [0.0,0];
		else
			if(aNumeric.abs() > 1)
				fraction = aNumeric;
				exponent = 0;
				while(fraction.abs() > 1)
					fraction = fraction / 2.0;
					exponent += 1;
				end
				return [fraction,exponent];
			else
				fraction = aNumeric;
				exponent = 0;
				while(fraction.abs() <= 1)
					fraction = fraction * 2.0;
					exponent -= 1;
				end
				fraction = fraction / 2.0;
				exponent = exponent + 1;
				return [fraction,exponent];
			end
		end
	end
end
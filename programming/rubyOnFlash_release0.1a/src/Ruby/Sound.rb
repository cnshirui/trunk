class Sound
	#properties
	def getDuration()
		return Integer.new(self.duration);
	end
	
	def getId3()
		return self.id3;
	end
	
	def getPosition()
		return self.position;
	end
	
	alias nativeAttachSound attachSound;
	def attachSound(id)
		return self.nativeAttachSound(id.toString());
	end
	
	alias nativeGetBytesLoaded getBytesLoaded;
	def getBytesLoaded()
		return Integer.new(self.nativeGetBytesLoaded());
	end
	
	alias nativeGetBytesTotal getBytesTotal;
	def getBytesTotal()
		return Integer.new(self.nativeGetBytesTotal());
	end
	
	alias nativeGetPan getPan;
	def getPan()
		return Integer.new(self.nativeGetPan());
	end
	
	alias nativeGetVolume getVolume;
	def getVolume()
		return Integer.new(self.nativeGetVolume());
	end
	
	alias nativeLoadSound loadSound;
	def loadSound(url,streaming)
		return self.nativeLoadSound(url.toString(),streaming.valueOf());
	end
	
	alias nativeSetPan setPan;
	def setPan(pan)
		temp = self.nativeSetPan(pan.valueOf());
		return Integer.new(temp);
	end
	
	alias nativeSetVolume setVolume;
	def setVolume(number)
		return self.nativeSetVolume(number.valueOf());
	end
	
	alias nativeStart start;
	def start(*args)
		if(args.size() == 0)
			return self.nativeStart(0,1);
		elsif(args.size == 1)
			return self.nativeStart(args[0].valueOf(),1);
		else
			return self.nativeStart(args[0].valueOf(),args[1].valueOf());
		end
	end
	
	alias nativeGetTransform getTransform;
	def getTransform()
		obj = self.nativeGetTransform();
		ret = Object.new();
		ret.ll = Integer.new(obj.ll);
		ret.lr = Integer.new(obj.lr);
		ret.rl = Integer.new(obj.rl);
		ret.rr = Integer.new(obj.rr);
		
		return ret;
	end
	
	alias nativeSetTransform setTransform;
	def setTransform(obj)
		ret = Object.new();
		ret.ll = obj.ll.valueOf();
		ret.lr = obj.lr.valueOf();
		ret.rl = obj.rl.valueOf();
		ret.rr = obj.rr.valueOf();
		
		return self.nativeSetTransform(ret);
	end
end
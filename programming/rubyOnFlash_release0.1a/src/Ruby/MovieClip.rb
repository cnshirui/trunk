class MovieClip
	#getters, setters
	def getAlpha()
		return Float.new(self._alpha);
	end
	
	def setAlpha(alpha)
		self._alpha = alpha.valueOf();
	end
	
	def getX()
		return Float.new(self._x);
	end
	
	def setX(x)
		self._x = x.valueOf();
	end
	
	def getY()
		return Float.new(self._y);
	end
	
	def setY(y)
		self._y = y.valueOf();
	end
	
	def getDropTarget()
		return String.new(self._droptarget);
	end
	
	def isEnabled?()
		return Object.toObject(self.enabled);
	end
	
	def setEnabled?(enabled)
		self.enabled = enabled.valueOf();
	end
	
	def isFocusEnabled?()
		return Object.toObject(self.focusEnabled);
	end
	
	def setFocusEnabled?(enabled)
		self.focusEnabled = enabled.valueOf();
	end
	
	def isFocusRect?()
		return Object.toObject(self._focusrect);
	end
	
	def setFocusRect?(flag)
		self._focusrect = flag.valueOf();
	end
	
	def getHeight()
		return Float.new(self._height);
	end
	
	def setHeight(height)
		self._height = height.valueOf();
	end
	
	def getHitArea()
		return self.hitArea;
	end
	
	def setHitArea(ha)
		self.hitArea = ha;
	end
	
	def isLockRoot?()
		return Object.toObject(self._lockroot);
	end
	
	def setLockRoot?(flag)
		self._lockroot = flag.valueOf();
	end
	
	def getMenu()
		return self.menu;
	end
	
	def setMenu(menu)
		self.menu = menu;
	end
	
	def getName()
		return String.new(self._name);
	end
	
	def setName(name)
		self._name = name.toString();
	end
	
	def getParent()
		return self._parent;
	end
	
	def setParent(parent)
		self._parent = parent;
	end
	
	def getQuality()
		return String.new(self._quality);
	end
	
	def setQuality(quality)
		self._quality = quality.toString();
	end
	
	def getRotation()
		return Float.new(self._rotation);
	end
	
	def setRotation(rotation)
		self._rotation = rotation.valueOf();
	end
	
	def getSoundBufTime()
		return Integer.new(self._soundbuftime);
	end
	
	def setSoundBufTime(soundBufTime)
		self._soundbuftime = soundBufTime.valueOf();
	end
	
	def isTabChildren?()
		return Object.new(self.tabChildren);
	end
	
	def setTabChildren?(flag)
		self.tabChildren = flag.valueOf();
	end
	
	def istTabEnabled?()
		return Object.toObject(self.tabEnabled);
	end
	
	def setTabEnabled?(flag)
		self.tabEnabled = flag.valueOf();
	end
	
	def getTabIndex()
		if(self.tabIndex)
			return Integer.new(self.tabIndex);
		else
			#undefined
			return self.tabIndex;
		end
	end
	
	def setTabIndex(flag)
		self.tabIndex = flag.valueOf();
	end
	
	def getTarget()
		return String.new(self._target);
	end
	
	def isTrackAsMenu?()
		return Object.toObject(self.trackAsMenu);
	end
	
	def setTrackAsMenu?(flag)
		self.trackAsMenu = flag.valueOf();
	end
	
	def getUrl()
		return String.new(self._url);
	end
	
	def isUseHandCursor?()
		return Object.toObject(self.useHandCursor);
	end
	
	def setUseHandCursor?(flag)
		self.useHandCursor = flag.valueOf();
	end
	
	def isVisible?()
		return Object.toObject(self._visible);
	end
	
	def setVisible?(flag)
		self._visible = flag.valueOf();
	end
	
	def getXMouse()
		return Integer.new(self._xmouse);
	end
	
	def getXScale()
		return Float.new(self._xscale);
	end
	
	def setXScale(scale)
		self._xscale = scale.valueOf();
	end
	
	def getYMouse()
		return Integer.new(self._ymouse);
	end
	
	def getYScale()
		return Float.new(self._yscale);
	end
	
	def setYScale(scale)
		self._yscale = scale.valueOf();
	end
	
	def setPosition(x,y)
		self._x = x.valueOf();
		self._y = y.valueOf();
	end
	
	alias nativeAttachMovie attachMovie;
	def attachMovie(idName,newName,depth,initObj)
		if(defined? initObj)
			return self.nativeAttachMovie(idName.toString(),newName.toString(),depth.valueOf(),initObj);
		else
			return self.nativeAttachMovie(idName.toString(),newName.toString(),depth.valueOf());
		end
	end
	
	alias nativeCreateEmptyMovieClip createEmptyMovieClip;
	def createEmptyMovieClip(newName,depth)
		return self.nativeCreateEmptyMovieClip(newName.toString(),depth.valueOf());
	end
	
	alias nativeCreateTextField createTextField;
	def createTextField(instanceName, depth, x, y, width, height)
		return self.nativeCreateTextField(instanceName.toString(),depth.valueOf(),x.valueOf(),y.valueOf(),width.valueOf(),height.valueOf());
	end
	
	alias nativeDuplicateMovieClip duplicateMovieClip;
	def duplicateMovieClip(newName,depth,initObj)
		if(defined? initObj)
			return self.nativeDuplicateMovieClip(newName.toString(),depth.valueOf(),initObj);
		else
			return self.nativeDuplicateMovieClip(newName.toString(),depth.valueOf());
		end
	end
	
	alias nativeGetBounds getBounds;
	def getBounds(obj)
		temp = self.nativeGetBounds(obj);
		result = Object.new();
		result.xMin = Float.new(temp.xMin);
		result.xMax = Float.new(temp.xMax);
		result.yMin = Float.new(temp.yMin);
		result.yMax = Float.new(temp.yMax);
		return result;
	end
	
	alias nativeGetBytesLoaded getBytesLoaded;
	def getBytesLoaded()
		temp = self.nativeGetBytesLoaded();
		return Integer.new(temp);
	end
	
	alias nativeGetDepth getDepth;
	def getDepth()
		temp = self.nativeGetDepth();
		return Integer.new(temp);
	end
	
	alias nativeGetInstanceAtDepth getInstanceAtDepth;
	def getInstanceAtDepth(depth)
		return self.nativeGetInstanceAtDepth(depth.valueOf());
	end
	
	alias nativeGetNextHighestDepth getNextHighestDepth;
	def getNextHighestDepth()
		return Integer.new(self.nativeGetNextHighestDepth());
	end
	
	alias nativeGetSWFVersion getSWFVersion;
	def getSWFVersion()
		return Integer.new(self.nativeGetSWFVersion());
	end
	
	alias nativeGetUrl getUrl;
	def getUrl(url, window, variables)
		if(defined? window)
			if(defined? variables)
				return self.nativeGetUrl(url.toString(),window.toString(),variables.toString());
			else
				return self.nativeGetUrl(url.toString(),window.toString());
			end
		else
			return self.nativeGetUrl(url.toString());
		end
	end
	
	alias nativeGlobalToLocal globalToLocal;
	def globalToLocal(obj)
		input = Object.new();
		input.x = obj.x.valueOf();
		input.y = obj.y.valueOf();
		temp = self.nativeGlobalToLocal(input);
		res = Object.new();
		res.x = Integer.new(temp.x);
		res.y = Integer.new(temp.y);
		
		return res;
	end
	
	alias nativeHitTest hitTest;
	def hitTest(*args)
		if(args.size()==1)
			return Object.toObject(Object.nativeMethodCall(self,"nativeHitTest",args[0]));
		else
			#assume 3 arguments, x,y,shapeFlag
			return Object.toObject(self.nativeHitTest(args[0].valueOf(),args[1].valueOf(),args[2].valueOf()));
		end
	end
	
	
	alias nativeLoadMovie loadMovie;
	def loadMovie(url,variables)
		if(defined? variables)
			return self.nativeLoadMovie(url.toString(),variables.toString());
		else
			return self.nativeLoadMovie(url.toString());
		end
	end
	
	alias nativeLoadVariables loadVariables;
	def loadVariables(url,variables)
		if(defined? variables)
			return self.nativeLoadVariables(url.toString(),variables.toString());
		else
			return self.nativeLoadVariables(url.toString());
		end
	end
	
	alias nativeLocalToGlobal localToGlobal;
	def localToGlobal(obj)
		input = Object.new();
		input.x = obj.x.valueOf();
		input.y = obj.y.valueOf();
		temp = self.nativeLocalToGlobal(input);
		res = Object.new();
		res.x = Integer.new(temp.x);
		res.y = Integer.new(temp.y);
		
		return res;
	end
	
	alias nativeStartDrag startDrag;
	def startDrag(lock,*coords)
		if(defined? lock)
			if(coords.size()>0)
				return self.nativeStartDrag(lock.valueOf(),coords[0].valueOf(),coords[1].valueOf(),coords[2].valueOf(),coords[3].valueOf());
			else
				return self.nativeStartDrag(lock.valueOf());
			end
		else
			return self.nativeStartDrag();
		end
	end
	
	alias nativeSwapDepths swapDepths;
	def swapDepths(arg)
		if(arg.instance_of(String))
			return self.nativeSwapDepths(arg.toString());
		else
			return self.nativeSwapDepths(arg.valueOf());
		end
	end
	
	alias nativeBeginFill beginFill;
	def beginFill(rgb,alpha)
		if(defined? rgb)
			if(defined? alpha)
				return self.nativeBeginFill(rgb.valueOf(),alpha.valueOf());
			else
				return self.nativeBeginFill(rgb.valueOf(),100.valueOf());
			end
		end
	end
	
	alias nativeBeginGradientFill beginGradientFill;
	def beginGradientFill(fillType,colors,alphas,ratios,matrix)
		newColors = [];
		i=0;
		while(i<colors.size())
			newColors[i] = colors[i].valueOf();
			i += 1;
		end
		
		newAlphas = [];
		i=0;
		while(i<alphas.size())
			newAlphas[i] = alphas[i].valueOf();
			i += 1;
		end
		
		newRatios = [];
		i=0;
		while(i<ratios.size())
			newRatios[i] = ratios[i].valueOf();
			i += 1;
		end
		
		newMatrix = Object.new();
		if(matrix.matrixType)
			newMatrix.x = matrix.x.valueOf();
			newMatrix.y = matrix.y.valueOf();
			newMatrix.w = matrix.w.valueOf();
			newMatrix.h = matrix.h.valueOf();
			newMatrix.r = matrix.r.valueOf();
			newMatrix.matrixType = matrix.matrixType.toString();
		else
			newMatrix.a = matrix.a.valueOf();
			newMatrix.b = matrix.b.valueOf();
			newMatrix.c = matrix.c.valueOf();
			newMatrix.d = matrix.d.valueOf();
			newMatrix.e = matrix.e.valueOf();
			newMatrix.f = matrix.f.valueOf();
			newMatrix.g = matrix.g.valueOf();
			newMatrix.h = matrix.h.valueOf();
			newMatrix.i = matrix.i.valueOf();
		end
		
		return self.nativeBeginGradientFill(fillType.toString(),newColors,newAlphas,newRatios,newMatrix);
	end
	
	alias nativeCurveTo curveTo;
	def curveTo(controlX,controlY,anchorX,anchorY)
		return self.nativeCurveTo(controlX.valueOf(),controlY.valueOf(),anchorX.valueOf(),anchorY.valueOf());
	end
	
	alias nativeLineStyle lineStyle;
	def lineStyle(*args)
		if(args.size() == 1)
			return self.nativeLineStyle(args[0].valueOf(),0.valueOf(),100.valueOf());
		elsif(args.size()==2)
			return self.nativeLineStyle(args[0].valueOf(),args[1].valueOf(),100.valueOf());
		elsif(args.size()==3)
			return self.nativeLineStyle(args[0].valueOf(),args[1].valueOf(),args[2].valueOf());
		end
	end
	
	alias nativeLineTo lineTo;
	def lineTo(x,y)
		return self.nativeLineTo(x.valueOf(),y.valueOf());
	end
	
	alias nativeMoveTo moveTo;
	def moveTo(x,y)
		return self.nativeMoveTo(x.valueOf(),y.valueOf());
	end
end
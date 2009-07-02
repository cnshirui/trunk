class TextField
	#properties
	def getAlpha()
		return Float.new(self._alpha);
	end
	
	def setAlpha(alpha)
		self._alpha = alpha.valueOf();
	end
	
	def getAutoSize()
		temp = self.autoSize;
		if(Object.primitiveEqual?(temp,true.valueOf()))
			return true;
		elsif(Object.primitiveEqual?(temp,false.valueOf()))
			return false;
		else
			return String.new(self.autoSize);	
		end
	end
	
	def setAutoSize(aString)
		self.autoSize = aString.valueOf();
	end
	
	def hasBackground?()
		return Object.toObject(self.background);
	end
	
	def setBackground?(flag)
		self.background = flag.valueOf();
	end
	
	def getBackgroundColor()
		return Integer.new(self.backgroundColor);
	end
	
	def setBackgroundColor(color)
		self.backgroundColor = color.valueOf();
	end
	
	def hasBorder?()
		return Object.toObject(self.border);
	end
	
	def setBorder?(border)
		self.border = border.valueOf();
	end
	
	def getBorderColor()
		return Integer.new(self.borderColor);
	end
	
	def setBorderColor(color)
		self.borderColor = color.valueOf();
	end
	
	def getBottomScroll()
		return Integer.new(self.bottomScroll);
	end
	
	def isEmbedFonts?()
		return Object.toObject(self.embedFonts);
	end
	
	def setEmbedFonts?(flag)
		self.embedFonts = flag.valueOf();
	end
	
	def getHeight()
		return Float.new(self._height);
	end
	
	def setHeight(height)
		self._height = height.valueOf();
	end
	
	def getHScroll()
		return Integer.new(self.hScroll);
	end
	
	def setHScroll(val)
		self.hScroll = val.valueOf();
	end
	
	def isHtml?()
		return Object.toObject(self.html);
	end
	
	def setHtml?(flag)
		self.html = flag.valueOf();
	end
	
	def getHtmlText()
		return String.new(self.htmlText);
	end
	
	def setHtmltext(aString)
		self.htmlText = aString.toString();
	end
	
	def getLength()
		return Integer.new(self.length);
	end
	
	def getMaxChars()
		if(Object.primitiveEqual?(self.maxChars,nil.valueOf()))then
			return nil;
		else
			return Integer.new(self.maxChars);
		end
	end
	
	def setMaxChars(max)
		self.maxChars = max.valueOf();
	end
	
	def getMaxHScroll()
		return Integer.new(self.maxhscroll);
	end
	
	def getMaxScroll()
		return Integer.new(self.maxscroll);
	end
	
	def getMenu()
		return self.menu;
	end
	
	def setMenu(menu)
		self.menu = menu;
	end
	
	def isMouseWheelEnabled?()
		return Object.toObject(self.mouseWheelEnabled);
	end
	
	def setMouseWheelEnabled?(flag)
		self.mouseWheelEnabled = flag.valueOf();
	end
	
	def isMultiline?()
		return Object.toObject(self.multiline);
	end
	
	def setMultiline?(flag)
		self.multiline = flag.valueOf();
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
	
	def isPassword?()
		return Object.toObject(self.password);
	end
	
	def setPassword?(flag)
		self.password = flag.valueOf();
	end
	
	def getQuality()
		return String.new(self._quality);
	end
	
	def setQuality(aString)
		self._quality = aString.toString();
	end
	
	def getRestrict()
		if(Object.primitiveEqual?(self.restrict,nil.valueOf()))then
			return nil;
		else
			String.new(self.restrict);
		end
	end
	
	def setRestrict(val)
		self.restrict = val.valueOf();
	end
	
	def getRotation()
		return Float.new(self._rotation);
	end
	
	def setRotation(degree)
		self._rotation = degree.valueOf();
	end
	
	def getScroll()
		return Integer.new(self.scroll);
	end
	
	def setScroll(scroll)
		self.scroll = scroll.valueOf();
	end
	
	def isSelectable?()
		return Object.toObject(self.selectable);
	end
	
	def setSelectable?(flag)
		self.selectable = flag.valueOf();
	end
	
	def isTabEnabled?()
		if(Object.primitiveEqual?(self.tabEnabled,nil.valueOf()))
			return nil.valueOf();#return AS null, which is Ruby undefined
		else
			return Object.toObject(self.tabEnabled);
		end
	end
	
	def setTabEnabled?(flag)
		self.tabEnabled = flag.valueOf();
	end
	
	def getTabIndex()
		if(Object.primitiveEqual?(self.tabIndex,nil.valueOf()))
			return nil.valueOf();
		else
			return Integer.new(self.tabIndex);
		end
	end
	
	def setTabIndex(tabIndex)
		self.tabIndex = tabIndex.valueOf();
	end
	
	def getTarget()
		return String.new(self._target);
	end
	
	def getText()
		return String.new(self.text);
	end
	
	def setText(text)
		self.text = text.toString();
	end
	
	def getTextColor()
		return Integer.new(self.textColor);
	end
	
	def setTextColor(color)
		self.textColor = color.valueOf();
	end
	
	def getTextHeight()
		return Float.new(self.textHeight);
	end
	
	def setTextHeight(height)
		self.textHeight = height.valueOf();
	end
	
	def getTextWidth()
		return Float.new(self.textWidth);
	end
	
	def setTextWidth(width)
		self.textWidth = width.valueOf();
	end
	
	def getType()
		return String.new(self.type);
	end
	
	def setType(type)
		self.type = type.toString();
	end
	
	def getUrl()
		return String.new(self._url);
	end
	
	def getVariable()
		return String.new(self.variable);
	end
	
	def setVariable(varName)
		self.variable = varName.toSTring();
	end
	
	def isVisible?()
		return Object.toObject(self._visible);
	end
	
	def setVisible?(flag)
		self._visible = flag.valueOf();
	end
	
	def getWidth()
		return Float.new(self._width);
	end
	
	def setWidth(width)
		self._width = width.valueOf();
	end
	
	def hasWordWrap?()
		return Object.toObject(self.wordWrap);
	end
	
	def setWordWrap?(flag)
		self.wordWrap = flag.valueOf();
	end
	
	def getX()
		return Float.new(self._x);
	end
	
	def setX(x)
		self._x = x.valueOf();
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
	
	def getY()
		return Float.new(self._y);
	end
	
	def setY(y)
		self._y = y.valueOf();
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
	
	self.nativeGetFontList = self.getFontList;
	def self.getFontList()
		fontList = self.nativeGetFontList();
		temp = [];
		i = 0;
		size = fontList.size();
		while(i < size)
			temp << String.new(fontList[i]);
			i+=1;
		end
		return temp;
	end
	
	alias nativeGetDepth getDepth;
	def getDepth()
		return Integer.new(self.naticeGetDepth());
	end
	
	alias nativeRemoveListener removeListener;
	def removeListener(listener)
		return Object.toObject(self.nativeRemoveListener(listener));
	end
	
	alias nativeReplaceSel replaceSel;
	def replaceSel(text)
		self.nativeReplaceSel(text.toString());
	end
	
	alias nativeReplaceText replaceText;
	def replaceText(beginIndex, endIndex, text)
		self.nativeReplaceText(beginIndex.valueOf(),endIndex.valueOf(),text.toString());
	end
end
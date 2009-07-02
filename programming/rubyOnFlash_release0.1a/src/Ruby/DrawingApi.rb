class MovieClip
	def drawSector(centerX,centerY,angle1, angle2,radius)
		diff = angle2 - angle1;
		
		pi = Math.PI;
		if(diff < 22.5)
			phi = diff * pi/180.0;
		elsif(diff < 45)
			phi = (diff/4.0) * pi/180.0;
		elsif(diff < 90)
			phi = (diff/8.0) * pi/180.0;
		elsif(diff < 180)
			phi = (diff/16.0) * pi/180.0;
		else
			phi = (diff/32.0) * pi/180.0;
		end
		
		rad1 = angle1*pi/180.0;
		rad2 = angle2*pi/180.0;
		
		cosVal = Math.cos(rad1);
		sinVal = Math.sin(rad1);
		startX = radius * cosVal + centerX;
		startY = radius * sinVal + centerY;
		
		self.moveTo(startX,startY);
		
		theta=rad1+phi
		while(theta<rad2)
			cosVal = Math.cos(theta);
			sinVal = Math.sin(theta);
			endX = radius * cosVal + centerX;
			endY = radius * sinVal + centerY;
			
			tanVal = Math.tan(phi/2.0);
			cosVal = Math.cos(theta-(pi/2.0));
			sinVal = Math.sin(theta-(pi/2.0));
			cX = endX + radius * tanVal * cosVal;
			cY = endY + radius * tanVal * sinVal;
			
			self.curveTo(cX,cY,endX,endY);
			
			theta = theta + phi;
		end
		
		if(diff==360)
			cosVal = Math.cos(theta);
			sinVal = Math.sin(theta);
			endX = radius * cosVal + centerX;
			endY = radius * sinVal + centerY;
			
			tanVal = Math.tan(phi/2.0);
			cosVal = Math.cos(theta-(pi/2.0));
			sinVal = Math.sin(theta-(pi/2.0));
			cX = endX + radius * tanVal * cosVal;
			cY = endY + radius * tanVal * sinVal;
			
			self.curveTo(cX,cY,endX,endY);
		else
			self.lineTo(centerX,centerY);
			self.lineTo(startX,startY);
		end
	end
	
	def drawSector2(centerX,centerY,angle1, angle2,radius,centerX2,centerY2)
		diff = angle2 - angle1;
		
		pi = Math.PI;
		if(diff < 22.5)
			phi = diff * pi/180.0;
		elsif(diff < 45)
			phi = (diff/4.0) * pi/180.0;
		elsif(diff < 90)
			phi = (diff/8.0) * pi/180.0;
		elsif(diff < 180)
			phi = (diff/16.0) * pi/180.0;
		else
			phi = (diff/32.0) * pi/180.0;
		end
		
		rad1 = angle1*pi/180.0;
		rad2 = angle2*pi/180.0;
		
		cosVal = Math.cos(rad1);
		sinVal = Math.sin(rad1);
		startX = radius * cosVal + centerX;
		startY = radius * sinVal + centerY;
		
		self.moveTo(startX,startY);
		
		theta=rad1+phi
		while(theta<rad2)
			cosVal = Math.cos(theta);
			sinVal = Math.sin(theta);
			endX = radius * cosVal + centerX;
			endY = radius * sinVal + centerY;
			
			tanVal = Math.tan(phi/2.0);
			cosVal = Math.cos(theta-(pi/2.0));
			sinVal = Math.sin(theta-(pi/2.0));
			cX = endX + radius * tanVal * cosVal;
			cY = endY + radius * tanVal * sinVal;
			
			self.curveTo(cX,cY,endX,endY);
			
			theta = theta + phi;
		end
		
		if(diff==360)
			cosVal = Math.cos(theta);
			sinVal = Math.sin(theta);
			endX = radius * cosVal + centerX;
			endY = radius * sinVal + centerY;
			
			tanVal = Math.tan(phi/2.0);
			cosVal = Math.cos(theta-(pi/2.0));
			sinVal = Math.sin(theta-(pi/2.0));
			cX = endX + radius * tanVal * cosVal;
			cY = endY + radius * tanVal * sinVal;
			
			self.curveTo(cX,cY,endX,endY);
		else
			self.lineTo(centerX2,centerY2);
			self.lineTo(startX,startY);
		end
	end
	
	def drawRect(left,top,right,bottom)
		self.moveTo(left,top);
		self.lineTo(right,top);
		self.lineTo(right,bottom);
		self.lineTo(left,bottom);
		self.lineTo(left,top);
	end
	
	def drawPolygon(*coords)
		pairs = coords.size()/2.0;
		self.moveTo(coords[0],coords[1]);
		i = 1;
		while(i<pairs)
			self.lineTo(coords[i*2],coords[i*2+1]);
			i = i+1;
		end
		
		self.lineTo(coords[0],coords[1]);
	end
	
	def drawEllipse(left,top,right,bottom)
		width = right - left;
		height = bottom - top;
		
		centerX = (left + right)/2.0;
		centerY = (top+bottom)/2.0;
		
		if(width>height)
			widthFactor = 1.0;
			heightFactor = height*1.0/width;
			radius = width/2.0;
		else
			heightFactor = 1.0;
			widthFactor = width*1.0/height;
			radius = height/2.0;
		end
		
		pi = Math.PI;
		phi = pi /8.0;
		
		startX = radius * 1.0*widthFactor + centerX;
		startY = centerY;
		
		self.moveTo(startX,startY);
		
		theta=phi;
		while(theta<2*pi)
			endX = radius * Math.cos(theta);
			endY = radius * Math.sin(theta);
			
			cX = endX + radius * Math.tan(phi/2.0) * Math.cos(theta-(pi/2.0));
			cY = endY + radius * Math.tan(phi/2.0) * Math.sin(theta-(pi/2.0));
			
			self.curveTo(cX*widthFactor + centerX,cY*heightFactor+centerY,endX*widthFactor + centerX,endY*heightFactor+centerY);
			theta= theta + phi;
		end
		
		endX = radius * Math.cos(theta);
		endY = radius * Math.sin(theta);
			
		cX = endX + radius * Math.tan(phi/2.0) * Math.cos(theta-(pi/2.0));
		cY = endY + radius * Math.tan(phi/2.0) * Math.sin(theta-(pi/2.0));
		self.curveTo(cX*widthFactor + centerX,cY*heightFactor+centerY,endX*widthFactor + centerX,endY*heightFactor+centerY);
	end
end
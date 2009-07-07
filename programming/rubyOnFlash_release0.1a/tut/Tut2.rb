load "DrawingApi.rb";
#Objective of Tut 2: Using the Drawing API, Use of Mouse

#Draw Rect
self.lineStyle(0, "0x0000FF", 100);
self.beginFill("0xFFFF00",100);
self.drawRect(0,0,100,100);
self.endFill();
#DrawSector
self.beginFill("0xFFFF00",100);
self.drawSector(200,50,0,360,50);
self.endFill();
#Draw Polygon
self.beginFill("0xFFFF00",100);
self.drawPolygon(400,0,450,0,480,30,450,60,400,60,370,30);
self.endFill();
#Draw Ellipse
self.beginFill("0xFFFF00",100);
self.drawEllipse(200,200,300,250);
self.endFill();

#Draw lines
self.moveTo(0,0);
def onMouseUp()
	self.lineStyle(0, "0x0000FF", 100);
	self.lineTo(self.getXMouse(),self.getYMouse());
end
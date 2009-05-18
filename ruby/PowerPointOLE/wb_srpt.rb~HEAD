require "win32ole" 

ppt = WIN32OLE.new("PowerPoint.Application") 
ppt.Visible = true
pres = ppt.Presentations.OpenOld "d:/Workplace/TestOLE/wbs.ppt"
slds = pres.slides
#sld = slds.Add(slds.Count+1, 2)
sld = slds.Item(1)
shps = sld.Shapes
#puts shps.Count
shp = shps.Item(3)
shp.Duplicate
puts sld.ole_func_methods
#puts "---------------"
#puts shp.OLEFormat.Object.ole_func_methods
#puts "---------------"
#puts shp.HasDiagramNode



#
#sld.Shapes.AddOLEObject(0, 0, pres.PageSetup.SlideWidth,pres.PageSetup.SlideHeight-30, "Shell.Explorer.2")
#puts sld.Shapes(3).OLEFormat.ProgID
#puts sld.Shapes(3).OLEFormat.Object.ole_methods
#
#wb = sld.Shapes(3).OLEFormat.Object
#ev = WIN32OLE_EVENT.new(wb, 'DWebBrowserEvents2')
#ev.on_event("DocumentComplete") { |pDisp, URL|
#   if URL == ""
#      varURL = "http://www.microsoft.com"
#      WebBrowser1.Navigate varURL
#   end
#}
# 
#pres.SaveAs("#{Dir.pwd}/wb.ppt")
puts  "after active it..."


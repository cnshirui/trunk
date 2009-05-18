require "win32ole" 

ppt = WIN32OLE.new("PowerPoint.Application") 
pres = ppt.Presentations.add
slds = pres.slides
sld = slds.Add(slds.Count+1, 2)
shps = sld.Shapes

sld.Shapes.AddOLEObject(0, 0, pres.PageSetup.SlideWidth,pres.PageSetup.SlideHeight-30, "Shell.Explorer.2")
puts sld.Shapes(3).OLEFormat.ProgID
puts sld.Shapes(3).OLEFormat.Object.ole_put_methods
puts sld.Shapes(3).OLEFormat.Object.LocationURL = "http://bbs.nju.edu.cn"

wb = sld.Shapes(3).OLEFormat.Object
ev = WIN32OLE_EVENT.new(wb, 'DWebBrowserEvents2')
ev.on_event("DocumentComplete") { |pDisp, URL|
   if URL == ""
      varURL = "http://www.microsoft.com"
      wb.Navigate varURL
   end
}

pres.SaveAs("#{Dir.pwd}/wb.ppt")
puts  "after active it..."

#$LOOP = true
#while($LOOP)
#  WIN32OLE_EVENT.message_loop
#end
#


#
#def wb.DocumentComplete()#(ByVal pDisp As Object, URL As Variant)
#  varURL = "http://www.microsoft.com"
#  wb.Navigate varURL
#end





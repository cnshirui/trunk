require "win32ole" 


begin 
      ppt = WIN32OLE.connect("PowerPoint.Application") 
rescue 
      ppt = WIN32OLE.new("PowerPoint.Application") 
end 

pres = ppt.Presentations.add
slds = pres.slides
sld = slds.Add(slds.Count+1, 2)
shps = sld.Shapes

#flashTypeVerbs = sld1.Shapes(3).OLEFormat.ObjectVerbs
#puts sld1.Shapes(3).OLEFormat.ObjectVerbs.Count
#puts ShockwaveFlash.ShockwaveFlash.ole_methos
#puts sld1.Shapes(3).ole_methods
#sld1.Shapes.AddOLEObject(411, 26, 289, 238, nil, "D:/Workplace/TestOLE/linechart.swf", false)
#sld1.Shapes.AddOLEObject(411, 26, 289, 238, nil, "D:\\Workplace\\TestOLE\\linechart.swf")# , false, nil, nil, "my flash", true)
#sld1.Shapes(3).Movie = "D:/Workplace/TestOLE/linechart.swf"
sld.Shapes.AddOLEObject(0, 0, pres.PageSetup.SlideWidth/2, pres.PageSetup.SlideHeight/2, "ShockwaveFlash.ShockwaveFlash.8")
sld.Shapes("ShockwaveFlash1").OLEFormat.Object.Movie = "D:\\Workplace\\TestOLE\\files\\linechart.swf"
sld.Shapes(3).OLEFormat.Object.SetVariable("Data", "<data><row><column>Greater Vancouver</column><column>1986965</column></row><row><column>Anmore</column><column>1344</column></row><row><column>Belcarra</column><column>682</column></row><row><column>Bowen Island</column><column>2957</column></row><row><column>Burnaby</column><column>193954</column></row><row><column>Coquitlam</column><column>112890</column></row><row><column>Delta</column><column>96950</column></row><row><column>Langley District</column><column>86896</column></row><row><column>Langley City</column><column>23643</column></row><row><column>Lions Bay</column><column>1379</column></row><row><column>Maple Ridge</column><column>63169</column></row><row><column>New Westminster</column><column>54656</column></row><row><column>North Vancouver District</column><column>82310</column></row><row><column>North Vancouver City</column><column>44092</column></row><row><column>Pitt Meadows</column><column>14670</column></row><row><column>Port Coquitlam</column><column>51257</column></row><row><column>Port Moody</column><column>23816</column></row><row><column>Richmond</column><column>164345</column></row><row><column>Surrey</column><column>347820</column></row><row><column>Vancouver</column><column>545671</column></row><row><column>West Vancouver</column><column>41421</column></row><row><column>White Rock</column><column>18250</column></row><row><column>Indian Reserves</column><column>6759</column></row><row><column>Greater Vancouver A</column><column>8034</column></row></data>")
puts sld.Shapes(3).OLEFormat.Object.GetVariable("Data")
puts sld.Shapes(3).OLEFormat.Object.FlashVersion


#sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.Movie = "http://localhost:3000/green-columnchart.swf"
#sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.FlashVars = "D:\\Workplace\\TestOLE\\files\\linechart.xml"
#sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.FlashVars = "Data=<data><row><column></column><column>Population</column></row><row><column>Greater Vancouver</column><column>2116581</column></row><row><column>Anmore</column><column>1785</column></row><row><column>Belcarra</column><column>676</column></row><row><column>Bowen Island</column><column>3362</column></row><row><column>Burnaby</column><column>202799</column></row><row><column>Coquitlam</column><column>114565</column></row><row><column>Delta</column><column>96723</column></row><row><column>Langley District</column><column>93726</column></row><row><column>Langley City</column><column>23606</column></row><row><column>Lions Bay</column><column>1328</column></row><row><column>Maple Ridge</column><column>68949</column></row><row><column>New Westminster</column><column>58549</column></row><row><column>North Vancouver District</column><column>82562</column></row><row><column>North Vancouver City</column><column>45165</column></row><row><column>Pitt Meadows</column><column>15623</column></row><row><column>Port Coquitlam</column><column>52687</column></row><row><column>Port Moody</column><column>27512</column></row><row><column>Richmond</column><column>174461</column></row><row><column>Surrey</column><column>394976</column></row><row><column>Vancouver</column><column>578041</column></row><row><column>West Vancouver</column><column>42131</column></row><row><column>White Rock</column><column>18755</column></row><row><column>Indian Reserves</column><column>7550</column></row><row><column>Greater Vancouver A</column><column>11050</column></row></data>"
#puts sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.Movie
#puts sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.GetVariable("FlashVars")
#puts sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.SetVariable("Data","<data><row><column>Greater Vancouver</column><column>1986965</column></row><row><column>Anmore</column><column>1344</column></row><row><column>Belcarra</column><column>682</column></row><row><column>Bowen Island</column><column>2957</column></row><row><column>Burnaby</column><column>193954</column></row><row><column>Coquitlam</column><column>112890</column></row><row><column>Delta</column><column>96950</column></row><row><column>Langley District</column><column>86896</column></row><row><column>Langley City</column><column>23643</column></row><row><column>Lions Bay</column><column>1379</column></row><row><column>Maple Ridge</column><column>63169</column></row><row><column>New Westminster</column><column>54656</column></row><row><column>North Vancouver District</column><column>82310</column></row><row><column>North Vancouver City</column><column>44092</column></row><row><column>Pitt Meadows</column><column>14670</column></row><row><column>Port Coquitlam</column><column>51257</column></row><row><column>Port Moody</column><column>23816</column></row><row><column>Richmond</column><column>164345</column></row><row><column>Surrey</column><column>347820</column></row><row><column>Vancouver</column><column>545671</column></row><row><column>West Vancouver</column><column>41421</column></row><row><column>White Rock</column><column>18250</column></row><row><column>Indian Reserves</column><column>6759</column></row><row><column>Greater Vancouver A</column><column>8034</column></row></data>")
#puts sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.GetVariable("Data")
#puts sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.SetVariable("src","/green-columnchart.swf")
#puts sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.GetVariable("src")
#puts sld1.Shapes("ShockwaveFlash1").OLEFormat.Object.MovieData = "<data><row><column>Greater Vancouver</column><column>1986965</column></row><row><column>Anmore</column><column>1344</column></row><row><column>Belcarra</column><column>682</column></row><row><column>Bowen Island</column><column>2957</column></row><row><column>Burnaby</column><column>193954</column></row><row><column>Coquitlam</column><column>112890</column></row><row><column>Delta</column><column>96950</column></row><row><column>Langley District</column><column>86896</column></row><row><column>Langley City</column><column>23643</column></row><row><column>Lions Bay</column><column>1379</column></row><row><column>Maple Ridge</column><column>63169</column></row><row><column>New Westminster</column><column>54656</column></row><row><column>North Vancouver District</column><column>82310</column></row><row><column>North Vancouver City</column><column>44092</column></row><row><column>Pitt Meadows</column><column>14670</column></row><row><column>Port Coquitlam</column><column>51257</column></row><row><column>Port Moody</column><column>23816</column></row><row><column>Richmond</column><column>164345</column></row><row><column>Surrey</column><column>347820</column></row><row><column>Vancouver</column><column>545671</column></row><row><column>West Vancouver</column><column>41421</column></row><row><column>White Rock</column><column>18250</column></row><row><column>Indian Reserves</column><column>6759</column></row><row><column>Greater Vancouver A</column><column>8034</column></row></data>"
#puts sld1.Shapes("ShockwaveFlash1").Movie = "D:\\Workplace\\TestOLE\\1.swf"

 
pres.SaveAs("#{Dir.pwd}/test.ppt")
puts  "after active it..."
ppt.Quit()






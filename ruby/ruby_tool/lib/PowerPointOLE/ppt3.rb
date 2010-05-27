#insert_file_to_word.rb
require "win32ole" 


begin 
      ppt = WIN32OLE.connect("PowerPoint.Application") 
rescue 
      #如果没有打开的Word程序，创建一个 
      ppt = WIN32OLE.new("PowerPoint.Application") 
#      ppt.Presentations.add    
end 

#ppt.visible = true
#
pres = ppt.ActivePresentation
#pres = ppt.Presentations.add

slds = pres.slides
puts "**************************"
puts slds.Item(1).Shapes(3).OLEFormat.ProgID
puts "**************************"

slds.Add(slds.Count+1, 2)
slds.Add(slds.Count+1, 2)
#slds.Add(slds.Count+1, 3)
#slds.Add(slds.Count+1, 4)
#slds.Add(slds.Count+1, 5)
#slds.Add(slds.Count+1, 6)
#slds.Add(slds.Count+1, 7)
#slds.Add(slds.Count+1, 8)
#slds.Add(slds.Count+1, 9)
#slds.Add(slds.Count+1, 10)


sld1 = slds.Item(1)
sld2 = slds.Item(2)
sld1.NotesPage.Shapes.Placeholders(2).TextFrame.TextRange.InsertAfter("Added Text")
puts "++++++++++++++++++++"
sld1.Comments.Add(0,0,"shirui","bobj","test comment")
puts sld1.NotesPage.ole_func_methods
puts "++++++++++++++++++++"
#sld1.AddRef("shirui")
#puts sld1.Scripts       #.ole_put_methods
#sld1.Comments(1).Text = "shirui"


shp1 = sld1.Shapes
shp2 = sld2.Shapes


#puts  "-----------------------------------50"
#puts sld1.ole_get_methods
puts  "-----------------------------------51"
puts sld1.ole_func_methods
#puts  "-----------------------------------52"
#puts sld1.ole_func_methods
#puts  "-----------------------------------60"
#puts shp1.ole_get_methods
#puts  "-----------------------------------61"
#puts shp1.ole_put_methods
#puts  "-----------------------------------62"
#puts shp1.ole_func_methods
#sld1.AddRef

###shp1.AddPicture("#{Dir.pwd}/files/all.jpg", true, true, 0, 0)
#sld1.AddComment("test comment") #Comments = "test comment"  # AddComment("test comment")
# shp2.Item(1).Delete
# shp2.Item(1).Delete
shp2.AddPicture("#{Dir.pwd}/files/sgl.jpg", true, true, 0, 0)
# http://farm2.static.flickr.com/1246/1228749068_9240b984ee.jpg
 
sld2.Shapes(3).Left = (pres.PageSetup.SlideWidth - sld2.Shapes(3).Width)/2
sld2.Shapes(3).Top = (pres.PageSetup.SlideHeight - sld2.Shapes(3).Height)/2
#puts sld2.Shapes(3).AnimationSettings
 
#shp2.AddComment
puts "-------------------"
#puts shp2.Title.ole_put_methods
#shp2.Title.Name = "shirui"
shp1.Item(1).TextEffect.Text = "shirui"
shp2.AddTextEffect(16,"Familiar Quotations", "Tahoma", 42, false, false, 100, 100)
puts shp2.Count


#puts slds.Count
#puts shp1.Count 
#puts shp2.Count
#sld1.Shapes(1).TextEffect.Text = "Hello"
## puts sld1.Height, sld1.Width
#puts sld1.Shapes(1).Height, sld2.Shapes(1).Width
#puts sld1.Shapes(2).Height, sld2.Shapes(2).Width
#puts sld1.Shapes(3).Height, sld2.Shapes(3).Width
#puts sld2.Shapes(3).Height, sld2.Shapes(3).Width
## puts shp1.ole_func_methods
 
pres.SaveAs("#{Dir.pwd}/files/slide1.ppt")
puts  "after active it..."

#ppt.PPFileDialog
ppt.Quit()
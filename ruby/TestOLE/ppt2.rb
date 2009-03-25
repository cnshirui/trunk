#insert_file_to_word.rb
require "win32ole" 


begin 
      ppt = WIN32OLE.connect("PowerPoint.Application") 
rescue 
      #如果没有打开的Word程序，创建一个 
      ppt = WIN32OLE.new("PowerPoint.Application") 
      ppt.Presentations.add    
end 

puts ppt.ole_func_methods
puts "-----------------------------------10"

ppt.visible = true

pres = ppt.ActivePresentation
puts pres.ole_get_methods
puts "-----------------------------------20"
puts pres.ole_put_methods
puts "-----------------------------------21"

slds = pres.slides
puts slds.ole_get_methods
puts "-----------------------------------30"
puts slds.ole_put_methods
puts "-----------------------------------40"
puts slds.Count


slds.Add(slds.Count+1, 2)
slds.InsertFromFile("d:\\intro.txt", 1)
#slds.InsertFromFile("d:\\1.doc", 1)
sld1 = slds.Item(1)

puts  "-----------------------------------50"
puts sld1.ole_get_methods
puts  "-----------------------------------51"
puts sld1.ole_put_methods
puts  "-----------------------------------52"
puts sld1.ole_func_methods

shp = sld1.Shapes
puts  "-----------------------------------60"
puts shp.ole_get_methods
puts shp.Count
puts  "-----------------------------------61"
puts shp.ole_put_methods
shp.AddPicture("d:\\all.jpg", true, true, 0, 0)
shp.AddPicture("d:\\sgl.jpg", true, true, 0, 0)
puts  "-----------------------------------62"
puts shp.ole_func_methods

#slds.add(3)



 
pres.SaveAs("d:\\test.ppt")
puts  "after active it..."

#ppt.PPFileDialog
ppt.Quit()
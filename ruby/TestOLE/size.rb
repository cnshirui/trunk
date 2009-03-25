#insert_file_to_word.rb
require "win32ole" 


begin 
      ppt = WIN32OLE.connect("PowerPoint.Application") 
rescue 
      ppt = WIN32OLE.new("PowerPoint.Application") 
end 

pres = ppt.Presentations.add
slds = pres.slides

slds.Add(slds.Count+1, 2)
slds.Add(slds.Count+1, 2)

sld1 = slds.Item(1)
sld2 = slds.Item(2)

shp1 = sld1.Shapes
shp2 = sld2.Shapes

sld1.NotesPage.Shapes.Placeholders(2).TextFrame.TextRange.InsertAfter("photo description")
puts Dir.pwd
shp1.AddPicture("#{Dir.pwd}/files/1.jpg", true, true, 0, 0, pres.PageSetup.SlideWidth, pres.PageSetup.SlideHeight)
shp2.AddTextEffect(16,"Familiar Quotations", "Tahoma", 42, false, false, 100, 100)


#sld1.Shapes(3).Left = (pres.PageSetup.SlideWidth - sld2.Shapes(3).Width)/2
#sld1.Shapes(3).Top = (pres.PageSetup.SlideHeight - sld2.Shapes(3).Height)/2
#sld1.Shapes(3).Left = sld2.Shapes(3).Width
#sld1.Shapes(3).Top = sld2.Shapes(3).Height

puts sld2.Shapes(3).Width
puts sld2.Shapes(3).Height

pres.SaveAs("#{Dir.pwd}/test2.ppt")
puts  "after active it..."
ppt.Quit()
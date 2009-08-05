require "win32ole" 

begin 
      excel = WIN32OLE.connect("Excel.Application") 
rescue 
      excel = WIN32OLE.new("Excel.Application") 
end 

begin
  wb = excel.workbooks.open("#{Dir.pwd}/shirui.xls")
rescue Exception => e
  puts e.to_s
  wb = excel.workbooks.add
  wb.SaveAs("#{Dir.pwd}/shirui.xls")
end

sht = wb.worksheets.item(1)
sht['Cells', 1, "A"] = "shirui"
sht['Cells', 4, 7] = "=B1"
#sht.cells[2][1].value = "=A1"

wb.Save #As("#{Dir.pwd}/shirui.xls")
puts  "after active it..."
excel.Quit()
require "win32ole" 

begin 
      excel = WIN32OLE.connect("Excel.Application") 
rescue 
      excel = WIN32OLE.new("Excel.Application") 
end 

filename = "whohar-red-linechart.xls"
begin
  wb = excel.workbooks.open("#{Dir.pwd}/#{filename}")
rescue
  wb = excel.workbooks.add
  wb.SaveAs("#{Dir.pwd}/#{filename}")
end

sht = wb.worksheets.item(1)

500.times { |i|
  sht["Cells", i+2, "A"] = ""
}

#122.times { |i|
 # sht["Cells", i+2, "A"] = i+2
#}

500.times { |i|
  sht["Cells", i+2, "M"] = i+2
}

499.times { |i|
  sht["Cells", i+3, "L"] = "=IF(ROW()=(COUNTA($A$2:$A$501)+1),A#{i+3},IF(COUNTA($A$2:$A$501)>10,IF(MOD(M#{i+3},FLOOR(COUNTA($A$2:$A$501)/10,1))=0,A#{i+3}, \"\"), A#{i+3}))"
}

wb.Save
puts  "after finish it..."
excel.Quit()
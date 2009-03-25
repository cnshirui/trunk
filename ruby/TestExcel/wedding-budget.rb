require "win32ole" 

begin 
      excel = WIN32OLE.connect("Excel.Application") 
rescue 
      excel = WIN32OLE.new("Excel.Application") 
end 

filename = "whohar-wedding-budget v9.xls"
begin
  wb = excel.workbooks.open("#{Dir.pwd}/#{filename}")
rescue
  wb = excel.workbooks.add
  wb.SaveAs("#{Dir.pwd}/#{filename}")
end

sht = wb.worksheets.item(1)

# 2-51, 53-102

50.times { |i|
  #sht["Cells", i+53, "A"] = "=IF(A#{i+2}=\"\",\"\",A#{i+2})"
}

50.times { |i|
  #sht["Cells", i+53, "B"] = "=IF(B#{i+2}=\"\",\"\",IF(E#{i+53}=K53, H53, B#{i+2}))"
}

50.times { |i|
  #sht["Cells", i+53, "C"] = "=IF(B#{i+2}=\"\",\"\",IF(E#{i+53}=K53, I53, C#{i+2}))"
}

50.times { |i|
  sht["Cells", i+2, "D"] = "=IF(A#{i+2}=\"\",\"\",B#{i+2}*C#{i+2})"
  #sht["Cells", i+53, "D"] = "=IF(A#{i+2}=\"\",\"\",B#{i+2})"
  #{i+53}*C#{i+53}
}

50.times { |i|
  sht["Cells", i+2, "E"] = "=#{i+1}"
  sht["Cells", i+53, "E"] = "=#{i+1}"
}

wb.Save
puts  "after finish it..."
excel.Quit()
require "win32ole" 

begin 
      excel = WIN32OLE.connect("Excel.Application") 
rescue 
      excel = WIN32OLE.new("Excel.Application") 
end 

filename = 'D:\careers\wujuan\wujuan_job_091005.xls'

wb = excel.workbooks.open("#{Dir.pwd}/#{filename}")

sht = wb.worksheets.item(1)
sht.Name = "Data"

100.times { |i|
#  sht["Cells", i+2, "M"] = "=IF(B#{i+2}=AA2,#{i+1},0)"
  sht["Cells", i+2, "M"] = "=IF(A#{i+2}=\"\",0,IF(B#{i+2}=\"\", #{i+1}, IF(B#{i+2}=AA2,#{i+1},0)))"  
#  =IF(B#{i+2}=\"\", 1, IF(B2=AA2,1,0))
#AND(A#{i+2}<>\"\",B#{i+2}=\"\")
#B#{i+2}=\"\"
}

100.times { |i|
  sht["Cells", i+2, "N"] = "=LARGE(M2:M101,#{i+1})"
}

sht["Cells", 1, "O"] = "=A1"
10.times {  |i|
  sht["Cells", 1, (i+80).chr] = "=IF(#{(i+67).chr}2=\"\",\"\",#{(i+67).chr}1)"
}

100.times { |i|
  sht["Cells", i+2, "O"] = "=IF(N#{i+2}=0,\"\",INDEX(A2:A101,N#{i+2}))"
#=IF(N#{i+2}=0,\"\",INDEX(A2:A101,N#{i+2}))
}

sht["Cells", 1, "Z"] = "=A2"
100.times { |i|
  sht["Cells", i+2, "Z"] = "=IF(A#{i+2}=Z1,B#{i+2},\"\")"
}
sht["Cells", 2, "Z"] = "=IF(B2=\"\", \"---\", IF(A2=Z1,B2,\"\"))"

10.times {  |i|
  100.times { |j|
    sht["Cells", j+2, (i+80).chr] = "=IF(OR(N#{j+2}=0,INDEX(C2:L101,#{j+1},#{i+1})=\"\"),\"\",INDEX(C2:L101,N#{j+2},#{i+1}))"
  }
}

sht["Cells", 1, "AA"] = "Dim2"
sht["Cells", 2, "AA"] = "NoChoice"

wb.Save
puts  "after finish it..."
excel.Quit()
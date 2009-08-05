require "win32ole" 

begin 
      excel = WIN32OLE.connect("Excel.Application") 
rescue 
      excel = WIN32OLE.new("Excel.Application") 
end 

wb = excel.workbooks.open('D:/xcelsius/bugfix/import_excel_lost_binding/Base.xls')

sheet = wb.worksheets.item(1)
#puts sheet.range("$B$2:$B$5").value

# puts wb's range
puts excel.evaluate("Sheet1!$B$2:$B$5").value
puts "..............."
e = excel.evaluate("'Sheet1'!$B$2:$B$5")
r = sheet.range("$B$2:$B$5")

excel.Quit()

puts 'end...'
require "jxl0.jar"
#require "poi-3.1-FINAL-20080629"

include Java
#include java.util.Date

#D:\workplace\TestExcel
include_class 'jxl.Workbook'
#import 'jxl.Workbook'
#include_class Java::org.apache.poi.poifs.filesystem.POIFSFileSystem
JFile = java.io.File

puts "Hello World"

workbook = Workbook.getWorkbook(JFile.new("excels/sports_attendanceXlf.xls"))
puts workbook.getSheet(0).getCell(0,0).getContents()

#puts 
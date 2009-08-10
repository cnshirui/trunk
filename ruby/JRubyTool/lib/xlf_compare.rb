require 'java'
import 'java.lang.System'
import 'java.io.FileWriter'
JFile = java.io.File

require 'fileutils.rb'
require 'libs/jzip'
require 'libs/jxml'
require 'libs/dom4j-1.6.1'
require 'libs/jaxen-1.1-beta-6'

import 'org.dom4j.Document'
import 'org.dom4j.DocumentHelper'
import 'org.dom4j.Element'
import 'org.dom4j.io.XMLWriter'
import 'org.dom4j.io.OutputFormat'
import 'org.dom4j.io.SAXReader'

compare_tool = "D:\\dev\\WinMerge\\WinMergeU.exe"
#"D:\\dev\\BC2\\BC2.exe"

argc = $*.length
if(argc == 2)
  puts $*.inspect
else
  dir = 'D:\xcelsius\bugfix\migrate_radio_background'
  files = ['radio_45_53_3.xlf', 'radio_45_53_3_t.xlf']
  dir = dir.gsub("\\", "/") if(dir.index("\\")!=-1)
  
  files.map! { |file| "#{dir}/#{file}" }
  puts files
  puts "please choose the original xlf file in command line!"
end

files = $* unless files

FileUtils.mkdir(dir + '/xml_compare') unless File.exist?(dir + '/xml_compare')
time = Time.now.to_i.to_s
xmls = files.map { |file| "#{dir}/xml_compare/#{file.sub(dir, "").sub(".xlf", ".xml")}" }
xmls.each { |xml| File.delete(xml) if File.exist?(xml)}
2.times do |i|
  # unzip xlf
  JZip::ZipFile.open(files[i]) do |zipfile|
    zipfile.extract('document.xml', xmls[i])
  end
  sleep(1)

  temp = "temp/#{time}.tmp"
  # format xml with jxml format
  doc = JXML::Document.new(File.new(xmls[i]).read)
  File.open(temp, "w+") do |file|
    file.write(doc.to_xml)
  end
  FileUtils.copy(temp, xmls[i])

  # format xml with compact format
  reader = SAXReader.new
  doc = reader.read(JFile.new(xmls[i]))
  format = OutputFormat.createCompactFormat()
  writer = XMLWriter.new(FileWriter.new(temp), format)
  writer.write(doc)
  writer.flush

  # format xml with pretty print
  reader = SAXReader.new
  doc = reader.read(JFile.new(temp))
  format = OutputFormat.createPrettyPrint()
  writer = XMLWriter.new(FileWriter.new(xmls[i]), format)
  writer.write(doc)
  writer.flush
end

# remove temp files
Dir["temp/*"].each do |file|
  FileUtils.rm_f(file) if(file!="readme.txt")
  puts "#{file}...removed!"
end

# execute compare
puts "#{compare_tool} '#{xmls[0]}'  '#{xmls[1]}'"
#exec("#{compare_tool} '#{xmls[0]}'  '#{xmls[1]}'")
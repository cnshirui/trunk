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

compare_tool = "C:/Program Files/Beyond Compare 3/BCompare.exe"

argc = $*.length
if(argc == 2)
  puts $*.inspect
else
  dir = "d:/migrate/migrate_60"
  files = ['gauge_title_value_prop_style_bind_50_60_change.xlf', 'gauge_title_value_prop_style_bind_50_60.xlf']

  files.map! { |file| "#{dir}/#{file}" }
  puts files
  puts "please choose the original xlf file in command line!"
end

files = $* unless files

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

#  FileUtils.rm_f(temp) if File.exist?(temp)
end

# execute compare
exec("#{compare_tool} #{xmls[0]}  #{xmls[1]}")
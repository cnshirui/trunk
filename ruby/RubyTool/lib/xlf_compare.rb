require 'java'
import 'java.lang.System'
import 'java.io.FileWriter'
JFile = java.io.File

require 'jzip'
require 'jxml'
require 'fileutils.rb'

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
  puts "please choose the original xlf file in command line!"
end

time = Time.now.to_i.to_s
xmls = []
2.times do |i|
  # unzip xlf
  xmls[i] = "data/#{time}.#{i}.xml"
  JZip::ZipFile.open($*[i]) do |zipfile|
    zipfile.extract('document.xml', xmls[i])
  end
  sleep(1)

  temp = "data/#{time}.tmp"
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

# execute compare
exec("#{compare_tool} #{xmls[0]}  #{xmls[1]}")
require "rubygems"
require 'zip/zip'
require 'java'
require 'libs/dom4j-1.6.1'
require 'libs/jaxen-1.1-beta-6'

import 'java.lang.System'
import 'java.io.FileWriter'
JFile = java.io.File

import 'org.dom4j.Document'
import 'org.dom4j.DocumentHelper'
import 'org.dom4j.Element'
import 'org.dom4j.io.XMLWriter'
import 'org.dom4j.io.OutputFormat'
import 'org.dom4j.io.SAXReader'

compare_tool = "C:/Program Files/Beyond Compare 3/BCompare.exe"
xml1 = "soap/test/line.soap.my.1.xml"
xml2 = "soap/test/line.soap.my.2.xml"

time = Time.now.to_i
tmp1 = "data/#{time}.1.xml"
tmp2 = "data/#{time}.2.xml"


reader = SAXReader.new
doc = reader.read(JFile.new(xml1))
format = OutputFormat.createCompactFormat()
writer = XMLWriter.new(FileWriter.new(tmp1), format)
writer.write(doc)
writer.flush

reader = SAXReader.new
doc = reader.read(JFile.new(tmp1))
format = OutputFormat.createPrettyPrint()
writer = XMLWriter.new(FileWriter.new(xml1), format)
writer.write(doc)
writer.flush

reader = SAXReader.new
doc = reader.read(JFile.new(xml2))
format = OutputFormat.createCompactFormat()
writer = XMLWriter.new(FileWriter.new(tmp2), format)
writer.write(doc)
writer.flush

reader = SAXReader.new
doc = reader.read(JFile.new(tmp2))
format = OutputFormat.createPrettyPrint()
writer = XMLWriter.new(FileWriter.new(xml2), format)
writer.write(doc)
writer.flush

exec("#{compare_tool} #{xml1}  #{xml2}")
puts "end"
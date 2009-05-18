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

def xml_format(xml_file)
  temp = "#{xml_file}.tmp"
  # format xml with jxml format
  doc = JXML::Document.new(File.new(xml_file).read)
  File.open(temp, "w+") do |file|
    file.write(doc.to_xml)
  end
  FileUtils.copy(temp, xml_file)

  # format xml with compact format
  reader = SAXReader.new
  doc = reader.read(JFile.new(xml_file))
  format = OutputFormat.createCompactFormat()
  writer = XMLWriter.new(FileWriter.new(temp), format)
  writer.write(doc)
  writer.flush

  # format xml with pretty print
  reader = SAXReader.new
  doc = reader.read(JFile.new(temp))
  format = OutputFormat.createPrettyPrint()
  writer = XMLWriter.new(FileWriter.new(xml_file), format)
  writer.write(doc)
  writer.flush
end
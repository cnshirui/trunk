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


file_name = 'soap/persist.xml'
reader = SAXReader.new
xml_doc = reader.read(JFile.new(file_name))
node = xml_doc.select_single_node("Persist/array/property/object[property/string = '_wsdlURLItem']")
puts node.as_xml



# write out to a system console
format = OutputFormat.createPrettyPrint()
writer = XMLWriter.new(System.out, format)
#writer.write(xml_doc)

# write out to a new file
#format = OutputFormat.createPrettyPrint()
#writer = XMLWriter.new(FileWriter.new('data/' + Time.now.sec.to_s + '.xml'), format)
#writer.write(doc)

puts "end..."


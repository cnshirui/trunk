require 'java'
require 'libs/dom4j-1.6.1'
require 'libs/jaxen-1.1-beta-6'

import 'java.lang.System'
import 'java.io.FileWriter'
include_class("java.io.File") { |pkg, name| "J" + name }

import 'org.dom4j.Document'
import 'org.dom4j.DocumentHelper'
import 'org.dom4j.Element'
import 'org.dom4j.io.XMLWriter'
import 'org.dom4j.io.OutputFormat'
import 'org.dom4j.io.SAXReader'


# -- create a new XML document --
#doc = DocumentHelper.create_document
#node = doc.add_element("shirui")

# -- read from an XML document --
file_name = 'data/soap/con-xml.xml'
reader = SAXReader.new
doc = reader.read(JFile.new(file_name))

#puts doc.as_xml
root = doc.get_root_element
puts root
id = root.select_single_node("@id").get_text
puts id 

# write out to a system console
#format = OutputFormat.createPrettyPrint()
#writer = XMLWriter.new(System.out, format)
#writer.write(doc)

# write out to a new file
#format = OutputFormat.createPrettyPrint()
#writer = XMLWriter.new(FileWriter.new('data/' + Time.now.sec.to_s + '.xml'), format)
#writer.write(doc)

puts "end..."


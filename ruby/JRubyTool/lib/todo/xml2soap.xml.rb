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


# -- read from document with XML Connection--
file_name = 'soap/con-xml.xml'
reader = SAXReader.new
xml_doc = reader.read(JFile.new(file_name))
xml_root = xml_doc.get_root_element

# -- create a new document with SOAP Connection --
soap_doc = DocumentHelper.create_document
node = soap_doc.add_element("connection")
uuid = xml_root.select_single_node("@id").get_text
#name = 
node.add_attribute("id", uuid)
node.add_element("children")

# write out to a system console
format = OutputFormat.createPrettyPrint()
writer = XMLWriter.new(System.out, format)
writer.write(soap_doc)

# write out to a new file
#format = OutputFormat.createPrettyPrint()
#writer = XMLWriter.new(FileWriter.new('data/' + Time.now.sec.to_s + '.xml'), format)
#writer.write(doc)

puts "end..."


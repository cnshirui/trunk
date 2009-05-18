require 'net/http'
require 'rexml/document'


puts "start..."

url = "http://localhost:8080/docs/wsdl.xml"
result = Net::HTTP.get(URI(url))
doc = REXML::Document.new result
headers = []
rows = []
doc.get_elements("definitions/types/xsd:schema/xsd:complexType").each do |node|
  name = node.attributes.get_attribute("name").value
  if(name == "Header")
    node.get_elements("xsd:sequence/xsd:element").each do |element|
      headers << element.attributes.get_attribute("name").value
    end
  elsif(name == "Row")
    node.get_elements("xsd:sequence/xsd:element").each do |element|
      rows << element.attributes.get_attribute("name").value
    end
  end
end

puts headers, rows, headers == rows

puts "end..."
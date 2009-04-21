require 'jxml'
require 'soap_connection'

File.open("soap/test/line.xml.xml") do |file|
  @xml_doc = JXML::Document.new(file.read)
end

xml_connection = @xml_doc.find("/CXCanvas/connection[@className = 'xcelsius.rpc.XMLConnection']", :first)
xml_connection.parent.remove_child xml_connection

connection = SoapConnection.new

wsdl = "http://localhost:3000/datasets/wsdl/MIV_TedbgGeKvQdLYeZu3R"
connection.parse_wsdl(wsdl)

url_range = "Sheet1!$E$1"
connection.set_url_binding(url_range)

data_range = "Sheet1!$A$1:$C$12"
connection.set_data_binding(data_range)

connection_node = connection.to_xml_node
#puts "rshi...", connection_node.to_xml
connection_node = @xml_doc.import_node(connection_node)
@xml_doc.get_root_element.append_child(connection_node)

#puts @xml_doc.to_xml

File.open("soap/test/line.soap.my.2.xml", "w+") do |file|
  file.write(@xml_doc.to_xml)
end



#puts connection.to_s
#def func num
#  puts num + 1
#end
#
#
#func 3
#File.open("D:/XML2Webservice/add-soap-step-compare/abc.xml", "w+") do |file|
#  file.write connection.to_s
#end

puts "end..."
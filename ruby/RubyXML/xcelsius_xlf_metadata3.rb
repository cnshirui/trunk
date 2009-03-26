require "rubygems"
require "zip/zip"
require "rexml/document"
require "jxml"

if !File.exist?("document.xml")
  start_time = Time.now
  Zip::ZipFile.open("whohar-budget-planner v2.6.3.xlf", Zip::ZipFile::CREATE) do |zf|
    File.open("document.xml", File::CREAT|File::RDWR) { |xf| xf.write(zf.read("document.xml")) }
  end
  puts "unzip xml file: #{Time.now-start_time}s"
end

start_time = Time.now #------------------------------------------

data_doc = JXML::Document.new(File.new("document.xml").read)
data_xml = REXML::Document.new "<connections/>"
root_data_xml = data_xml.root
node_component = root_data_xml.add_element "component", {"type" => "FlashVars", "name" => "FlashVars"}
node_property = node_component.add_element "property", {"name" => "variableFormat"}
node_string = node_property.add_element "string"
node_string.add_text "XML"

xpath = "//connection[@className='xcelsius.rpc.FlashVars']/metaData/data"
index_range = 0
data_doc.find(xpath).first.value.split("&").each do |line|
  words = line.split(/={rangeValuesXML:|}/)
  name_component = words[0]
  node = data_doc.find("CXCanvas/connection/bindings/property/value/binding/endpoint[@id='#{words[1]}']").first
  nodes_row = node.find("cells/array/property/array/property")
  count_row = nodes_row.last.attributes("id").to_i - nodes_row.first.attributes("id").to_i + 1
  nodes_col = node.find("cells/array/property/array/property/array/property")
  count_col = nodes_col.last.attributes("id").to_i - nodes_col.first.attributes("id").to_i + 1    
  node_range = node_component.add_element "range", {"num" => index_range += 1, "name" => name_component,"rows" => count_row.to_s, "cols" => count_col}

  node.find("cells/array/property/array/property/array/property/string").each do |cell|
    node_cell = node_range.add_element "cell"
    node_cell.add_text cell.value
  end    
end
    
puts "parse xml file: #{Time.now-start_time}s" #-------------------------------------

puts data_xml.to_s
data_xml.add(REXML::XMLDecl.default)
puts data_xml.to_s
#  <?xml version="1.0" encoding="utf-8"?>
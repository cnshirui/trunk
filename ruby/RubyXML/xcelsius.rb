require "rubygems"
require "zip/zip"
require "rexml/document"
require "jxml"
include REXML


if !File.exist?("document.xml")
  start_time = Time.now
  Zip::ZipFile.open("whohar-budget-planner v2.6.3.xlf", Zip::ZipFile::CREATE) do |zf|
    File.open("document.xml", File::CREAT|File::RDWR) { |xf| xf.write(zf.read("document.xml")) }
  end
  puts "unzip xml file: #{Time.now-start_time}s"
end

start_time = Time.now #------------------------------------------

#doc = Document.new(File.new("document.xml"))
#XPath.each(doc, "//component") do  |element| 
#  if "xcelsius.controls.sliderClasses.HSlider"==element.attributes["className"]
#    puts element.attributes["displayName"]
#  end
#end
#parse xml file: 8.307s

#doc = REXML::Document.new "<root/>"
#root_node = doc.root
#el = root_node.add_element "myel"
#el2 = el.add_element "another", {"id"=>"10"}
## does the same, but also sets attribute "id" of el2 to "10"
#el3 = REXML::Element.new "blah"
#el.elements << el3
#el3.attributes["myid"] = "sean"
#puts doc.to_s

data_doc = JXML::Document.new(File.new("document.xml").read)
#data_doc.find("//connection").each do |range|
#  puts range.attributes("displayName")
#end

#data_doc.find_by_tag_name("cells").each do |node|
##  puts node.parent.attributes("displayName")
#  if node.parent.parent.parent.parent.parent.parent.attributes("className") == "xcelsius.rpc.XMLConnection"
#    puts node.parent.parent.parent.parent.parent.parent.attributes("id")
#    puts node.parent.attributes("id")
#    puts node.parent.name, node.parent.parent.name, node.parent.parent.parent.name, node.parent.parent.parent.parent.name, node.parent.parent.parent.parent.parent.name, node.parent.parent.parent.parent.parent.parent.name, "--------------------"
#  end
#end

#data_doc.find("cells").each do |node|
##  puts node.parent.attributes("displayName")
#  if node.parent.parent.parent.parent.parent.parent.attributes("className") == "xcelsius.rpc.XMLConnection"
#    puts node.parent.parent.parent.parent.parent.parent.attributes("id")
#    puts node.parent.attributes("id")
#    puts node.parent.name, node.parent.parent.name, node.parent.parent.parent.name, node.parent.parent.parent.parent.name, node.parent.parent.parent.parent.parent.name, node.parent.parent.parent.parent.parent.parent.name, "--------------------"
#  end
#end

#[@className='xcelsius.rpc.XMLConnection']
#endpoint
#binding
#value
#property
#bindings
#connection
#//property[name/string='loadRanges_ary']//cells
#property[contains(name/string, 'Ranges_ary')]
#connection/properties/property/value/array/property/string
#data_doc.find("//connection[@className='xcelsius.rpc.XMLConnection']//property[name/string='loadRanges_ary']//cells").each do |node|
#[properties[property/name/string='transferType'][property/name/string='load']]
#[properties/property/name/string='load']
#xpath = "//connection[@className='xcelsius.rpc.XMLConnection'][properties/property[3][name/string='transferType'][value/string='load']]//cells"
#xpath = "//connection[@className='xcelsius.rpc.XMLConnection']//property[contains(name/string, 'Ranges_ary')]//cells"

data_xml = REXML::Document.new "<connections/>"
root_data_xml = data_xml.root
#el = root_node.add_element "myel"
#el2 = el.add_element "another", {"id"=>"10"}
#el3 = REXML::Element.new "blah"
#el.elements << el3
#el3.attributes["myid"] = "sean"
#puts doc.to_s

index_range = 0

xpath = "//connection[@className='xcelsius.rpc.XMLConnection']//property[name/string='loadRanges_ary']//cells"
data_doc.find(xpath).each do |node|
#  puts node.parent.attributes("displayName")
#    puts node.parent.parent.parent.parent.parent.parent.attributes("id")
#<component type="FlashVars" name="FlashVars">
    node_component = root_data_xml.add_element "component", {"type" => "XMLConnection", "name" => "XMLConnection"}
    node_property = node_component.add_element "property", {"name" => "variableFormat"}
    node_string = node_property.add_element "string"
    node_string.add_text "XML"
    
    name_component = node.find("../../../../../..").first.find("properties/property[name/string='loadNames_ary']/value/array/property/string").first.value
    nodes_row = node.find("array/property/array/property")
    count_row = nodes_row.last.attributes("id").to_i - nodes_row.first.attributes("id").to_i + 1
    nodes_col = node.find("array/property/array/property/array/property")
    count_col = nodes_col.last.attributes("id").to_i - nodes_col.first.attributes("id").to_i + 1    
    node_range = node_component.add_element "range", {"num" => index_range += 1, "name" => name_component,"rows" => count_row.to_s, "cols" => count_col}
    
    puts node.find("../../../../../..").first.find("properties/property[name/string='loadNames_ary']/value/array/property/string").first.value
    
#    puts node.parent.attributes("id")
    row_nodes = node.find("array/property/array/property")
    puts row_nodes.last.attributes("id").to_i - row_nodes.first.attributes("id").to_i + 1
    
    column_nodes = node.find("array/property/array/property/array/property")
    puts column_nodes.last.attributes("id").to_i - column_nodes.first.attributes("id").to_i + 1
    
#    puts node.parent.name, node.parent.parent.name, node.parent.parent.parent.name, node.parent.parent.parent.parent.name, node.parent.parent.parent.parent.parent.name, node.parent.parent.parent.parent.parent.parent.name, "--------------------"
    node.find("array/property/array/property/array/property/string").each do |cell|
      puts cell.value
      node_cell = node_range.add_element "cell"
      node_cell.add_text cell.value
    end
end
    
puts "parse xml file: #{Time.now-start_time}s" #-------------------------------------

puts data_xml.to_s




#-------------------------------------------------------------------
#root = doc.root
#puts root.elements["component/component[@className='xcelsius.controls.sliderClasses.HSlider']"].each { |slider|
#  puts slider.class
#}

#xcelsius.controls.sliderClasses.HSlider ["displayName"]
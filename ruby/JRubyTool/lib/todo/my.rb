# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

require 'rexml/document'

# read xml
file = File.open("charts_en.xml")
doc = REXML::Document.new(file.read)

# parse local info
doc.elements.each("xcelsius/component/properties/property/value/string") do |node|
  value = node.get_elements("locElement/locLabel")[0].get_text
  node.delete_element("*") 
  node.text = value
end

# access property name and value
doc.elements.each("//component") do |node|
  puts ""
  print node.attribute("className")
  node.elements.each("properties/property") do |prop|
    name = prop.get_elements("name/string")[0].get_text
    value = prop.get_elements("value/*")[0].get_text
#    print "\t#{value}"
    print "\t#{name}"
  end
end

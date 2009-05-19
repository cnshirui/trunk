require 'rexml/document'

# read xml
file = File.open("component_en.xml")
doc = REXML::Document.new(file.read)

component_count = 0
components = []

doc.elements.each("xcelsius/component") do |node|
#  puts node.attribute("className")
components << node.attribute("className").to_s
  component_count += 1
end

components.sort!
puts component_count
puts components, components.length
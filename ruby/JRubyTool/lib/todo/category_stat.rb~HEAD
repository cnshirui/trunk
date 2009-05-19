require 'rexml/document'

# read xml
file = File.open("category_en.xml")
doc = REXML::Document.new(file.read)

component_count = 0
components = []

doc.elements.each("xcelsius/category") do |category|
  name = category.get_elements("name/locElement/locLabel")[0].get_text
  if(name != "Maps")
    puts name
    category.elements.each("components/symbol") do |component|
      component_count += 1
      puts "\t #{component.get_text}"
    end      
  end
end

#components.sort!
puts component_count
#puts components, components.length
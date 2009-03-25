require 'rexml/document'

#list_doc = REXML::Document.new("<files/>")
#doc_root = list_doc.root

charts = "<xcelsius>"
maps = "<xcelsius>"
others = "<xcelsius>"

Dir.chdir("files/en")
Dir["*"].each do |file|
  xml = REXML::Document.new(File.new(file).read)
  puts "#{file} -- #{xml.get_elements("xcelsius/component").length}"
  xml.get_elements("xcelsius/component").each do |node|
    type = node.attribute("className").value
    if(type.index("xcelsius.charts"))
      charts += node.to_s
    elsif(type.index("xcelsius.maps"))
      maps += node.to_s
    else
      others += node.to_s
    end
  end

#  node = doc_root.add_element("file")
#  node.add_text(file.to_s)
end

charts += "</xcelsius>" # xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='component.xsd'>"
maps += "</xcelsius>"
others += "</xcelsius>"

Dir.chdir("../..")
xml = REXML::Document.new(charts)
puts xml.get_elements("xcelsius/component").length
File.open("files/new/charts_en.xml", "w+") do |file|
  file.write(xml.to_s)
end

xml = REXML::Document.new(maps)
puts xml.get_elements("xcelsius/component").length
File.open("files/new/maps_en.xml", "w+") do |file|
  file.write(xml.to_s)
end

xml = REXML::Document.new(others)
puts xml.get_elements("xcelsius/component").length
File.open("files/new/others_en.xml", "w+") do |file|
  file.write(xml.to_s)
end

#puts list_doc
#File.open("component_file_list.xml", "w+") do |file|
# file.write(list_doc)
#end
#puts list_doc.get_elements("files/file").length
#puts comp_list.sort
puts "end...."
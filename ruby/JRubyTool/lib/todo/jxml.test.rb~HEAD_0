require 'jxml'

#File.open("data/1.xml") do |file|
#  puts file.read
#end


xml = JXML::Document.new("<root><a/><b/></root>")
#puts xml.to_xml

#xpath = "//component[@className='xcelsius.containers.Panel'][@displayName='Panel Container 1']"
#node = xml.find(xpath, :first)
#puts node.to_xml
node = xml.find('/root/b', :first)
puts node.child_index
puts node.parent.to_xml
puts node.to_xml

puts "end..."
# To change this template, choose Tools | Templates
# and open the template in the editor.

puts "Hello World"

require 'java'
require 'jxml'

File.open("data/1.xml") do |file|
  puts file.read
end


xml = JXML::Document.new(File.new("data/web.xml").read)
#puts xml.to_xml

#xpath = "//component[@className='xcelsius.containers.Panel'][@displayName='Panel Container 1']"
#node = xml.find(xpath, :first)
#puts node.to_xml

puts xml.to_xml

puts "end..."
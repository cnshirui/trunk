

require 'xml_connection'

con = XmlConnection.new(File.new("soap/test/xml.xml").read)
puts con.url_range
puts con.data_range

puts "end.........."

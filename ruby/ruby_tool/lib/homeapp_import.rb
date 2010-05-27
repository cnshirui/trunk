require 'rexml/document'

xml = REXML::Document.new(File.new('data/data_import.xml').read)
xml.elements.each('data/*/*') do |e|
  puts e.to_s.match(/<.+>/).to_s[1..-2]
end
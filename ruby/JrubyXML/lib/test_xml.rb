# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

puts "Hello World"
require "rexml/document"
include REXML

doc = Document.new "<excel/>"
root = doc.root
node = root.add_element "first"
#node.add_text Text.new("Central America & Carribean", false, nil, false)
str1 = "\"Good & Bye\""
str = "Good Bye"
str = str1.gsub("&", "&amp;")
text = Text.new(str, false, nil, true)
#text.write_with_substitution(text, str)
#text << '"'
#text = text.write_with_substitution("", str)
node.add_text text
#node.add_text REXML::Text.("", "Central America & Carribean")
puts doc.to_s

#puts Text.new("Good & Bye", false, nil, false ).to_s
#puts Text.new("").write_with_substitution("", "Good & Bye")

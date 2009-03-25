# 
# To change this template, choose Tools | Templates
# and open the template in the editor.

puts File.open("xldoc").read

#require "rubygems"
#require "rexml/document"
#include REXML
#
#doc = Document.new "<root>\"jiangsu\"</root>"
#root_node = doc.root
#puts root_node.get_text
#
#el = root_node.add_element "first"
##el.add_text "#{Text.SLAICEPS}shirui#{Text.SLAICEPS}"
#el.add_text  Text.new( "\"sean russell\"", false, nil, true )
#
#puts doc.to_s

#-------------------------------------------------------------------------------------------
##workbook = ExcelWorkBook.new("here.xls")
#worksheet = workbook.worksheet("Sheet1")
##worksheet = workbook.worksheet_at(0)
#puts worksheet.row_at(0).string_value_at(2)
#
#sheet_name, row, col = "Sheet1!R51C3".split(/!R|C/)
#puts sheet_name, row, col
# ------------------------------------------------------------------------------------------
#class Foo
#  def func_a
#    puts "func_a in Foo"
#  end
#  
#  def func_b
#    func_a
#    puts "func_b"
#  end
#end
#
#class Bar < Foo
#  def func_a
#    puts "func_a in Bar"
#  end
#end
#
#a = Bar.new
#a.func_b
# ------------------------------------------------------------------------------------------
#require "rubygems"
#require "rexml/document"
#
#doc = REXML::Document.new "<root/>"
#root_node = doc.root
#el = root_node.add_element "myel"
#el2 = el.add_element "another", {"id"=>"10"}
## does the same, but also sets attribute "id" of el2 to "10"
#el3 = REXML::Element.new "blah"
#el.elements << el3
#el3.attributes["myid"] = "sean"
#puts doc.to_s








# --------------------------------------------------------------------------------
# require "jxml"
#
#
#data_xml = JXML::Document.new("<connections></connections>")
#root = data_xml.find("/").first
#root.children << "shirui"
#puts root.to_xml


#---------------------------------------------------------------------------------
#begin
#  result = system("test")
#  puts "shirui...", result
#rescue
#  puts "there's no test cmd"
#end
  

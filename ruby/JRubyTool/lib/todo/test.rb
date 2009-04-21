
require 'xcelsius_file'

puts $$
puts $".inspect

#require 'pp'
#
#class PrettyPrint
#  include PP::PPMethods
#end
#
#array = [[1,2,3],[4,5,6]]
#puts array.inspect
#q = PrettyPrint.new(STDOUT)
#array.pretty_print(q)
#q.flush

#require 'app/models/data_table'
#
#table = DataTable.find(:first)
#puts table.java_data_table

#xml_request =<<EOF
#Type:record
#Activity:query-data
#Input:
#<? xml version='1.0' ?>
#<DATASET>
#<SITE_ID>123456789</SITE_ID>
#<MLID>987654321</MLID>
#<DATA type='email'>someuser@somedomain.com</DATA>
#</DATASET>
#EOF
#
#puts xml_request.inspect

#module Mod
# alias_method :orig_exit, :exit
# def exit(code=0)
#   puts "Exiting with code #{code}"
#   orig_exit(code)
# end
#end
#include Mod
#exit(99)



#class A
#  def owner
#    "class owner"
#  end
#
#  def test
#    owner = "shirui"
#    puts owner
#  end
#end
#
#
#a = A.new
#a.test

# To change this template, choose Tools | Templates
# and open the template in the editor.

#puts File.exist?("D:/Perforce/Whohar/v6/whohar/runtime/development/public/xcelsius_template_subclass_extension/xcelsius_file/1/whohar-2-datasets-chart.xlf")

#class MyException < Exception; end
#
#begin
#  puts "begin..."
#  raise MyException, "my message", ["my backtrace"]
#rescue Exception => e
#  puts e.to_s, e.message, e.backtrace
#end
#puts "Hello World"


#require 'rexml/document'

#comp_list = []
#xml = REXML::Document.new(File.new("files/_en.xml").read)
#xml = REXML::Document.new(charts)
#puts xml.get_elements("xcelsius/component").length
#xml.get_elements("xcelsius/category/components/symbol").each do |node|
#  comp_list << node.get_text
#end

#puts comp_list.sort
#xml.get_elements("xcelsius/category").each do |category|
#  puts category.get_elements("name/locElement/locLabel")[0].get_text
#  puts category.get_elements("components/symbol").length
#end
#puts xml
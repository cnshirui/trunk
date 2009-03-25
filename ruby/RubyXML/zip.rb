# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 
require "rubygems"
require "zip/zip"

####### Using ZipInputStream alone: #######
#
#Zip::ZipInputStream.open("whohar-budget-planner v2.6.3.xlf") do |zis|
#  entry = zis.get_next_entry
#  print "First line of '#{entry.name} (#{entry.size} bytes):  "
#  puts "'#{zis.gets.chomp}'"
#  entry = zis.get_next_entry
#  print "First line of '#{entry.name} (#{entry.size} bytes):  "
#  puts "'#{zis.gets.chomp}'"
#end
#
#  Zip::ZipFile.open("my.zip", Zip::ZipFile::CREATE) {
#   |zipfile|
#    zipfile.get_output_stream("first.txt") { |f| f.puts "Hello from ZipFile" }
#    zipfile.mkdir("a_dir")
#  }
  
#  require 'zip/zip'
#
  Zip::ZipFile.open("my.zip", Zip::ZipFile::CREATE) {
    |zipfile|
    puts zipfile.read("first.txt")
    zipfile.remove("first.txt")
  }
#  
#  Zip::ZipFile.open("whohar-budget-planner v2.6.3.xlf", Zip::ZipFile::CREATE) do |zf|
#    File.open("1.xml", File::CREAT|File::RDWR) do |xf|
#      xf.write(zf.read("document.xml"))
#    end
##    puts zf.read("document.xml")
##    zipfile.remove("first.txt")
#  end
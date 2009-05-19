require "rubygems"
gem 'rubyzip'
require 'zip/zip'


puts "start..."

time = Time.now.to_i
puts time

Zip::ZipFile.open('data/chart-xml.xlf') do |zipfile|
  zipfile.extract('document.xml', "data/document.#{time}.xml") {true}
  zipfile.extract('xldoc', "data/xldoc.#{time}.xls") {true}
end

Zip::ZipFile.open("data/chart-soap.#{time}.xlf", Zip::ZipFile::CREATE) do |zipfile|
  zipfile.add('document.xml', "data/web.xml")
  zipfile.add('xldoc', "data/xldoc.#{time}.xls")
  zipfile.commit
end

puts "end..."

require "rubygems"
gem 'rubyzip'
require 'zip/zip'


puts "start..."

time = Time.now.to_i
puts time

Zip::ZipFile.open('data/1.xlf') do |zipfile|
  zipfile.extract('document.xml', "data/document.xml") {true}
end

File.open("data/document.xml", "w+") do |file|
  file.write("shirui")
end

Zip::ZipFile.open("data/1.xlf", Zip::ZipFile::CREATE) do |zipfile|
  zipfile.remove("document.xml")
  zipfile.add('document.xml', "data/document.xml")
  zipfile.commit
end

puts "end..."

require 'jzip'
require 'fileutils.rb'

file = "soap/" + Time.now.to_i.to_s + ".zip"
FileUtils.copy("soap/1.xlf", file)

JZip::ZipFile.open(file) do |zipfile|
  zipfile.replace('document.xml', "soap/bind-con-xml.xml")
  zipfile.extract('document.xml', "soap/bind-con-xml.1.xml")
end
puts "extract successful..........."
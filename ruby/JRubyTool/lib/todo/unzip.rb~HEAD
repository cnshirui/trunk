require "rubygems"
gem 'rubyzip'
require 'zip/zip'

require 'rexml/document'
include REXML

Zip::ZipFile.open('whohar_budget_projection.xlf') do |zipfile|
  zipfile.extract('document.xml', '1.xml') {true}
end

puts "end"
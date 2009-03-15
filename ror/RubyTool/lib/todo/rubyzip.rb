require "rubygems"
require 'zip/zip'

#puts RUBY_PLATFORM

Zip::ZipFile.open('budget_planner1.xlf') do |zipfile|
  zipfile.extract('document.xml', '1.xml') {true}
end

puts "end..."
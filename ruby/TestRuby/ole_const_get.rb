#!/usr/bin/env ruby
require 'win32ole'

ie = WIN32OLE.new('InternetExplorer.Application')
module IE_CONST
end

WIN32OLE.const_load(ie, IE_CONST)
IE_CONST.constants.sort.each do |c|
  puts "#{c} = #{IE_CONST.const_get(c)}"
end

IE_CONST::CONSTANTS.each do |k, v|
  puts "#{k} = #{v}"
end

puts WIN32OLE::VERSION
ie.quit

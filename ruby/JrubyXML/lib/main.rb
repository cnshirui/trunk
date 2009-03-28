# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 
#require "rubygems"
#require "jrexml"

puts "shirui...."

require 'benchmark'
require 'rexml/document'

xml = File.open(File.dirname(__FILE__) + "/document.xml") {|f| f.read }

Benchmark.bm(7) do |x|
  x.report("REXML") do
    10.times do
      REXML::Document.new xml
    end
  end
  x.report("JREXML") do
    require "rubygems"
#    gem 'jrexml'
    require 'jrexml'
    10.times do
      REXML::Document.new xml
    end
  end
end

#
#require 'rexml/parsers/baseparser'
#require 'jrexml/java_pull_parser'
#
#class REXML::Parsers::BaseParser
#  # Extend every REXML base parser with a version that uses a Java pull parser 
#  # library
#  def self.new(*args)
#    obj = allocate
#    obj.extend(JREXML::JavaPullParser)
#    obj.send :initialize, *args
#    obj
#  end
#end


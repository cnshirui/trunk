# file: xml_benchmarks.rb
#
# Each test has an XML Parser open a 98k XML document 
# and count one type of leaf element (466 entries).
# This is repeated a total of 50 times for each Parser.
# This whole loop is first done as a rehearsal and
# then repeated a second time.
#
# These results were produced on:
#   MacBook Pro
#   2.33 GHz Intel Core 2 Duo
#   4 GB memory
# running MacOS X 10.5.2
#
# Ruby versions tested:
#   MRI:   ruby 1.8.6 (2007-09-24 patchlevel 111) [universal-darwin9.0]
#   JRuby: ruby 1.8.6 (2008-03-20 rev 6255) [i386-jruby1.1RC3]
#
# Library versions MRI:
#   libxml-ruby 0.5.2.0
#   hpricot 0.6
#
# Library versions JRuby:
#   hpricot 0.6.161
#   jrexml 0.5.3
#
# ruby ./xmlbenchmark.rb
#
# Ruby platform: universal-darwin9.0
# 
# Rehearsal -------------------------------------------
# rexml     3.870000   0.050000   3.920000 (  4.054477)
# hpricot   1.100000   0.030000   1.130000 (  1.186130)
# libxml    0.200000   0.010000   0.210000 (  0.227036)
# ---------------------------------- total: 5.260000sec
# 
#               user     system      total        real
# rexml     3.830000   0.030000   3.860000 (  4.088498)
# hpricot   1.080000   0.020000   1.100000 (  1.177623)
# libxml    0.190000   0.010000   0.200000 (  0.213941)
# 
# jruby ./xmlbenchmark.rb
#
# Ruby platform: java
# 
# Rehearsal ---------------------------------------------------------
# rexml                   7.983000   0.000000   7.983000 (  7.983000)
# hpricot                 3.868000   0.000000   3.868000 (  3.869000)
# jdom_document_builder   1.287000   0.000000   1.287000 (  1.288000)
# ----------------------------------------------- total: 13.138000sec
# 
#                             user     system      total        real
# rexml                   6.938000   0.000000   6.938000 (  6.938000)
# hpricot                 3.254000   0.000000   3.254000 (  3.254000)
# jdom_document_builder   0.774000   0.000000   0.774000 (  0.774000)
# 
# Now add in JREXML to see if it speeds up rexml
# Rehearsal ------------------------------------------------
# rexml+jrexml   6.997000   0.000000   6.997000 (  6.997000)
# --------------------------------------- total: 6.997000sec
# 
#                    user     system      total        real
# rexml+jrexml   6.952000   0.000000   6.952000 (  6.953000)
#
# Ruby script and 100k data fle located here:
#
# svn co https://svn.concord.org/svn/projects/trunk/common/ruby/xml_benchmarks
#
require "rubygems"
require 'benchmark'
require "rexml/document"
require 'hpricot'
require 'xml/libxml' unless RUBY_PLATFORM =~ /java/ 
require 'open-uri'

if RUBY_PLATFORM =~ /java/ 
  include Java 
  import javax.xml.parsers.DocumentBuilder
  import javax.xml.parsers.DocumentBuilderFactory
  @dbf = DocumentBuilderFactory.new_instance
else
  @xml_parser = XML::Parser.new
end

@bundle_with_466_sock_entries = File.read("document.xml")

def rexml_count_socks
  doc = REXML::Document.new(@bundle_with_466_sock_entries).root
  socks = doc.elements.to_a('./sockEntries').length
end

unless RUBY_PLATFORM =~ /java/ 
  def libxml_count_socks
    @xml_parser.string = @bundle_with_466_sock_entries
    doc = @xml_parser.parse.root
    socks = doc.find('//sockEntries').length
  end
end

def hpricot_count_socks
  doc = Hpricot.XML(@bundle_with_466_sock_entries)
  socks = doc.search("//sockEntries").length
end

if RUBY_PLATFORM =~ /java/ 
  def jdom_document_builder_count_socks
    doc = @dbf.new_document_builder.parse("bundle_with_466_sock_entries.xml").get_document_element   
    socks = doc.getElementsByTagName("sockEntries")
    socks.get_length
  end
end

n = 50
puts "Ruby platform: #{RUBY_PLATFORM}"
puts "Open 98k XML document and count one type of leaf element (466 entries): #{n} times."
Benchmark.bmbm do |x|
  x.report("rexml") { n.times {rexml_count_socks} }
  x.report("hpricot")  { n.times {hpricot_count_socks} }
  x.report("jdom_document_builder")  { n.times {jdom_document_builder_count_socks} } if RUBY_PLATFORM =~ /java/ 
  x.report("libxml") { n.times {libxml_count_socks} }  unless RUBY_PLATFORM =~ /java/ 
end

if RUBY_PLATFORM =~ /java/ 
  require 'jrexml'
  def rexml_with_jrexml_count_socks
    doc = REXML::Document.new(@bundle_with_466_sock_entries).root
    socks = doc.elements.to_a('./sockEntries').length
  end

  puts "\n\nNow add in JREXML to see if it speeds up rexml"
  Benchmark.bmbm do |x|
    x.report("rexml+jrexml") { n.times {rexml_with_jrexml_count_socks} }
  end
end

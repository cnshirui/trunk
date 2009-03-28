## Benchmark [ruby]

require 'benchmark'
require 'rexml/document'

xml = File.open(File.dirname(__FILE__) + "/atom_feed.xml") {|f| f.read }

Benchmark.bm(7) do |x|
  x.report("REXML") do
    10.times do
      REXML::Document.new xml
    end
  end
  x.report("JREXML") do
    require 'jrexml'
    10.times do
      REXML::Document.new xml
    end
  end
end


## Results [plaintext]
             user     system      total        real
REXML   20.089000   0.000000  20.089000 ( 20.089000)
JREXML   2.954000   0.000000   2.954000 (  2.954000)

## Code [ruby]

require 'rexml/parsers/baseparser'
require 'jrexml/java_pull_parser'

class REXML::Parsers::BaseParser
  # Extend every REXML base parser with a version that uses a Java pull parser 
  # library
  def self.new(*args)
    obj = allocate
    obj.extend(JREXML::JavaPullParser)
    obj.send :initialize, *args
    obj
  end
end
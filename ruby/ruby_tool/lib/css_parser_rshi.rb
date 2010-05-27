require 'css_parser'
include CssParser

parser = CssParser::Parser.new
parser.add_block!(File.new("data/art.css").read)
#puts parser.to_s
#parser[/gauge/]
parser.find_by_selector(".gauge2").each do |style|
  puts style
end


def parse

end

dir = "D:\\Perforce\\depot2\\X6\\COR\\Xcelsius\\Flash\\skins"
Dir.chdir(dir)
Dir["*/css/art.css"].each do |file|
  #  puts file
end
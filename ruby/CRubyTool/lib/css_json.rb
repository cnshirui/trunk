require 'json'

#parser = JSON::Parser.new(File.new("data/art.css").read)
#puts parser.source

result = JSON.parse(File.new("data/art.css").read)
result[/gauge/].each do |result|
  puts result
end
require 'net/http'

#Dir.chdir("d:/download")
#Dir["*.swf"].each do |file|
#  puts file, file.class
#  puts File.new(file).read[0..2] == "CWS"
#end


File.open('./public_xcelsius_templates.txt') do |file|
  file.each_line do |line|
    id, name, uuid, visibility, type = line.split("\t")
    
    url = "http://van-r-server02/analytics/download/#{uuid}"
    msg = "Download analytic '#{name}': #{url}"
    result = Net::HTTP.get(URI(url))
    if(result[0..2] == "CWS")
      puts "OK: #{msg}"
    else
      puts "Error: #{msg}"
    end
  end
end

puts "Hello World"

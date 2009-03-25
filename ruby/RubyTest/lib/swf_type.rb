require 'net/http'

#Dir.chdir("d:/download")
#Dir["*.swf"].each do |file|
#  puts file, file.class
#  puts File.new(file).read[0..2] == "CWS"
#end

def print_line line
  len = line.length
  add = len > 160?"":"."*(160-len)
  print "#{line}#{add}"
end


File.open('./public_xcelsius_templates.txt') do |file|
  file.each_line do |line|
    id, name, uuid, visibility, type = line.split("\t")
    
    url = "http://van-r-server02/analytics/download/#{uuid}"
    print_line  "Download analytic \"#{name}\": #{url}"

    begin
      result = Net::HTTP.get(URI(url))
    rescue Timeout::Error
      puts "Execution expired!"
    rescue Exception => e
      puts e.to_s, e.backtrace.join("\n")
      puts "Other system error!"
    else
      if(result[0..2] == "CWS")
        puts "OK!"
      else
        puts "Error!"
      end
    end
    

  end
end

puts "Hello World"

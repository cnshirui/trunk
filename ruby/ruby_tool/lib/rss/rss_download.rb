require 'net/http'
require 'uri'

dir_name = Time.now.to_s
dir_name.gsub!(/[:]/, "-")	# \s\+
puts dir_name
Dir.mkdir(dir_name)

File.open("rss.2.txt") do |file|
	Dir.chdir(dir_name)
	file.each do |line|
		puts "#{file.lineno}: #{line}"
		begin
			line.gsub!("\r", "")
			line.gsub!("\n", "")
			url = URI.parse(line)
			req = Net::HTTP::Get.new(url.path)
			res = Net::HTTP.start(url.host, url.port) {|http|
			  http.request(req)
			}
			
			# puts res.body		
=begin			
			File.open(file.lineno, "w+") do |new_file|
				new_file.write(res.body)
			end
=end			
		rescue Exception => e
			puts e # .backstrace.join("\n")
			next
		end
	end
end



=begin
url = URI.parse('http://www.example.com/index.html')
res = Net::HTTP.start(url.host, url.port) {|http|
  http.get('/index.html')
}
puts res.body

url = URI.parse('http://www.example.com/index.html')
req = Net::HTTP::Get.new(url.path)
res = Net::HTTP.start(url.host, url.port) {|http|
  http.request(req)
}
puts res.body
=end

# puts "read" if(File.exist?("rss.1.txt"))
# puts "done..."
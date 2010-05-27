require 'iconv'

cd = Iconv.new("gbk", "utf-16")	#  ("utf-16", "utf-8")	
input = File.new("pg00013.html")
output = File.open("output.html", "w+")

begin
input.each do |s| 
	begin
		output << cd.iconv(s) 
	rescue
		puts "failed"
	end
end
output << cd.iconv(nil)                   # Don't forget this!
ensure
cd.close
end

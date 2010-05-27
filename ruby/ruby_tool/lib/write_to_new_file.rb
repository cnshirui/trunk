file_name = "rss_#{Time.new.to_i}.txt"
File.open(file_name, "w+") do |file|
	file.write "abc..."
end
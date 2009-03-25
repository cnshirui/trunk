Dir.chdir("D:/Perforce/Whohar/CXOnline/WebDesigner/test/jetty/deploy/xcelsius/themes/built-in")
i = 0
Dir["*"].each do |file|
	# {label:"Admiral", data:0}, 
	
	puts "{label:\"#{file[0..-5]}\", data:#{i}}, "
	i += 1
end	




=begin
array = "["
array += "\"#{file[0..-5]}\", "
array += "]"
puts array
=end
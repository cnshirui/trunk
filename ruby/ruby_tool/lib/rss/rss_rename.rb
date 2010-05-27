puts "ARGV The alias to the $*."
puts ARGV.length
puts ARGV[0]

Dir.chdir(ARGV[0])

Dir["*.html"].each do |file_name|
	# file = File.new(file_name)
	# content = File.new(file_name).read
	content = ""
	File.open(file_name) do |file|
		content = file.read
	end
	content.match(/【(.+)】/)
	if($1)
		new_file_name = $1 + ".html"
		new_file_name.gsub!(/[\/\*:]/, "_")
		puts "#{file_name} => #{new_file_name}"
		File.rename(file_name, new_file_name)
	end
end

=begin
line = "<title>【绩效考核专员，上海盖世网络技术有限公司】前程无忧官方招聘网站</title>"
line.match(/<title>(.+)<\/title>/)
new_name = $1
puts new_name
=end
Dir.chdir("d:/download/disk_list")
content = ""
Dir["*"].each do |file|
  content += "#{file}\n #{File.new(file).read}\n\n"
end

File.open("d:/all_disk.txt", "w+") do |file|
  file.write content
end

puts "done!"
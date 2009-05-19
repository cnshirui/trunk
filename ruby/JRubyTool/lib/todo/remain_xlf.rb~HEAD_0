require 'fileutils'

puts "start..."

#dir = "D:/perforce/whohar/v5/whohar/runtime/development/public/xcelsius_template_subclass_extension/xcelsius_file"
dir = "D:/Shared/database.backup/production.bk.1/public/xcelsius_template_subclass_extension/xcelsius_file"
Dir.chdir(dir)

Dir["*/*"].each do |file|
  if(File.directory?(file))
    FileUtils.rm_rf(file)
    puts "delete #{file}"
  elsif(!file.match(/\.xlf/i))
    FileUtils.rm_f(file)
    puts "delete #{file}"
  else
    puts "remain #{file}"
  end
end


dir = "D:/Shared/database.backup/production.bk.1/public/xcelsius_template_subclass_extension/template_file"
Dir.chdir(dir)

Dir["*/*"].each do |file|
  if(File.directory?(file))
    FileUtils.rm_rf(file)
    puts "delete #{file}"
  elsif(!file.match(/\.swf/i))
    FileUtils.rm_f(file)
    puts "delete #{file}"
  else
    puts "remain #{file}"
  end
end









puts "end..."
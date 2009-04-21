require 'fileutils'

puts "start..."
Dir["../../../flex/*/bin*/*"].each do |file|
  if(File.directory?(file))
    FileUtils.rm_r(file)
  else
    FileUtils.rm(file)
  end
  puts "#{file}\t\t\t\tremoved!"
end


puts "end..."

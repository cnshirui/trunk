require 'fileutils'
require 'print_tool'

def clear_lower_version path
  Dir.chdir(path)

  # backup folder list
  old_list = Dir["*"].to_yaml

  hash = Hash.new
  Dir["*"].each do |file|
    from = file.index(/\d+\.\d+\.\d+/)
    unless(from && from>0)
      print_line "#{file}"
      puts "special!"
      next
    end
    plugin_name = file[0...from]

    next if(hash.has_key?(plugin_name) && file<hash[plugin_name])

    hash[plugin_name] = file
  end

  #hash.each_pair { |key, value|   puts "#{key} => #{value}" }

  Dir["*"].each do |file|
    unless(hash.has_value?(file))
      FileUtils.rm_rf(file)
      print_line "#{file}"
      puts "removed!"
    else
      print_line "#{file}"
      puts "ok!"
    end
  end

  # update folder list
  File.open("list_b.txt", "w+") { |file| file.write(old_list) }
  File.open("list_c.txt", "w+") { |file| file.write(Dir["*"].to_yaml) }
end

#clear_lower_version("D:/dev/plugins/")
clear_lower_version("D:/dev/eclipse-rshi/plugins/")
clear_lower_version("D:/dev/eclipse-rshi/features/")

puts "end..."
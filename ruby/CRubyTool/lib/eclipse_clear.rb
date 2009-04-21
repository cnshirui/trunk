require 'fileutils'

def clear_lower_version path

  # backup folder list


  Dir.chdir(path)
  hash = Hash.new
  Dir["*"].each do |file|
    plugin_name = file.match(/[A-Za-z\.]+/).to_s

    if(hash.has_key?(plugin_name) && file<hash[plugin_name])
      next
    end

    hash[plugin_name] = file
  end

  Dir["*"].each do |file|
    unless(hash.has_value?(file))
      FileUtils.rm_rf(file)
      puts "#{file}...removed!"
    end
  end

  # update folder list
end

clear_lower_version("D:/dev/eclipse-rshi/plugins/")
clear_lower_version("D:/dev/eclipse-rshi/features/")

puts "end..."
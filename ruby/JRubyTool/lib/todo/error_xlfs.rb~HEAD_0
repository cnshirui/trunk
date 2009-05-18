require 'yaml'
require 'fileutils'

xod_unsupported_xlfs = []
File.open(File.join("lib/tasks/error_xlf.yml")) do |file|
  error_templates = YAML::load(file)
  xod_unsupported_xlfs = error_templates["xod_unsupported"].to_a.map { |item|  item[0]}
end

Dir.chdir("D:/Shared/database.backup/production.bk.1/xcelsius_file")
xod_unsupported_xlfs.each do |name|
  Dir["*/#{name}"].each do |xlf|
    puts File.dirname(xlf)
#    dir =  "d:/error_xlfs/#{File.dirname(xlf)}"
#    FileUtils.mkdir_p(dir)
#    FileUtils.cp(xlf, "d:/error_xlfs/#{xlf}")
  end
end

puts "done!"
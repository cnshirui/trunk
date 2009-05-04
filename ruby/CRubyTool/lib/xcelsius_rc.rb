require 'print_tool'

dir_b = "D:/perforce/depot2/X5/COR/Xcelsius/Designer/Xcelsius/Xcelsius/res_b/"
dir = "D:/perforce/depot2/X5/COR/Xcelsius/Designer/Xcelsius/Xcelsius/res/"
compare_tool = "C:/Program Files/Beyond Compare 3/BCompare.exe"

Dir.chdir(dir)
puts Dir["*/xcelsius.rc*"].length
Dir["*/xcelsius.rc*"].each do |file_name|

  # File.open(file_name) { |file| content = file.read }
  content = File.new(file_name).read
  File.chmod(0777, file_name)
  results = content.scan(/Xcelsius\.Document/)
  count = results ? results.length : 0
  # Regexp.new("Xcelsius.Document")
  # /Xcelsius\.Do[ck]ument/
  content.gsub!(/Xcelsius\.Document/, "Xcelsius.5.Document")
  File.open(file_name, "w+") { |file| file.write(content) }

#  Thread.new { exec("#{compare_tool} #{dir_orig + file_name} #{dir + file_name}"); Thread.stop; }
#  Process.fork  { exec("#{compare_tool} #{dir_b + file_name} #{dir + file_name}"); Thread.stop; }
#  Thread.pass
  print_line file_name
  puts count
end
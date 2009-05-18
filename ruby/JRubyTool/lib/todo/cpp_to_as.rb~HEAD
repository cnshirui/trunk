cpp_file = "test.cpp"

Dir.chdir(File.dirname(__FILE__))

# read type conversion from YAML 
type_list = {}
File.open("data/types.yaml") do |file|
  file.each_line do |line|
    c_type, as_type = line.split(/: |\n/)
    type_list[c_type] = as_type
  end
end

  
# code translator from CPP
content = ""
File.open(cpp_file) do |file|
  content = file.read
  
  content.gsub!("*", "")
  content.gsub!("->", ".")
  content.gsub!("unsigned int", "uint")
  
  type_list.each_pair do |c_type, as_type|
    # int i; => var i:int;
    content.gsub!(Regexp.new("#{c_type} \\w+")) do |match|
      c_type, var = match.split(" ")
      new_del = "var #{var}:#{as_type}"
      new_del[0..-2]
    end
  end
end

puts content

File.open(cpp_file.sub!(".cpp", ".as"), "w") do |file|
  file.write content
end
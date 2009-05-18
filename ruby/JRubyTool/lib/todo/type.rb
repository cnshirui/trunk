# read type conversion from YAML 
type_list = {}
File.open("types.yaml") do |file|
  file.each_line do |line|
    c_type, as_type = line.split(/: |\n/)
    type_list[c_type] = as_type
  end
end

begin
  
# code translator from CPP
content = ""
File.open("document.cpp") do |file|
  content = file.read
  type_list.each_pair do |key, value|
    content.gsub!(key) do |match|
      # int i; => var i:int;
    end
  end
end

  
puts content
  
end
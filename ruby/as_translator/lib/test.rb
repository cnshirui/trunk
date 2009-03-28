time = Time.now
File.open("XDocument.#{time.min}.#{time.sec}.as", "w") do |file|
  file.write "shirui"
end


=begin
c_type = "MSXML2::IXMLDOMNodePtr"
as_type = "XML"
line = "MSXML2::IXMLDOMNodePtr propNode=NULL;"

line.gsub!(Regexp.new("#{c_type} \\w+")) do |match|
  c_type, var = match.split(" ")
  puts c_type, var, "-----"
  "var #{var}:#{as_type}"
end

puts line, c_type

c_line = "int i;"
type = "int";
as_line = "var i:int;"
c_line.gsub!(Regexp.new("#{type} \\w")) do |match|
  c_type, var = match.split(" ")
  puts c_type, var
end

puts Regexp.new("#{type} \\w")
puts Regexp.new('\w')
=end
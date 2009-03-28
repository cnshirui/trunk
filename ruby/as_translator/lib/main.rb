# 
# To change this template, choose Tools | Templates
# and open the template in the editor.

require "yaml"

puts "Hello World"
#type_list = YAML.load_file("types.yaml")
#
#type_list.each_pair { |key, value|  
#  print "#{key}: #{value}\n"
#}
#
#puts type_list["TRUE"]

File.open("types.yaml") do |file|
  file.each_line do |line|
    c_type, as_type = line.split(/: |\n/)
    print "#{c_type} => #{as_type}\n"
  end
end
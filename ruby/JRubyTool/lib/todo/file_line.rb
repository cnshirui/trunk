# To change this template, choose Tools | Templates
# and open the template in the editor.

puts "Hello World"

content = ""
File.open('data/1.txt') do |file|
  file.each do |line|
    content += line.sub(/2\s+\r/, "1\r")
  end
end

puts content
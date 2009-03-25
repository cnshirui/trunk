# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

puts "Hello World"


count = 0
content = ""
puts Dir.pwd
File.open("lib/IDocument.as") do |file|
    content = file.read
end

content.each_line do |line|
  puts "#{count += 1}: " + line
##  args = line.match(/\(.+\)/)
##  puts args
##  new_args = args.split(/\(|,|\)/).map do |pair|
##    type, var = pair.split
##    "#{var}:#{type}"
##  end if args
##  puts "#{count += 1}: " + new_args 
#  
##  new_args.delete_at(0)
##  new_args = new_args.join(", ")
##  new_args = "(#{new_args})"
##  puts new_args
end

# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 
def show_regexp(a, re)
  if a=~ re
    puts "#{$`}<<#{$&}>>#{$'}"
  else
    puts "no match"
  end
  puts $1,$2
end

#show_regexp("this is\nthe time", /^the/)
#show_regexp("this is\nthe time", /is$/)
#show_regexp("this is\nthe time", /\Athis/)
#show_regexp("this is\nthe time\n", /\Ztime/)
#show_regexp("this is\nthe time", /\Z/)
#show_regexp("this is\nthe time", /\Athe/)
#show_regexp("banana", /(an)*/)
show_regexp('He said "Hello"', /(\w)\1/)
show_regexp('Mississipip', /(\w+)\1/)
puts 

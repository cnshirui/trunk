# To change this template, choose Tools | Templates
# and open the template in the editor.

puts "Hello World"
str = "javax.xml_1.3.4.v200806030440.jar"
from = str.index(/\d+\.\d+\.\d+\d/)
puts from
puts from.nil?
puts str[0...from]

# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

puts "Hello World"
pre_map = false
cur_map = false


File.open("component_file_list.xml") do |file|
  file.each_line do |line|
    if line =~ /Province|Country|Continent/
      pre_map = cur_map
      cur_map = true
#      puts "<!-- #{line[0..-2]} -->"
    else
      pre_map = cur_map
      cur_map = false
    end
    
    
    puts "\n<!-- map component" if !pre_map && cur_map
    puts "-->\n\n" if pre_map && !cur_map
#    puts line[0..-2]
    print line
  end
end

puts Time.now
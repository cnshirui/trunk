require 'rexml/document'


# generate a Hash including supported component
support_file = File.open("category.txt")

line_count = 0
empty_count = 0
spport_count = 0
support_components = Hash.new

support_file.each do |line|
  if(line.length > 2)
    className, supported = line.split
#    puts className.length
    if(supported)
#      puts className
      support_components[className] = 1
      spport_count += 1
    end
    line_count += 1
  else
    empty_count += 1
  end
end

puts line_count, empty_count, line_count + empty_count
puts spport_count, support_components.size#, support_components.inspect

# read category_en.xml and comment unsupported components
file = File.open("category_en.xml")

uncomment_count = 0

file.each do |line|
  from = line.index("<symbol>")
  to = line.index("</symbol>")
  if(from != to)
    class_name = line[from+8...to]
    if(support_components.has_key?(class_name))
      puts line[0..-3]
      uncomment_count += 1
    elsif(class_name.index("xcelsius.maps") == -1)
      puts line[0..-3]
    else
      line.insert(from, "<!--")
      puts line[0..-3] + "-->"
    end
  else
    puts line[0..-3]
  end
end

puts uncomment_count

content_output = ""
File.new("data/lottery_3d.txt").each_line do |line|
  day_index, numbers = line.split
  if(numbers)
    numbers_array = numbers.split(",")
    number_one = numbers.gsub!(",", "")
#    content_output += "#{day_index}\t#{number_one}\r"
    content_output += "#{number_one}\r"
  end
end

puts content_output
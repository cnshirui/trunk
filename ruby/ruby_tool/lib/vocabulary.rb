MODE_INTERVAL = 0
MODE_SEQUENCE = 1
# TIMES = [0.2, 1, 8, 10, 20, 40, 70, 150]
TIMES = [0.2, 1, 8, 10, 30, 70, 140, 290]
# TIMES = {0.2, 1, 8, 10, 20, 60, 310}

count_page = 310
page_per_unit = 6
count_unit = count_page / page_per_unit

puts "10 words per page: 5 minutes"
puts "6 pages: 30 mins, then 3 mins to review 5 pages"
puts "next 6 pages, total 1 hour and 6 mins"
puts "review 12 pages in the evening"
puts "the first 3 remember period in the current day"

days = []
times_actual = TIMES[3..-1]
puts "total units: #{count_unit}"
count_unit.times do |i|
  times_actual.each do |j_a|
    j = j_a / 10
    days[i+j] = [] unless days[i+j]
    days[i+j] << i + 1
  end
end

days.each_index do |index|
  puts "day #{index}: #{days[index].inspect}"
end


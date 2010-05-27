file_name = "horse_chess.txt"
hash = Hash.new

f = File.new(file_name)
f.each do |line|
  #  puts "#{f.lineno}: #{line}"
  if(hash[line].nil?)
    hash[line] = 1
  else
    hash[line] += 1
  end
end

hash.sort {|a,b| a[1]<=>b[1]}
hash.each do |key, value|
  puts "#{key} => #{value}"
end

#File.open(file_name) do |file|
#  puts file.read
#end


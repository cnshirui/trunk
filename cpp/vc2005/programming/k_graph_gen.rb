puts "OK"
puts "A" * 2
table = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

n_vertex = 26
n_edges = 26 * 25 / 2

content = ""
file = File.open("k_graph_gen_input.txt")
file.each do |line|
	2.times do |i|
		content += table[i, 1] + line[0, 1] +  table[i, 1] + line[1, 1] + "\n"
	end	
end	

=begin
n_vertex.times do |i|
	n_vertex.times do |j|
		content += table[i, 1]*2 + table[j, 1]*2 + "\n"
	end
end
=end

puts content 

File.open("k_graph_gen.txt", "w+") do |file|
	file.write content
end	
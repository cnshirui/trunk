require 'java'

#b = [[1],[2]].to_java(java.lang.Object[])
#b = [[1],[2]].to_java(java.lang.reflect.Array)
#  def to_java_2d_array(values_array_of_arrays)
#    # in JRuby 1.1.4 workaround
#    nrow = values_array_of_arrays.length
#    ncol = values_array_of_arrays[0].length
#    java_array_of_arrays = java.lang.Object[nrow, ncol].new
#    puts java_array_of_arrays.length
#    puts java_array_of_arrays[0].length
#
#    nrow.times do  |i|
##      java_array_of_arrays[i] = java.lang.String[ncol].new
#      ncol.times do |j|
#        java_array_of_arrays[i][j] = java.lang.String.new(values_array_of_arrays[i][j])
#      end
#    end
#
#    return java_array_of_arrays
#  end

  def to_java_2d_array(values_array_of_arrays)
    # in JRuby 1.1.4 workaround
    nrow = values_array_of_arrays.length
    ncol = values_array_of_arrays[0].length
    java_array_of_arrays = java.lang.Object[nrow, ncol].new
    java_array_of_arrays.each_with_index do |row, i|
      java.lang.reflect.Array.set(java_array_of_arrays, i, values_array_of_arrays[i].to_java)
    end

    return java_array_of_arrays
  end

values_array_of_arrays = [['a', '1', '2'],['b', '1', '3'],['c', '3', '2']]
new_ary = to_java_2d_array(values_array_of_arrays)
puts new_ary[0][0], new_ary[0][0].class
puts new_ary[2][2], new_ary[2][2].class
#puts java_array_of_arrays[2].class
#puts java_array_of_arrays[2][2].class
#puts java.lang.String.new.class

#    puts java_array_of_arrays.methods
#    java_array_of_arrays.each_with_index do |row, i|
#      java_array_of_arrays[i] = values_array_of_arrays[i].to_java
#      java_array_of_arrays[i].each_with_index do |col, j|
#        java_array_of_arrays[i][j]
#values_array_of_arrays[i][j]
#        puts java_array_of_arrays[i].class
#        puts java_array_of_arrays[i][j].class
#        java_array_of_arrays[i][j] = java.lang.String.new("a")
#        puts java_array_of_arrays[i][j].class
#        java_array_of_arrays[i][j] = values_array_of_arrays[i][j]
#        java.lang.Object.new
#      end
#      row = values_array_of_arrays[i]
#      java_array_of_arrays[i] = values_array_of_arrays[i].to_java(:object)
#      puts java_array_of_arrays[i][0].class
#      puts row
#      java_array_of_arrays[i] = row
#    end
  
#i = 3
#j = 3
#java_array_of_arrays = java.lang.Object[i, j].new
#java_array_of_arrays1 = java_array_of_arrays.map.to_java(java.lang.Object[])
#
##puts java_array_of_arrays.methods
#java_array_of_arrays.each_with_index do |row, i|
#  puts "#{i}: #{row}"
#end
#puts java_array_of_arrays.length
#puts java_array_of_arrays[0].length
#puts values_array_of_arrays.inspect
#new_array = values_array_of_arrays.map do |ary|
#  puts ary.inspect
#  ary.to_java
#end
#puts new_array.class
#java_array_of_arrays = new_array.to_java(java.lang.Object[])


#.map(&:to_java)
#(java.lang.reflect.Array.newInstance(java.lang.Object, 3)
#java_array_of_arrays = values_array_of_arrays.map(&:to_java).to_java(java.lang.Object[])
#java_array_of_arrays = values_array_of_arrays.map {|ary| ary.to_java :object}


#
#b =  [['a', '1', '2'],['b', '1', '3'],['c', '3', '2']]
#puts b.to_java(java.lang.Object[])










puts "Hello World"

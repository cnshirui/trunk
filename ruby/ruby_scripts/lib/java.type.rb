puts VERSION
puts PLATFORM
puts [1,2].to_java
values_array_of_arrays = [['1', '2'], ['3', '4']]
#java_array_of_arrays = values_array_of_arrays.map(&:to_java)
java_array_of_arrays = values_array_of_arrays.map do |ary|
  ary.to_java :string
end
java_array_of_arrays1 = java_array_of_arrays.to_java(java.lang.Object[])
puts java_array_of_arrays1
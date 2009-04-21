# To change this template, choose Tools | Templates
# and open the template in the editor.

Dir.chdir("D:/dev/eclipse-rshi/plugins/")

puts Dir["*"].length
hash = {} #Hash.new

Dir["*"].each do |file|
  plugin_name = file.match(/[A-Za-z\.]+/)
  
  if(!hash.has_key?(plugin_name))
    hash[plugin_name] = file
  else
    if(file > hash[plugin_name])
      hash[plugin_name] = file
    end
  end
  
  
end

puts hash.keys.length
puts "Hello World"

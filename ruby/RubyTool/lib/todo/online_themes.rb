# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

Dir.chdir("styles")
puts "<Themes>"

Dir["*"].each do |dir|
  File.open(dir + "/theme.xml") do |file|
    file.each do |line|
      puts line if(file.lineno != 1)
      puts "<dir>#{dir}</dir>" if(file.lineno == 3)
    end
  end
end

puts "</Themes>"
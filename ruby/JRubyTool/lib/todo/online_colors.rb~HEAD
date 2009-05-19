# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

Dir.chdir("themes/built-in")
Dir["*"].each do |file_name|
  File.open(file_name) do |file|
    file.each do |line|
      puts line if(file.lineno != 1)
    end
  end
end
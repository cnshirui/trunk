# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

puts "Hello World"


    File.open("d:/XML2Webservice/download/1.#{Time.now.to_i}.xml", "w+") do |file|
      file.write "shirui"
    end
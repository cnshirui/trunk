# 
# To change this template, choose Tools | Templates
# and open the template in the editor.
 

count = 0
File.open("XDocument.as") do |file|
  file.each_line do |line|
    if(line.match(/public function \w+\(/))
#      count += 1
#      puts "#{count}: #{line}"
      line.scan(/public function \w+\(/) do |match|
        func_name = "test" + match[16..16].upcase + match[17..-2]
#        puts "            xDocumentTS.addTest(new XDocumentTest(\"#{func_name}\"));"
        puts "        public function #{func_name}():void {"
        puts "            //TODO: implement the test"
        puts "        }", ""
      end
    end
  end
end

puts Time.now
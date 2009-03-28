require 'fileutils'

Dir.chdir('D:/perforce/gc_trunk/opensocial/shirui')
xml = File.new('roamer.xml').read
start = xml.index("<![CDATA[")
tail = xml.rindex("]]>")
xml.sub!(xml[start...tail], "<![CDATA[\n\n\n\n  ")

content = File.new('roamer.html').read
xml.insert(start + 11, content)

File.open('roamer.xml', "w+") do |file|
  file.write(xml)
end

# copy to server folder
#localhost = "D:/workspace/ruby/opensocial/public/os/"
localhost = "D:/perforce/gc_trunk/opensocial/shirui/"

#FileUtils.cp("roamer.xml", localhost + "roamer.xml")
FileUtils.cp("D:/workspace/flex/CampusRoamer/bin-debug/PersonDemo.swf", localhost + "PersonDemo.swf")

puts "done!"
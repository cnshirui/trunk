js_file = "D:/perforce/gc_trunk/opensocial/campus_roamer/campus_roamer.js"
template_file = js_file.sub(".js", "_template.xml")
out_file = js_file.sub(".js", ".xml")

content = File.new(js_file).read
result = File.new(template_file).read

script_start = "<script type=text/javascript'>"
content = script_start + "\n\n" + content

script_end = "</script>"
content += "\n\n" + script_end + "\n\n"

script_position = result.rindex("]]>")
result.insert(script_position-2, content)


puts result
File.open(out_file, "w+") do |file|
  file.write(result)
end


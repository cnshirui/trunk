puts "generating docs index..."


# read content from template
template = ""
File.open("index.template.html") do |file|
  template = file.read
end

list_start = template.index("</div>")

Dir["*"].each do |dir|
  if File.directory?(dir)
    doc_list = "<a href='docs/#{dir}'>#{dir}</a><br/>\n"
    template.insert(list_start, doc_list)
    list_start += doc_list.length
  end
end

puts template

File.open("index.html", "w") do |file|
  file.write template
end


puts "over@#{Time.now}"
require 'rss/1.0'         
require 'rss/2.0'         
require 'open-uri'         
        
# feed= "http://news.csdn.net/Feed.aspx?Column=15666b36-9a91-403c-ab31-2ebce8d6b4e8" # url或本地XML文件  
# feed = "http://rss.51job.com/rss/0200/Rss_0200_0600.xml"	# hr
feed = "http://rss.51job.com/rss/0200/Rss_0200_0100.xml"    # cs
content = ""         
open(feed) do |s|       
	content = s.read       
end       
rss = RSS::Parser.parse(content, false) # false表示不验证feed的合法性  

channel = rss.channel   
items = rss.items # rss.channel.items亦可   
  
puts "频道信息"  
puts "标题： #{channel.title}"  
puts "链接： #{channel.link}"  
puts "描述： #{channel.description}"  
puts "更新时间： #{channel.date}"  
puts "文章数量： #{items.size}"  

content = ""
  
for i in 0 ... items.size   
	content += "#{items[i].link}\r\n"
=begin	
  puts "----------- 文章#{i} -----------"  
  puts "\t标题： #{items[i].title}"  
  puts "\t链接： #{items[i].link}"  
  puts "\t发表时间： #{items[i].date}"  
  puts "\t内容： #{items[i].description}"  
=end  
end  

file_name = "rss_#{Time.new.to_i}.txt"
File.open(file_name, "w+") do |file|
	file.write(content)
end


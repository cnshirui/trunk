#insert_file_to_word.rb
require "win32ole" 


begin 
      word = WIN32OLE.connect("word.application") 
rescue 
      #如果没有打开的Word程序，创建一个 
      word = WIN32OLE.new("word.application") 
      word.documents.add 
end 

word.visible = true 

doc = word.ActiveDocument() 

#保存原有的文本字体 
old_font_name = word.selection.font.name 
old_font_size = word.selection.font.size 
old_font_italic = word.selection.font.italic 


#创建文件路径字体 
word.selection.font.name = "Arial Narrow" 
word.selection.font.size = 12 
word.selection.font.italic = true 


#输入文件路径 
word.selection.typeText("#{ARGV[0]}\n") 


#创建文件文本字体 
word.selection.font.name = "Courier New" 
word.selection.font.size = 10.5 
word.selection.font.italic = false 


#一行行写入，换行符用\11，这样可以作为一个整体来 
#处理文件，而不用分段。 
ARGF.each do |line| 
      word.selection.typeText("#{line.chomp}\x0b") 
end 


word.selection.typeText("\n") 


#恢复原有字体 
word.selection.font.name = old_font_name 
word.selection.font.size = old_font_size 
word.selection.font.italic = old_font_italic 

require 'jxml'


  def col_indice range_text, offset = 0
    index = range_text.split("!")[1].upcase
    start_col = index.scan(/[A-Z]+/)[0]
    return get_index_in_alphabet(start_col) + offset
  end

  def row_indice range_text, offset = 0
    index = range_text.split("!")[1].upcase
    start_rol = index.scan(/[0-9]+/)[0].to_i
    start_rol+offset
  end
  
  def get_index_in_alphabet col_str
    col_str.upcase
    str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    index = 1
    col_str.length.times do |i|
      index = index + str.index(col_str[i...i+1])+26*(col_str.length-i-1)
    end
    index
  end
  
  def from_rc(range)
    
  end

  # change range to row/column index, such as 
  # "Sheet1!$E$1" => "Sheet1!R1C5", Sheet1!$A$1:$C$7 => [[]]
  def range2rc(range)
    sheet_name = range.scan(/[^!]+/)[0]
    indices = range.scan(/\$\w/)

    unless(indices.length ==2 or indices.length ==4)
      puts "error: range index count!"
    end
    
    row_begin = indices[1][1,1]
    col_begin = indices[0][1] - 64    # 'A' ascii value
    
    if(indices.length == 4)
      row_end = indices[3][1,1]
      col_end = indices[2][1] - 64    # 'A' ascii value
    else
      row_end = row_begin
      col_end = col_begin
    end

#    puts row_begin, row_end, col_begin, col_end
    rc_array = []
    row_begin.upto(row_end) do|i|
      r_array = []
      col_begin.upto(col_end) do |j|
        r_array << "#{sheet_name}!R#{i}C#{j}"
      end
      rc_array << r_array
    end

    return rc_array
  end

  def set_array(node, array)
    @xml_doc = JXML::Document.new "<root/>"
    node = @xml_doc.find("/root", :first)

    array.each_index do |i|
      row_prop = @xml_doc.create_element('property')
      node.append_child(row_prop)
      row_prop.set_attribute('id', i.to_s)

      row_array = @xml_doc.create_element('array')
      row_prop.append_child(row_array)
      array[i].each_index do |j|
        col_prop = @xml_doc.create_element('property')
        row_array.append_child(col_prop)
        col_prop.set_attribute('id', j.to_s)
        string_node = @xml_doc.create_element("string")
        col_prop.append_child(string_node)
        string_node.value = array[i][j]
      end
    end

    puts "rshi.............", @xml_doc.to_xml
    File.open("./1.#{Time.now.to_i}.xml", "w+") do |file|
      file.write @xml_doc.to_xml
    end

  end

  from = " Sheet1!$A$1:$C$7"
#  array = range2rc(from)
#  set_array(nil, array)
def seperate_header_rows(range)
  sheet_name = range.scan(/[^!]+/)[0]
  indices = range.scan(/\$\w/)

  row_begin = indices[1][1,1]
  row_end = indices[3][1,1]

  if(row_begin == row_end)
    puts "range error: only 1 row, can't be seperated to header and column"
  else
    row_new = (row_begin.to_i + 1).to_s
  end

  col_begin = indices[0][1,1]
  col_end = indices[2][1,1]

  return ["#{sheet_name}!$#{col_begin}$#{row_begin}:$#{col_end}$#{row_begin}", \
      "#{sheet_name}!$#{col_begin}$#{row_new}:$#{col_end}$#{row_end}"]
end

seperate_header_rows(from)
  puts "end..."



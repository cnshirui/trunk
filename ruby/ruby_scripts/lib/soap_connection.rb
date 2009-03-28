require 'jxml'
require 'rexml/document'
require 'net/http'

# used to construct a xml node for SOAP Connection, need wsdl, data_name, data range, url range
class SoapConnection
  
  def initialize
    file_name = 'soap_element.xml'
    File.open(file_name) do |file|
      @xml_doc = JXML::Document.new(file.read)
    end
  end
  
  def parse_wsdl(url)
    set_wsdl_url(url)

    result = Net::HTTP.get(URI(url))
    doc = REXML::Document.new result
    headers = []
    rows = []
    doc.get_elements("definitions/types/xsd:schema/xsd:complexType").each do |node|
      name = node.attributes.get_attribute("name").value
      if(name == "Header")
        node.get_elements("xsd:sequence/xsd:element").each do |element|
          headers << element.attributes.get_attribute("name").value
        end
      elsif(name == "Row")
        node.get_elements("xsd:sequence/xsd:element").each do |element|
          rows << element.attributes.get_attribute("name").value
        end
      end
    end

    if(headers.length != rows.length)
      puts "parse wsdl error: header doesn't equal rows"
    end

    set_dims(headers)
  end

  def set_dims(columns)
    col_num = columns.size
    xpath = "/connection/properties/property[name/string = 'objOutput']/value"
    value_node = @xml_doc.find(xpath, :first)
    
    # lname = header or row
    xpath = "object/property/array/property/object/property/array/property/   \
        object[property[@id = 'lname'][string = 'header' or string = 'row']]/property[@id = 'dims']/number"
    list = value_node.find(xpath)
    list.each do |dim_node|
      dim_node.value = col_num.to_s + ".000000"
    end
    
    # add a column for header and every row
    xpath = "object/property/array/property/object/property/array/property/   \
        object[property[@id = 'lname'][string = 'header' or string = 'row']]/property[@id = 'children']/array/property[@id = '0']"
    add_column(value_node, xpath, columns)
    
    # lname = table
    xpath = "object/property/array/property/object[property[@id = 'lname']/string = 'table']/property[@id = 'dims']/number"
    dim_node = value_node.find(xpath, :first)
    dim_node.value = (col_num * 2).to_s + ".000000"

    # lname = GetDataTableResponse
    xpath = "object[property[@id = 'lname']/string = 'GetDataTableResponse']/property[@id = 'dims']/number"
    dim_node = value_node.find(xpath, :first)
    dim_node.value = (col_num * 2 + 1).to_s + ".000000"
  end
  
  def add_column(node, xpath, columns)
    list = node.find(xpath)
    
    list.each do |col_node|
      array_node = col_node.parent
      lname_node = col_node.find("object/property[@id = 'lname']/string", :first)
      lname_node.value = columns[0].to_s

      (columns.size - 1).times do |i|
        new_col_node = col_node.copy
        array_node.append_child(new_col_node)
        new_col_node.set_attribute('id', (i+1).to_s)
        lname_node = new_col_node.find("object/property[@id = 'lname']/string", :first)
        lname_node.value = columns[i+1].to_s
      end
    end
  end
  
  def set_url_endpoint(url)
    xpath = "/connection/properties/property[name/string = 'sUrlendpoint']/value/string"
    node = @xml_doc.find(xpath, :first)
    node.value = url.sub('wsdl', 'api')
  end
  
  def set_uuid(uuid)
    xpath = "/connection"
    node = @xml_doc.find(xpath, :first)
    node.set_attribute('id', uuid)
  end
  
  def get_properties
    node = @xml_doc.find("/connection/properties", :first)
    return node.to_xml
  end
  
  def get_bindings
    node = @xml_doc.find("/connection/bindings", :first)
    return node.to_xml
  end
  
  def set_url_binding(url_binding)
    xpath = "/connection/bindings/property[name/string = 'sUrlendpoint']/value/binding/endpoint"
    end_point = @xml_doc.find(xpath, :first)
    end_point.set_attribute('displayname', url_binding)

    # TODO: modify workbook name

    range_node = end_point.find("range", :first)
    range_node.value = url_binding
  end
    
  def set_data_binding(binding)
    xpath = "/connection/bindings/property[name/string = 'objOutput']/value/binding/endpoint"
    end_point_list = @xml_doc.find(xpath)
    header_rows = seperate_header_rows(binding)

    if(end_point_list.length != header_rows.length)
      puts "set data binding error: can't find enough end point"
    else
      header_rows.each_index do |i|
        end_point = end_point_list[i]
        end_point.set_attribute('displayname', header_rows[i])

        # modify range node
        range_node = end_point.find("range", :first)
        range_node.value = header_rows[i]

        # modify cells node
        array_node = end_point.find("cells/array/property[@id = '0']/array", :first)
        range_array = range2rc(header_rows[i])
        set_array(array_node, range_array)
      end
    end
  end

  # "Sheet1!$A$1:$C$7" => ["Sheet1!$A$1:$C$1", "Sheet1!$A$2:$C$7"]
  def seperate_header_rows(range)
    sheet_name = range.scan(/[^!]+/)[0]
    indices = range.scan(/\$\w+/)

    row_begin = indices[1][1..-1]
    row_end = indices[3][1..-1]

    if(row_begin == row_end)
      puts "range error: only 1 row, can't be seperated to header and column"
    else
      row_new = (row_begin.to_i + 1).to_s
    end

    # TODO: if column index is "AA", "AB", ... while note a single character
    col_begin = indices[0][1,1]
    col_end = indices[2][1,1]

    return ["#{sheet_name}!$#{col_begin}$#{row_begin}:$#{col_end}$#{row_begin}", \
        "#{sheet_name}!$#{col_begin}$#{row_new}:$#{col_end}$#{row_end}"]
  end

#   <cells>
#    <array>
#      <property id="0">
#        <array>
#          <property id="0">
#            <array>
#              <property id="0">
#                <string>Sheet1!R2C1</string>
#              </property>
#              <property id="1">
#                <string>Sheet1!R2C2</string>
#              </property>
#            </array>
#          </property>
  def set_array(node, array)
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
  end
  
  # change range to row/column index, such as
  # "Sheet1!$E$1" => "Sheet1!R1C5", Sheet1!$A$1:$C$7 => [[]]
  def range2rc(range)
    sheet_name = range.scan(/[^!]+/)[0]
    indices = range.scan(/\$\w+/)

    unless(indices.length ==2 or indices.length ==4)
      puts "error: range index count!"
    end

    # TODO: if column index increase to "AA", "AB",... instead of a single character
    row_begin = indices[1][1..-1].to_i
    col_begin = indices[0][1] - 64    # 'A' ascii value

    if(indices.length == 4)
      row_end = indices[3][1..-1].to_i
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
  
  def get_wsdl_url
    xpath = "/connection/Persist/array/property/object[property/string = '_wsdlURLItem']/property[@id = 'value']/string"
    node = @xml_doc.find(xpath, :first)
    return node.value
  end
  
  def set_wsdl_url(url)
    xpath = "/connection/Persist/array/property/object[property/string = '_wsdlURLItem']/property[@id = 'value']/string"
    node = @xml_doc.find(xpath, :first)
    node.value = url
    
    set_url_endpoint(url)
  end  
  
  def get_persist
    node = @xml_doc.find("/connection/Persist", :first)
    return node.to_xml
  end
  
  def to_s
    return @xml_doc.to_xml
  end

  def to_xml_node
    return @xml_doc.get_root_element
  end
end

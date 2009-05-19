require 'fileutils.rb'
require 'rexml/document'
require 'net/http'

require 'jzip'
require 'jxml'

require 'java'
import 'java.util.UUID'

class XcelsiusFile
  def initialize(xlf_file)
    if(File.exist?(xlf_file))
      @uid = "#{Time.now.to_i}.#{rand(1000)}"
      folder_name = "tempxlf/#{@uid}/"
      FileUtils.mkdir_p(folder_name) if(!File.exist?(folder_name))
      
      @file_name = folder_name + File.basename(xlf_file)
      @xml_file_name = folder_name + "document.xml"
      FileUtils.copy(xlf_file, @file_name)
    else
      puts "error: xcelsius file doesn't exist!"
      return nil
    end
    
    JZip::ZipFile.open(@file_name) do |zipfile|
      zipfile.extract('document.xml', @xml_file_name)
    end
  end
  
  def get_connection_type
    xml_doc = JXML::Document.new(File.new(@xml_file_name).read)
    connections = []
    xml_doc.find("/CXCanvas/connection").each do |node|
      # "xcelsius.rpc.FlashVars" => "FlashVars"
      class_name = node.attributes('className')[13..-1]
      connections << class_name if(class_name != "FlashVars")
    end
    
    connections.uniq!
    if(connections.length == 1)
      return connections[0]
    else
      raise JZipError, "error: multi-connections in document.xml..."
      return nil
    end
        
  end

  def set_soap_connection(data_variables)
    if(get_connection_type != "XMLConnection")
      puts "error: xcelsius file's connection type isn't XMLConnection!"
      return
    end

    File.open(@xml_file_name) do |file|
      @xml_doc = JXML::Document.new(file.read)
    end

    data_variables.each do |variable|
      variable_name = variable["name"]
      wsdl_url = variable["wsdl_url"]
      xpath = "/CXCanvas/connection[@className = 'xcelsius.rpc.XMLConnection'][properties/property[name/string = 'loadNames_ary'][value/array/property/string = '#{variable_name}']]"
      node = @xml_doc.find(xpath, :first)
      xml_con = XmlConnection.new(node.to_xml)
      node.parent.remove_child(node)

      soap_con = SoapConnection.new
      soap_con.set_name(xml_con.name)
      soap_con.set_uid(xml_con.uid)
      soap_con.set_url_binding(xml_con.url_range)
      soap_con.set_data_binding(xml_con.data_range)
      soap_con.parse_wsdl(wsdl_url)
      new_node = soap_con.to_xml_node
      new_node = @xml_doc.import_node(new_node)
      @xml_doc.get_root_element.append_child(new_node)
    end

    # update xml_doc to disk file
    File.open(@xml_file_name, "w+") do |file|
      file.write(@xml_doc.to_xml)
    end
  end

  def get_xlf_file
    JZip::ZipFile.open(@file_name) do |zipfile|
      zipfile.replace('document.xml', @xml_file_name)
    end
    
    return File.new(@file_name, "r")
  end

  class JZipError < StandardError ; end

  class XmlConnection

    attr_reader :name, :uid, :url_range, :data_range

    def initialize(xml_str)
      @xml_doc = JXML::Document.new(xml_str)
      @name = @xml_doc.get_root_element.attributes("displayName")
      @uid = @xml_doc.get_root_element.attributes("id")
      xpath = "/connection/bindings/property[name/string = 'loadRanges_ary']/value/binding/endpoint/range"
      @data_range = @xml_doc.find(xpath, :first).value
      xpath = "/connection/bindings/property[name/string = 'url']/value/binding/endpoint/range"
      @url_range = @xml_doc.find(xpath, :first).value
    end
  end

  # used to construct a xml node for SOAP Connection, need wsdl, data_name, data range, url range
  class SoapConnection

    def initialize
      @xml_doc = JXML::Document.new(File.new('soap_element.xml').read)

      # update uuid
      @xml_doc.find("/connection", :first).set_attribute("id", "BO#{get_uuid}")

      xpath = "/connection/bindings/property/value/binding/endpoint"
      @xml_doc.find(xpath).each do |node|
        node.set_attribute("id", "BO#{get_uuid}")
      end
    end

    def get_uuid
      return UUID.randomUUID().toString.gsub(/-/, "").upcase
    end

    def set_name(value)
      @xml_doc.get_root_element.set_attribute("displayName", value)
      xpath = "/connection/properties/property[name/string = 'connectionName']/value/string"
      @xml_doc.find(xpath, :first).value = value
    end

    def set_uid(value)
      @xml_doc.get_root_element.set_attribute("id", value)
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
            # <xsd:element name="item" type="xsd:string"/>
            obj = {}
            obj["name"] = element.attributes.get_attribute("name").value
            obj["type"] = element.attributes.get_attribute("type").value[4..-1]
            headers << obj
          end
        elsif(name == "Row")
          node.get_elements("xsd:sequence/xsd:element").each do |element|
            obj = {}
            obj["name"] = element.attributes.get_attribute("name").value
            obj["type"] = element.attributes.get_attribute("type").value[4..-1]
            rows << obj
          end
        end
      end

      if(headers.length != rows.length)
        puts "parse wsdl error: header doesn't equal rows"
      end

      set_dims(headers, rows)
    end

    def set_dims(headers, rows)
      col_num = headers.size
      xpath = "/connection/properties/property[name/string = 'objOutput']/value"
      value_node = @xml_doc.find(xpath, :first)

      # lname = header or row
      xpath = "object/property/array/property/object/property/array/property/   \
          object[property[@id = 'lname'][string = 'header' or string = 'row']]/property[@id = 'dims']/number"
      list = value_node.find(xpath)
      list.each do |dim_node|
        dim_node.value = col_num.to_s + ".000000"
      end

      # add a column for header
      xpath = "object/property/array/property/object/property/array/property/   \
          object[property[@id = 'lname']/string = 'header']/property[@id = 'children']/array/property[@id = '0']"
      col_node = value_node.find(xpath, :first)
      add_column(col_node, headers)

      # add a column for every row
      xpath = "object/property/array/property/object/property/array/property/   \
          object[property[@id = 'lname']/string = 'row']/property[@id = 'children']/array/property[@id = '0']"
      col_node = value_node.find(xpath, :first)
      add_column(col_node, rows)

      # lname = table
      xpath = "object/property/array/property/object[property[@id = 'lname']/string = 'table']/property[@id = 'dims']/number"
      dim_node = value_node.find(xpath, :first)
      dim_node.value = (col_num * 2).to_s + ".000000"

      # lname = GetDataTableResponse
      xpath = "object[property[@id = 'lname']/string = 'GetDataTableResponse']/property[@id = 'dims']/number"
      dim_node = value_node.find(xpath, :first)
      dim_node.value = (col_num * 2 + 1).to_s + ".000000"
    end

    def add_column(col_node, columns)
      array_node = col_node.parent
      lname_node = col_node.find("object/property[@id = 'lname']/string", :first)
      lname_node.value = columns[0]["name"]
      type_node = col_node.find("object/property[@id = 'type']/string", :first)
      type_node.value = columns[0]["type"]

      # currently we have only one element, so others need to be copied from it
      (columns.size - 1).times do |i|
        new_col_node = col_node.copy
        array_node.append_child(new_col_node)
        new_col_node.set_attribute('id', (i+1).to_s)
        lname_node = new_col_node.find("object/property[@id = 'lname']/string", :first)
        lname_node.value = columns[i+1]["name"]
        type_node = new_col_node.find("object/property[@id = 'type']/string", :first)
        type_node.value = columns[i+1]["type"]
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

#      xpath = "cells/array/property/array/property/array/property/string"
#      cells_node = end_point.find(xpath, :first)
#      cells_node.value = url_binding
       
      # modify cells node
      array_node = end_point.find("cells/array/property[@id = '0']/array", :first)
      range_array = range2rc(url_binding)
      set_array(array_node, range_array)
    end

    def set_data_binding(binding)
      xpath = "/connection/bindings/property[name/string = 'objOutput']/value/binding/endpoint"
      end_point_list = @xml_doc.find(xpath)


      xpath = "/connection/properties/property[name/string = 'objOutput']/value/
          object/property/array/property/object/property/array/property/   \
          object[property[@id = 'lname'][string = 'header' or string = 'row']]/property[@id = 'value']/array"
      value_node_list = @xml_doc.find(xpath)

      header_rows = seperate_header_rows(binding)
      if(end_point_list.length != header_rows.length)
        puts "set data binding error: can't find enough end point"
        return
      end

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

        # TODO: also need to set data range in properties part,
        # this should be moved to parse_wsdl
        set_array(value_node_list[i], range_array, true)
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
  #          <property id="0"> // from here
  #            <array>
  #              <property id="0">
  #                <string>Sheet1!R2C1</string>
  #              </property>
  #              <property id="1">
  #                <string>Sheet1!R2C2</string>
  #              </property>
  #            </array>
  #          </property>
    def set_array(node, array, set_nil = false)
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
          string_node.value = array[i][j] unless set_nil
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
end
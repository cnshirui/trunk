require "rubygems"
require "zip/zip"
require "rexml/document"
require "jxml"
require "xcelsius_swf_metadata"

class XcelsiusXlfMetadata < XcelsiusSwfMetadata

  def initialize(swf_file, xlf_file)
    super swf_file
    @xlf_file = xlf_file
    @data_str = "<connections/>"
  end  
  
  def extract_connection_info
    unzip_xlf_file
    extract_flashvars_info
    extract_xmlconnection_info
  end
  
  def unzip_xlf_file
    Zip::ZipFile.open("budget_planner.xlf", Zip::ZipFile::CREATE) do |zf|
      @xml_file = zf.read("document.xml")
      @xls_file = zf.read("xldoc")
    end
  end  
  
  def extract_flashvars_info
    xlf_xml = JXML::Document.new @xml_file
    data_xml = REXML::Document.new @data_str
    root_data_xml = data_xml.root
    
    node_component = root_data_xml.add_element "component", {"type" => "FlashVars", "name" => "FlashVars"}
    node_property = node_component.add_element "property", {"name" => "variableFormat"}
    node_string = node_property.add_element "string"
    node_string.add_text "XML"

    # look for FlashVars
    xpath = "//connection[@className='xcelsius.rpc.FlashVars']/metaData/data"
    index_range = 0
    xlf_xml.find(xpath).first.value.split("&").each do |line|
      words = line.split(/={rangeValuesXML:|}/)
      name_component = words[0]
      node = xlf_xml.find("CXCanvas/connection/bindings/property/value/binding/endpoint[@id='#{words[1]}']").first
      nodes_row = node.find("cells/array/property/array/property")
      count_row = nodes_row.last.attributes("id").to_i - nodes_row.first.attributes("id").to_i + 1
      nodes_col = node.find("cells/array/property/array/property/array/property")
      count_col = nodes_col.last.attributes("id").to_i - nodes_col.first.attributes("id").to_i + 1    
      node_range = node_component.add_element "range", {"num" => index_range += 1, "name" => name_component,"rows" => count_row.to_s, "cols" => count_col}

      node.find("cells/array/property/array/property/array/property/string").each do |cell|
        node_cell = node_range.add_element "cell"
        node_cell.add_text cell.value
      end    
    end   
    
    @data_str = data_xml.to_s
  end
  
  def extract_xmlconnection_info
    xlf_xml = JXML::Document.new @xml_file
    data_xml = REXML::Document.new @data_str
    root_data_xml = data_xml.root
    
    # look for XMLDataButton
    xpath = "//connection[@className='xcelsius.rpc.XMLConnection']//property[name/string='loadRanges_ary']//cells"
    index_range = 0
    xlf_xml.find(xpath).each do |node|
        node_component = root_data_xml.add_element "component", {"type" => "XMLConnection", "name" => "XMLConnection"}
        node_property = node_component.add_element "property", {"name" => "variableFormat"}
        node_string = node_property.add_element "string"
        node_string.add_text "XML"

        name_component = node.find("../../../../../..").first.find("properties/property[name/string='loadNames_ary']/value/array/property/string").first.value
        nodes_row = node.find("array/property/array/property")
        count_row = nodes_row.last.attributes("id").to_i - nodes_row.first.attributes("id").to_i + 1
        nodes_col = node.find("array/property/array/property/array/property")
        count_col = nodes_col.last.attributes("id").to_i - nodes_col.first.attributes("id").to_i + 1    
        node_range = node_component.add_element "range", {"num" => index_range += 1, "name" => name_component,"rows" => count_row.to_s, "cols" => count_col}

        node.find("array/property/array/property/array/property/string").each do |cell|
          node_cell = node_range.add_element "cell"
          node_cell.add_text cell.value
        end
    end 

    @data_str = data_xml.to_s    
  end
  
  def data
    @data_str
  end
  
  def definition
    @definition_str
  end
end

start_time = Time.now #------------------------------------------
abc = XcelsiusXlfMetadata.new "budget_planner.swf", "budget_planner.xlf"
abc.extract_connection_info
puts abc.data
puts "parse xml file: #{Time.now-start_time}s" #-------------------------------------


require 'fileutils'
require 'rexml/document'


# puts a width-equal line, filling spaces with "..."
def print_line line
  line_width = 140
  len = line.length
  add = len>line_width ? "" : "."*(line_width-len)
  print "#{line}#{add}"
end

def open_xlf src_xlf
  xlf_name = File.basename(src_xlf)

  # copy xcelsius file from cache to xod_server_temp_folder
  copy_folder = "copy.#{Time.now.to_i}.#{rand(1000)}"
  copy_path = File.join(XCELSIUS_SERVER_DIRECTORY, copy_folder)
  FileUtils.mkdir_p(copy_path, :mode => 0777) unless File.exist?(copy_path)
  FileUtils.copy(src_xlf, File.join(copy_path, xlf_name))

  # open xlf
  xml_response = XcelsiusOnLineResource.open_xlf(File.join(copy_folder, xlf_name))
  open_xlf_response = REXML::Document.new xml_response
  xlf_folder = XcelsiusOnLineResource.parse_xml(open_xlf_response, 'xlfFolder')[0]
  range = XcelsiusOnLineResource.parse_xml(open_xlf_response, 'xlfFile', 'type', 'range')[0]

  unless(xlf_folder && File.exist?(File.join(XCELSIUS_SERVER_DIRECTORY, xlf_folder, range)))
    raise "Open xlf service error: #{xlf_name}!"
  end

  return xlf_folder
end

def save_xlf xlf_folder, xlf_name, dest_xlf
  # save xlf`
  xml_response = XcelsiusOnLineResource.save_xlf(xlf_folder, xlf_name)
  save_xlf_response = REXML::Document.new xml_response

  # update xlf
  xlf_folder = XcelsiusOnLineResource.parse_xml(save_xlf_response, 'xlfFolder')[0]
  xlf_file = XcelsiusOnLineResource.parse_xml(save_xlf_response, 'xlfFile', 'type', 'xlf')[0]
  unless(xlf_folder && File.exist?(File.join(XCELSIUS_SERVER_DIRECTORY, xlf_folder, xlf_file)))
    raise "Save xlf service error: #{xlf_name}!"
  else
    FileUtils.copy(File.join(XCELSIUS_SERVER_DIRECTORY, xlf_folder, xlf_file), dest_xlf)
  end
end

def export_swf src_xlf, dest_swf, xlf_folder = nil
  xlf_name = File.basename(src_xlf)
  swf_name = File.basename(dest_swf)

  # copy xcelsius file from cache to xod_server_temp_folder
  if(xlf_folder)
    copy_path = xlf_folder
  else
    copy_folder = "copy.#{Time.now.to_i}.#{rand(1000)}"
    copy_path = File.join(XCELSIUS_SERVER_DIRECTORY, copy_folder)
    FileUtils.mkdir_p(copy_path, :mode => 0777) unless File.exist?(copy_path)
    FileUtils.copy(src_xlf, File.join(copy_path, xlf_name))
  end

  # generate swf and update to runtime folder
  xml_response = XcelsiusOnLineResource.export_swf(File.join(copy_folder, xlf_name), swf_name, 'type')
  export_swf_response = REXML::Document.new xml_response
  xlf_folder = XcelsiusOnLineResource.parse_xml(export_swf_response, 'xlfFolder')[0]
  swf_file = XcelsiusOnLineResource.parse_xml(export_swf_response, 'xlfFile', 'type', 'swf')[0]
  unless(xlf_folder && File.exist?(File.join(XCELSIUS_SERVER_DIRECTORY, xlf_folder, swf_file)))
    raise "SWF Generator Error: #{File.basename(dest_swf)}!"
  else
    FileUtils.copy(File.join(XCELSIUS_SERVER_DIRECTORY, xlf_folder, swf_file), dest_swf)
  end
end

#
# clear runtime files except xlf/swf
#
def clear_runtime_file
  ext_folder = File.join(ActiveRecord::Base.runtime_public_directory, "xcelsius_template_subclass_extension")
  remain_type_file(File.join(ext_folder, "xcelsius_file"), "xlf")
  remain_type_file(File.join(ext_folder, "template_file"), "swf")
end

def remain_type_file(dir, type)
  Dir.chdir(dir)
  Dir["*/*"].each do |file|
    if(File.directory?(file))
      FileUtils.rm_rf(file)
    elsif(!file.match(/\.#{type}/i))
      FileUtils.rm_f(file)
    end
  end
  Dir.chdir(RAILS_ROOT)
end

def log_error_summary(id, msg)
  @error_summary[id] = msg
end

def print_error_summary
  puts "\n\n\n------------------ Error Summary: Total #{@error_summary.size} -------------------------"
  @error_summary.each_pair { |key, value|  puts "#{key}: #{value}"}
  puts "------------------ End -----------------------------------"
end

# print the running time of the algorithm
def print_time_expense
  puts "", sprintf("Running time: %.3f minutes", (Time.now - @start_time) / 60)
end

# used to summarize total time expense
@start_time = Time.now
@error_summary = {}

namespace :db do
  namespace :xcelsius_template do

    def resize_to_canvas_size ; end
    
    desc "Auto Detect Xcelsius Template Size from Xcelsius Files"
    task :resize_to_canvas_size => :environment do
      with_title_and_error_summary "Auto Detect Xcelsius Template Size from Xcelsius Files" do
        XcelsiusTemplate.find(:all).each do |x|
          filename = x.template_file.filename
          print_loading_message "Loading #{filename}"
          begin
            File.open(File.join("#{RAILS_ROOT}/sample_data/templates/data_files", filename)) do |template|
              basic_info = Array.new XcBurnTools.get_basic_info(template.path)
              x.update_attributes :width => basic_info[1], :height => basic_info[2] if basic_info
              print_result_message
            end
          rescue Exception => e
            print_result_message e
          end
        end
      end

      print_time_expense
    end

    def extract_sample_data ; end
    
    desc "Extract sample data from xcelsius files"
    task :extract_sample_data => :environment do
      with_title_and_error_summary "Extract sample data from xcelsius files" do
        template_name = ENV['NAME']
        template_list = ENV['LIST']
        clear_runtime_file

        # look for all templates to extract
        templates = []
        if template_name
          template = XcelsiusTemplate.find_by_name(template_name)
          templates << template
        elsif template_list
          list = File.open(template_list) { |f| f.read }.split("\r\n")
          XcelsiusTemplate.find(:all).each do |template|
            templates << template if(list.include?(template.template_file.filename))
          end
        else
          templates = XcelsiusTemplate.find(:all)
        end

        # extract for each template
        templates.each do |template|

          print_loading_message "Loading No.#{template.id}: #{template.name}"
          begin
            template.extract_sample_data_from_exl_xml
            template.save
            template.variables.map(&:save!)
            template.data_variables.each do |dv|
              dv.save!
              dv.sample_data_table.save! # in case of changes to the sample_data_table
            end
            print_result_message
          rescue Exception => e
            log_error_summary(template.id, e.message)
            print_result_message e
          end
        end

        print_error_summary
      end

      print_time_expense
    end

    def change_connection_type_xml2soap; end

    desc "Change all XML connection to WebService"
    task :change_connection_type_xml2soap => :environment do
      with_title_and_error_summary "Change all XML connection to WebService" do

        clear_runtime_file
        
        templates = XcelsiusTemplate.find(:all)
        templates.each do |x|
          id = x.super_template_id
          xlf = x.xcelsius_file
          xlf_name = xlf.filename

          print_loading_message "Loading No.#{id} #{xlf_name}"

          begin
            # all data_variable for template
            data_variables = x.data_variables

            # if length == 0, it shows no connectionn in template
            if(data_variables.length == 0)
              raise "no DataVariable, need to fix!"
            end

            # collect info of each data_variable to a soap_connection
            soap_connections = []
            data_variables.each do |v|
              # change connection type for each data_variable
              v.type = "SoapDataVariable"
              v.save

              # collect all data_columns for data_variable
              columns = []
              v.data_columns.each do |data_column|
                formula_type = Format::FormulaValueType.fromInt(data_column.formula_value_type_value)
                struct_type = DataTableApiTools::Struct_types_by_formula_value_types[formula_type]

                column = {}
                column[:name] = data_column.name
                column[:type] = struct_type.to_s
                columns << column
              end

              connection = {}
              connection[:name] = v.flashvar
              connection[:wsdl] = "http://localhost:3000/datasets/wsdl/--------------------------------"

              # parse data_column.name as web_service tag
              array = columns.map { |column| column[:name] }
              tags = DataTableApiTools.struct_member_names_for_array(array)
              tags.each_with_index { |tag, i| columns[i][:name] = tag }

              connection[:columns] = columns
              soap_connections << connection
            end

            # no soap_connection to update xlf
            if(soap_connections.length == 0)
              raise "Error: No analytic uses #{xlf_name}, so without WSDL to update it!"
            end

            # used to update xcelsius template files
            xf = XcelsiusFile.new(x.xcelsius_file.path)
            con_type = xf.get_connection_type

            # make sure both xlf and swf are soap.
            if(con_type == 'SoapConnector')
              if(!File.exist?(x.template_file.path) || File.mtime(x.xcelsius_file.path) > File.mtime(x.template_file.path))
                export_swf(x.xcelsius_file.path, x.template_file.path)
                puts "update!"
              else
                puts "soap, skip!"
              end
              next
            elsif(con_type != "XMLConnection")
              raise "Xcelsius unsupport connection type: #{con_type}"
            end

            # change connection type: xml => soap
            xf.set_soap_connection(soap_connections)
   
            # other will xcelsius_file => open_xlf => save_xlf => run_time
            xlf_folder = open_xlf(xf.path)
            range_file = File.join(XCELSIUS_SERVER_DIRECTORY, xlf_folder, "range.xml")
            xf.add_binding_range(range_file)

            # update data ranges, then save
            save_xlf(xlf_folder, xlf_name, x.xcelsius_file.path)

            export_swf(x.xcelsius_file.path, x.template_file.path)

            print_result_message
                        
          rescue Exception => e
            log_error_summary(id, e.message + " - " + xlf_name)
            print_result_message e
          end
        end

        print_error_summary
      end

      # clear xcelsius file cache in tempxlf
      XcelsiusFile.clear_cache

      print_time_expense
    end
  end
end

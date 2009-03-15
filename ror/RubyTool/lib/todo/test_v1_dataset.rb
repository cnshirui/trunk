require 'net/http'
require 'uri'
require 'rexml/document'
require 'jxml'

ALL_TYPES_DATASET_XML  = File.new(File.dirname(__FILE__) + "/all_types_dataset.xml").read

begin
  url = URI.parse('https://localhost:3000/v1/datasets/')
  req = Net::HTTP::Post.new(url.path)
  req.basic_auth 'wxiong', 'CorianderSageSaffron'
  req.set_content_type('text/xml')
  req["RAW_POST_DATA"] = ALL_TYPES_DATASET_XML


  res = Net::HTTP.new(url.host, url.port).start do |http|
    http.request(req)
  end

  case res
  when Net::HTTPSuccess, Net::HTTPRedirection
    # OK
    puts "#{Time.new}: #{res.inspect}, #{res.body}"
  else
    puts res.error!
  end
rescue Exception => e
  puts e, e.backtrace.join("\n")
end


=begin
1. modify datasets_api_controller.rb 235
      begin
        body_node = JXML::Document.new request.env['RAW_POST_DATA']
      rescue Exception => e
        body_node = JXML::Document.new request.request_parameters['RAW_POST_DATA']
      end
=end
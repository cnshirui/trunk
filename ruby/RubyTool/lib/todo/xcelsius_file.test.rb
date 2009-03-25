require 'xcelsius_file'

#file = "soap/test/line.xml.xlf"
file = "D:/perforce/whohar/v5/whohar/runtime/development/public/xcelsius_template_subclass_extension/xcelsius_file/1/whohar-2-datasets-chart.xlf"
xlf = XcelsiusFile.new(file)
#xlf = XcelsiusFile.new("soap/test/line.2.xml.xlf")

#new_xlf = xlf.get_xlf_xlf
#puts new_xlf.ctime
obj = {}
obj["name"] = "Data"
#obj["name"] = "Connection 2"
obj["wsdl_url"] = "http://localhost:3000/datasets/wsdl/MIV_TedbgGeKvQdLYeZu3R"

#obj2 = {}
#obj2["name"] = "Connection 3"
#obj2["wsdl_url"] = "http://localhost:3000/datasets/wsdl/Z_Nns77IhOWYdnu8Q0mU0j"
#

xlf.set_soap_connection([obj])
#xlf.set_soap_connection([obj, obj2])

time = Time.new.to_i
puts "generate a new xlf: #{time}"
File.open("soap/test/#{time}.xlf", "w+") do |dest|
  dest.write(xlf.get_xlf_xlf.read)
end

puts xlf.get_xlf_xlf.path
 

puts "end......"

require "win32ole"
require "mail_factory"

# excel name
filename = 'D:\careers\wujuan\hr_job_091010.xls'

# read file with excel
begin 
      excel = WIN32OLE.connect("Excel.Application") 
rescue 
      excel = WIN32OLE.new("Excel.Application") 
end 

wb = excel.workbooks.open(filename)
sht = wb.worksheets.item(1)

# calc count of jobs
job_count = 0
512.times do |i|
  if(sht["Cells", i+2, "A"].value)
    job_count += 1
  end
end

# stat all jobs
from_line = ARGV.length>0 ? ARGV[0].to_i : 1
end_line = job_count + 1
puts "send job form Row #{from_line} to Row #{end_line} ..."

Range.new(from_line, end_line).each do |i|
  job = sht["Cells", i, "A"].value
  email = sht["Cells", i, "B"].value
  puts "Row #{i}. Job: #{job}\t\tEmail: #{email} ..."
  send_job_request(email, job)
  puts "--------------------------------------------------------------, send over"
end

# send with stmp client
puts "all done..."
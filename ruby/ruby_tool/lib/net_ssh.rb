puts RUBY_VERSION

require 'rubygems'
require 'net/ssh'

5.times do |i|
  puts "ssh connecting to ... "
  begin
    Net::SSH.start('10.1.20.23', 'root', :password => "accelops123") do |ssh|
      # capture all stderr and stdout output from a remote process
      output = ssh.exec!("uname -a")
      puts output
    end
  rescue Net::SSH::Exception => e
#    puts e.backstrace
    puts e.message, e.class, e.backtrace
  end
end
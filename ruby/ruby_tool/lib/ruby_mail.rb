   require "net/smtp"

   # params :
   #   ARGV[0] = subject
   #   ARGV[1] = content
   #   ARGV[2] = filename
   #   ARGV[3] = to

   def sendemail(subject, content, to=nil)
      from = "wuj_424@126.com"
      to = ["shiruide@gmail.com"] if to.nil?
      sendmessage = "Subject: "+subject +"\n\n"+content

      smtp = Net::SMTP.new("smtp.126.com", 25)
      smtp.set_debug_output $stderr
      smtp.start("126.com", from, "200710251", :login) do |smtp|
        smtp.send_message(sendmessage, from, [to])
      end

#      smtp = Net::SMTP.start("mail.163.com", 25)
#      smtp.send_message(sendmessage, from, to)
#      smtp.finish
  end

  def sendemail_file(subject, filename, to)
      content = ""
      File.open(filename) do |file|
        file.each_line {|line| content += "#{line}\n" }
      end
      sendemail(subject, content, to)
  end


  subject = ARGV[0] || "system autoly send"
  content = ARGV[1] || ""
  filename = ARGV[2] || "data/WUJUAN_090927_GENERAL_WEB.html"
  to = ARGV[3]

  if content.to_s == "" and filename.to_s!=""
      sendemail_file(subject,filename,to)
  else
    puts "else..."
    content = "Nothing" if content.to_s == ""
    sendemail(subject, content, to)
  end  
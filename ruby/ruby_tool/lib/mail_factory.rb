require 'net/smtp'
require 'rubygems'
require 'mailfactory'

def send_job_request(email_to = "shiruide@gmail.com", job_desc = "人事助理")
  email_from = "wuj_424@126.com"
  email_content = "<p><font face='Verdana' color='#0000ff'>您好！</font></p><blockquote style='MARGIN-RIGHT: 0px' dir='ltr'><p><font   face=Verdana   color=#0000ff>我在互联网上看到贵公司的招聘，对你们的职位比较感兴趣，衷心希望加入贵公司。</font></p><p><font face='Verdana'   color=#0000ff>中英文简历在附件里，谢谢！</font></p></blockquote><p dir='ltr'><font face=Verdana color=#0000ff>-----------------------</font></p><p><font face='Verdana'></td><font color=#0000ff></font></font></p><p><font color='#0000ff'><font face='Verdana'>Best wish,</font> </font> </p><p><font face='Verdana' color='#0000ff'>吴娟（Juan Wu）</font> </p>"
  mail = MailFactory.new()


  mail.to = [email_to]	# join(',")
  mail.from = email_from
  mail.subject = "应聘#{job_desc}"
  #mail.text = "你能否成功收到邮件和附件，有无乱码？"
  mail.html = email_content
  mail.attach("D:\\careers\\wujuan\\resume\\WUJUAN_091005_PDF.pdf")

  smtp = Net::SMTP.new('smtp.126.com', 25)
  smtp.set_debug_output $stderr
  smtp.start('126.com', email_from, '200710251', :plain) do |smtp|
  #Net::SMTP.start('smtp.126.com', 25, '126.com', email_from, '200710251', :plain)  do |smtp|
    # mail.to = toaddress
    smtp.send_message(mail.to_s(), email_from, [email_to])
  end
 end 
 
# send_job_request("wuj_424@126.com")
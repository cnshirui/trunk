class Gmailer < ActionMailer::Base
  
  def signup_notification(user)
    #recipients "#{user.name} <#{shiruide@gmail.com}>"
    recipients "Shi Rui <shiruide@gmail.com>"
    from       "My Forum "
    subject    "Please activate your new account"
    sent_on    Time.now
    body :user => user, :url => "123 code", :host => "bbs.nju.edu.cn"
#      :url => activate_url(user.activation_code), :host => user.site.host
    render :text => "send email ok!"
  end

end

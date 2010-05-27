#ActionMailer::Base.delivery_method = CHITO_CONFIG[:mail][:method]
#ActionMailer::Base.smtp_settings = CHITO_CONFIG[:mail][:settings]

# {"mail"=>{"method"=>:smtp, "settings"=>{:port=>25, :domain=>"example.com", :address=>"mail.example.com"}}}
ActionMailer::Base.delivery_method = CHITO_CONFIG["mail"][:method]
ActionMailer::Base.smtp_settings = CHITO_CONFIG["mail"][:settings]

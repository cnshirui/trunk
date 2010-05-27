require 'net/http'

puts "begin"

Net::HTTP::Proxy("proxy", 8080).start('code.google.com') {|http|
	req = Net::HTTP::Get.new('/')
	#req.basic_auth 'account', 'password'
	response = http.request(req)
	print response.body		
}
	
=begin
    # Example
    proxy_class = Net::HTTP::Proxy('proxy.example.com', 8080)
                    :
    proxy_class.start('www.ruby-lang.org') {|http|
      # connecting proxy.foo.org:8080
                    :
    }

    require 'net/http'

    proxy_addr = 'your.proxy.host'
    proxy_port = 8080
            :
    Net::HTTP::Proxy(proxy_addr, proxy_port).start('www.example.com') {|http|
      # always connect to your.proxy.addr:8080
            :
    }
	
    require 'net/http'

    Net::HTTP.start('www.example.com') {|http|
      req = Net::HTTP::Get.new('/secret-page.html')
      req.basic_auth 'account', 'password'
      response = http.request(req)
      print response.body
    }
=end	
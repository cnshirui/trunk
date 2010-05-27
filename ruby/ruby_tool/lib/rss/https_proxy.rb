require 'net/http'
require 'net/https'

url = "https://mail.google.com/mail/"	# 'www.apple.com'

puts ENV['http_proxy']
proxy_url = URI.parse(ENV['http_proxy'])

proxy = Net::HTTP::Proxy(proxy_url.host,proxy_url.port,proxy_url.user,proxy_url.password)
puts proxy.get(URI.parse(url))


=begin
Net::HTTP::Proxy(proxy.host,proxy.port,proxy.user,proxy.password).start(url) do |http|
	http.use_ssl = true
	# http.verify_mode = OpenSSL::SSL::VERIFY_NONE
	puts http.get('/').body
end
=end
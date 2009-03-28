#!/usr/bin/env ruby
require 'win32ole'

$urls = []

#定义全局变量$ie作为ie自动化客户端的载体
$ie = nil
def navigate(url)
  $urls << url

  if url['sina'] || url['yahoo']
  #若发现sina或者yahoo字样则转向google
      $ie.navigate('http://www.google.com')
  end
end

#停止消息循环
def stop_msg_loop
  puts "Now Stop IE..."
  $LOOP = FALSE;
end

#默认的处理句柄
def default_handler(event, *args)
  case event
  when "BeforeNavigate"
    puts "Now Navigate #{args[0]}..."
  end
end

#创建IE自动化客户端
$ie = WIN32OLE.new('InternetExplorer.Application')
$ie.visible = TRUE
$ie.gohome

###############################################
#通过WIN32OLE_EVENT注册针对DWebBrowserEvents
#这个DispatchEvent接口的连接点
###############################################
ev = WIN32OLE_EVENT.new($ie, 'DWebBrowserEvents')


###############################################
#定义针对各种接口事件的回调函数
# default_handler(*args):默认的事件处理
# navigate(url): NavigateComplete事件的处理
# stop_msg_loop:Quit事件的处理
###############################################

ev.on_event {|*args| default_handler(*args)}
ev.on_event("NavigateComplete") {|url| navigate(url)}
ev.on_event("Quit") {|*args| stop_msg_loop}

$LOOP = TRUE
while ($LOOP)
  WIN32OLE_EVENT.message_loop
end

#打印那些浏览器访问过的URL
puts "You Navigated the URLs ..."
$urls.each_with_index do |url, i|
  puts "(#{i+1}) #{url}"
end

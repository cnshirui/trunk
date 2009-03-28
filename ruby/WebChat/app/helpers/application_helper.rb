# Methods added to this helper will be available to all templates in the application.
module ApplicationHelper
  
  def message_content(message)
    %(<div class="message #{message.level}" id="message-#{message.id}"><span class="time">#{message.created_at.strftime('%R')}</span> <span class="sender">#{message.sender.nickname}</span> <span class="content">#{message.content}</span></div>)
  end
  
end

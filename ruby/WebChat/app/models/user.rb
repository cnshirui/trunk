class User < ActiveRecord::Base
  has_many :lines, :foreign_key => 'sender_id'
  
  def say_in_chatroom(chatroom, say)
    Message.new(:chatroom => chatroom, :content => filterhtml(say), :sender => self).save
  end

  def action_in_chatroom(chatroom, say)
    Message.new(:chatroom => chatroom, :content => filterhtml(say), :sender => self, :level => 'action').save
  end
  
  def joins_chatroom(chatroom)
    Message.new(:chatroom => chatroom, :sender => self, :content => "加入聊天室", :level => 'sys').save
  end
  
  def leaves_chatroom(chatroom)
    Message.new(:chatroom => chatroom, :sender => self, :content => "离开聊天室", :level => 'sys').save
  end

  def filterhtml(say)
    say.gsub!(/\</, '&lt;')
    say.gsub!(/\>/, '&gt;')
    say
  end
end

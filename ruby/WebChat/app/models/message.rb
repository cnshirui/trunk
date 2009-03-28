class Message < ActiveRecord::Base
  belongs_to :chatroom
  belongs_to :sender, :class_name => 'User', :foreign_key => 'sender_id'
  
  validates_length_of :content, :in => 1..1000
end

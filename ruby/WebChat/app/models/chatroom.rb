class Chatroom < ActiveRecord::Base
  has_and_belongs_to_many :users do
    def cleanup
      ActiveRecord::Base.connection.execute("DELETE FROM chatrooms_users WHERE last_seen < DATE_SUB(\'#{ Time.now.strftime("%Y-%m-%d %H:%M:%S") }\', INTERVAL 1 MINUTE) OR last_seen IS NULL")
    end
  end
  
  has_many :messages, :order => 'created_at asc' do
    def since(time)
      find(:all, :conditions => ['created_at >= ?', time])
    end
  end
  
  def latest_messages(qty = 10)
    messages.find(:all, :limit => qty, :order => 'created_at DESC').reverse
  end
  
  def users_just_left
    User.find_by_sql(["SELECT u.* FROM users u, chatrooms_users cu WHERE cu.last_seen < DATE_SUB(?, INTERVAL 1 MINUTE) AND cu.user_id = u.id AND cu.chatroom_id = ?", Time.now, self.id])
  end
end

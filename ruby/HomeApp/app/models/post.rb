class Post < ActiveRecord::Base
  validates_presence_of :body, :title, :user_id
  has_many :comments
end

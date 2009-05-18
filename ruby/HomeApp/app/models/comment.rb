class Comment < ActiveRecord::Base
  validates_presence_of :user_id
  belongs_to :post
end

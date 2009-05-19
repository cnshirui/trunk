class Post < ActiveRecord::Base

  has_many :comments
  validates_presence_of :body, :title, :user_id

  acts_as_ferret :fields => [:body, :title]

end

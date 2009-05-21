class Post < ActiveRecord::Base

  has_many :comments
  validates_presence_of :body, :title, :user_id

  acts_as_ferret :fields => [:body, :title]

  PRIVACIES = ['private', 'friend', 'public']

  CATEGORIES = ['BEC', 'Ruby', 'Linux', 'Maths', 'Programming', 'Career', 'Others']

end

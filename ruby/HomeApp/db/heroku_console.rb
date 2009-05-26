=begin
Post.find(:all).each do |post|
  # post.user_id = 1
  # post.privacy = Post::PRIVACIES[0]
  # post.category = Post::CATEGORIES[0]
  # post.save
end

Comment.find(:all).each do |comment|
  comment.user_id = 1
  comment.save
end

User.find(:all).each do |user|
  user.is_admin = false
  user.save
end
=end

# set default privacy as 'friend' for all posts
Post.find(:all).each { |post| post.privacy = post.user_id == 2 ? 'public' : 'friend'; post.save }

# set local db's models order from 1
i = 1; Post.find(:all).each { |post| post.id = i; i += 1; post.save; }
i = 1; User.find(:all).each { |user| user.id = i; i += 1; user.save; }
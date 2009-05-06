class AddUserToPostComment < ActiveRecord::Migration
  def self.up
    add_column :posts, :user_id, :integer, :default => 1
    add_column :comments, :user_id, :integer, :default => 1
  end

  def self.down
    remove_column :posts, :user_id
    remove_column :comments, :user_id
  end
end

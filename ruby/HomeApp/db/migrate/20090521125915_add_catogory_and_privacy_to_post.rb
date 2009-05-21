class AddCatogoryAndPrivacyToPost < ActiveRecord::Migration
  def self.up
    add_column :posts, :category, :string, :default => 'BEC'
    add_column :posts, :privacy, :string, :default => 'private'
  end

  def self.down
    remove_column :posts, :category
    remove_column :posts, :privacy
  end
end

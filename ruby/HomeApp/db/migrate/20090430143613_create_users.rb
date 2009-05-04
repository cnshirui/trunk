class CreateUsers < ActiveRecord::Migration
  def self.up
    create_table :users do |t|
      t.column :name,            :string
      t.column :hashed_password, :string
      t.column :salt,            :string
    end
    
    User.create(:name => 'wujuan', :password => '05261234',
      :password_confirmation => '05261234')
  end

  def self.down
    drop_table :users
  end
end
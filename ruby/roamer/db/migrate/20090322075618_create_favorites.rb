class CreateFavorites < ActiveRecord::Migration
  def self.up
    create_table :favorites do |t|
      t.string :host_uid
      t.string :guest_uid

      t.timestamps
    end
  end

  def self.down
    drop_table :favorites
  end
end

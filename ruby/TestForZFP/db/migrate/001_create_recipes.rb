class CreateRecipes < ActiveRecord::Migration
  def self.up
    create_table :recipes do |t|
      t.column :title, :string
      t.column :summary, :text
      t.column :ingredients, :text
      t.column :instructions, :text
      t.column :cooking_time, :string
      t.column :created_at, :datetime
      t.column :updated_at, :datetime
    end
  end

  def self.down
    drop_table :recipes
  end
end

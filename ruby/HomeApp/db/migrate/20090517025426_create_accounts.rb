class CreateAccounts < ActiveRecord::Migration
  def self.up
    create_table :accounts do |t|
      t.string :title
      t.decimal :value
      t.string :category
      t.string :whose
      t.date :date

      t.timestamps
    end
  end

  def self.down
    drop_table :accounts
  end
end

# This file is auto-generated from the current state of the database. Instead of editing this file, 
# please use the migrations feature of Active Record to incrementally modify your database, and
# then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your database schema. If you need
# to create the application database on another system, you should be using db:schema:load, not running
# all the migrations from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20081218055821) do

  create_table "apps", :force => true do |t|
    t.string   "source_url"
    t.string   "title"
    t.text     "description"
    t.string   "directory_title"
    t.string   "title_url"
    t.string   "author"
    t.string   "author_email"
    t.string   "author_affiliation"
    t.string   "author_location"
    t.string   "screenshot"
    t.string   "thumbnail"
    t.integer  "height"
    t.integer  "width"
    t.boolean  "scaling"
    t.boolean  "scrolling"
    t.boolean  "singleton"
    t.string   "author_photo"
    t.text     "author_aboutme"
    t.string   "author_link"
    t.text     "author_quote"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "persistences", :force => true do |t|
    t.integer  "person_id"
    t.integer  "app_id"
    t.string   "type"
    t.string   "instance_id"
    t.string   "key"
    t.string   "value"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end

# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_FacebookerTest_session',
  :secret      => '61504640decc7bef8faaccd0a116f43b6e3f4ba6801a628d01ba4b7ff4f16008d693ef7e4e3b4e455aab89d08882eaf9dcda2e20fb551a38fbb382d8b91d3d8a'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store

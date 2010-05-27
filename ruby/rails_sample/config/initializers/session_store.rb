# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_rails_sample_session',
  :secret      => '164f953802dc7a11646d289fdacd83b39ea173096b9358e719f3bd35c729db55a4acbdf3fa0fbb1c0dbbe5ea12efa9def116f1a297581643c02751fb5df12a37'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store

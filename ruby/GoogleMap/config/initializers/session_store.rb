# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_GoogleMap_session',
  :secret      => 'a9448e497f91eabf43e8d28028e65a1336c90ed3bc275f004933a80d5316e725e5ee543caf30d09682c4d26b86b4aa70d759d1c70586c32cbe22cb9c0709c0b0'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store

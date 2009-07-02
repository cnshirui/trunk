# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_RFacebookTest_session',
  :secret      => 'd5461916409b73665f093d037e97ee0cf05ce2419abcdd142ec09df7d847705d93c25cff4f314124d294e5cf0ef940fbde7136dd1dd08141f80622d6201eacf6'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store

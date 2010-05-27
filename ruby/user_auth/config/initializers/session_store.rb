# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_user_auth_session',
  :secret      => '8f986fe0d36f01dbe04f667a0598c15e179bb3ef9d260e4587b960b5eadf981b6c5b4cbddf154bf9b563d0bf7d71722e63cfcf0cb194f4e54e872b776b407229'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store

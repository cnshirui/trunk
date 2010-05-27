# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_csmoe_session',
  :secret      => 'f287bdd9c10c86adcd67cab74feef65fe77d7867bf998a7c3ed9bce872fdb957d9479b34423f4510d5db9ed01e2100b37d213f9c7a5e76e74fd8c69749188afb'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store

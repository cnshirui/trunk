class FacebookController < ApplicationController

  before_filter :require_facebook_login

  def index
    # make Facebook call
    useridA = 1234
    useridB = 9876
    response = fbsession.users_getInfo(:uids => [useridA, useridB], :fields => ["first_name", "last_name"])

    # now, parse the user elements to get the names
    users = response.user_list
    @nameA = users[0].first_name
    @nameB = users[1].first_name
  end

end

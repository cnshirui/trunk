# Methods added to this helper will be available to all templates in the application.
module ApplicationHelper
  def access(model)
    value = false
    value = true if(model.privacy=='public')
    value = true if(model.privacy=='friend' && session[:user_id])
    value = true if(model.user_id == session[:user_id])
    return value
  end
end

# Filters added to this controller apply to all controllers in the application.
# Likewise, all the methods added will be available for all controllers.

class ApplicationController < ActionController::Base
  helper :all # include all helpers, all the time

  # See ActionController::RequestForgeryProtection for details
  # Uncomment the :secret if you're not using the cookie session store
  protect_from_forgery # :secret => 'c8a7945d67b6966438bcaa6f92ba40a7'
  
  # See ActionController::Base for details 
  # Uncomment this to filter the contents of submitted sensitive data parameters
  # from your application log (in this case, all fields with names like "password"). 
  # filter_parameter_logging :password

  private

  def authorize
    unless User.find_by_id(session[:user_id])
      flash[:notice] = "Please log in"
      redirect_to(:controller => "users", :action => "login")
    end
  end

  def admin
    user = User.find_by_id(session[:user_id])
    if user
      unless user.is_admin
        redirect_to(:controller => "posts")
      end
    else
      flash[:notice] = "Please log in"
      redirect_to(:controller => "users", :action => "login")
    end
  end
end

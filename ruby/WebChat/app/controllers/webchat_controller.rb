class WebchatController < ApplicationController

  layout 'general'  
  

  def visit
    @page_class = params[:style] if params[:style]

    unless session[:nickname] || params[:nickname]
      @chatrooms=Chatroom.find_all
      render 'webchat/get_nickname'
      return false
    end

    if params[:nickname]
      if (params[:nickname].length < 2 || params[:nickname].length > 25)
        flash[:error] = "请输入有效的昵称。"
        render 'webchat/get_nickname'
        return false        
      end
      session[:nickname] = params[:nickname]
      if params[:chatroom_title] != ""
        @chatroom = Chatroom.find_by_name(params[:chatroom_title])
        unless @chatroom
          @chatroom = Chatroom.new(:name=>params[:chatroom_title],:title=>params[:chatroom_title],:topic=>params[:chatroom_title]).save
          @chatroom = Chatroom.find_by_name(params[:chatroom_title])
        end
      else
        @chatroom = Chatroom.find_by_id(params[:chatroom_id])  
      end
      
    end

    @user = User.find_or_create_by_nickname(session[:nickname])

    unless @chatroom.users.include?(@user)
      @user.joins_chatroom(@chatroom)
      @chatroom.users.push_with_attributes(@user, { :last_seen => Time.now })
    end
  end

  # Post a user's message to a chatroom
  def say
    return false unless request.xhr?
    get_chatroom_and_user
    
    # Get the message
    message = params[:message]
    return false unless message
    
    if message.match(/^\/\w+/)
      # It's an IRC style command
      command, arguments = message.scan(/^\/(\w+)(.+?)$/).flatten
      logger.info(command)
      case command
        when 'me'
          @user.action_in_chatroom(@chatroom, arguments)
          @message = Message.find(:first, :order => "id DESC", :conditions => ["sender_id = ?", @user.id])
        when 'nick'
          #session[:nickname] = arguments
        when 'join'
          @aux_command = %(window.location = "webchat/visit/#{arguments}";)
        else
          return false
      end
    else
      @user.say_in_chatroom(@chatroom, message)
      @message = Message.find(:first, :order => "id DESC", :conditions => ["sender_id = ?", @user.id])
    end
    
    render :layout => false
  end

  # Get the latest messages since the user last got any
  def get_latest_messages
    return false unless request.xhr?
    get_chatroom_and_user
    @messages = @chatroom.messages.since(session[:last_retrieval])
    session[:last_retrieval] = Time.now
    render :layout => false
  end
  
  # Get a list of users for the chatroom and clean up any who left
  def get_user_list
    return false unless request.xhr?
    get_chatroom_and_user
    
    # Tell the database we're still alive
    @chatroom.users.delete(@user)
    @chatroom.users.push_with_attributes(@user, { :last_seen => Time.now })
    
    # Announce any users who just left
    if @chatroom.users_just_left
      for user in @chatroom.users_just_left
        user.leaves_chatroom(@chatroom)
      end
    end
    
    # Do a clean up of users in the chatroom
    @chatroom.users.cleanup
    render :layout => false
  end
  
  
  private
  
  # Get chatroom and user info that most methods use
  def get_chatroom_and_user
    @user = User.find_by_nickname(session[:nickname])    
    @chatroom = Chatroom.find_by_name(params[:chatroom_name])  
  end
end

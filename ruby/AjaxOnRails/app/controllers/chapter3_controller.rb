class Chapter3Controller < ApplicationController

  def get_time
    sleep 1.second
    render :text => Time.now.to_s
  end

  def repeat
    render :text => params.inspect
  end

  def reverse
    @reversed_text = params[:text_to_reverse].reverse
    render :layout => false
  end

  def index
  end
end

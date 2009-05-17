class CommentsController < ApplicationController
  def create
    @post = Post.find(params[:post_id])
    comment = params[:comment]
    comment['user_id'] = session[:user_id]
    @comment = @post.comments.create!(comment)
    
    # debugger
    respond_to do |format|
      format.html { redirect_to @post }
      format.js
    end
  end

  def edit
    @comment = Comment.find(params[:id])
  end

end

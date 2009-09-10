require 'utils/time'

class PostsController < ApplicationController
  
  before_filter :authorize, :except => [:index, :show]

  # GET /posts
  # GET /posts.xml
  def index
    @posts = Post.paginate(:page => params[:page], :order => "created_at DESC", \
        :per_page => 5, :conditions => ["user_id = ? AND privacy = ?", User.find_by_name("shirui").id, 'public'])

    @comments = Comment.find(:all)
    @comments.sort! { |a, b| a.updated_at <=> b.updated_at }
    @comments.reverse!
    
    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @posts }
      format.json { render :json => @posts }
      format.atom
    end
  end

  # GET /posts/1
  # GET /posts/1.xml
  def show
    @post = Post.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @post }
    end
  end

  # GET /posts/new
  # GET /posts/new.xml
  def new
    @post = Post.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @post }
    end
  end

  # GET /posts/1/edit
  def edit
    @post = Post.find(params[:id])
  end

  # POST /posts
  # POST /posts.xml
  def create
    @post = Post.new(params[:post])
    @post.user_id = session[:user_id]

    respond_to do |format|
      if @post.save
        flash[:notice] = 'Post was successfully created.'
        format.html { redirect_to(@post) }
        format.xml  { render :xml => @post, :status => :created, :location => @post }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @post.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /posts/1
  # PUT /posts/1.xml
  def update
    @post = Post.find(params[:id])

    respond_to do |format|
      if @post.update_attributes(params[:post])
        flash[:notice] = 'Post was successfully updated.'
        format.html { redirect_to(@post) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @post.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /posts/1
  # DELETE /posts/1.xml
  def destroy
    @post = Post.find(params[:id])
    @post.destroy

    respond_to do |format|
      format.html { redirect_to(posts_url) }
      format.xml  { head :ok }
    end
  end

  def person
    id_person = params[:id]
    id_person ||= session[:user_id]
    @user = User.find(id_person)
    
    @posts = Post.paginate(:page => params[:page], :conditions => { :user_id => @user.id }, :order => "created_at DESC", :per_page => 5)
    @comments = []
    Post.find(:all, :conditions => { :user_id => @user.id }).each { |post| @comments.concat post.comments }
    @comments.sort! { |a, b| a.updated_at <=> b.updated_at }
    @comments.reverse!
  end

  def search
    content = params[:posts_search][:keys]
    @results_count, results = Post.find_id_by_contents(content)
    @posts = results.map { |result| Post.find(result[:id] )}

    @comments = Comment.find(:all)
    @comments.sort! { |a, b| a.updated_at <=> b.updated_at }
    @comments.reverse!
  end

end

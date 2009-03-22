class FavoritesController < ApplicationController
  # GET /favorites
  # GET /favorites.xml
  def index
    @favorites = Favorite.find(:all)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @favorites }
    end
  end

  # GET /favorites/1
  # GET /favorites/1.xml
  def show
    @favorite = Favorite.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @favorite }
    end
  end

  def list
    favorites = Favorite.find(:all, :conditions => "host_uid == #{params[:id]}")
    users = favorites.map { |item|  User.find_by_uid(item.guest_uid) }
    users = users ? users.uniq! : []
    users = users ? users.compact! : []
    render :xml => users.to_xml
  end

  # GET /favorites/new
  # GET /favorites/new.xml
  def new
    @favorite = Favorite.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @favorite }
    end
  end

  # GET /favorites/1/edit
  def edit
    @favorite = Favorite.find(params[:id])
  end

  # POST /favorites
  # POST /favorites.xml
  def create
    hash = params[:favorite]

    name = hash.delete('guest_name')
    image_url = hash.delete('guest_image_url')
    uid = hash[:guest_uid]
    if(!User.find_by_uid(uid))
      user = User.new(:uid => uid, :name => name, :image_url => image_url)
      user.save
    end

    @favorite = Favorite.new(hash)

    respond_to do |format|
      if @favorite.save
        flash[:notice] = 'Favorite was successfully created.'
        format.html { redirect_to(@favorite) }
        format.xml  { render :xml => @favorite, :status => :created, :location => @favorite }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @favorite.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /favorites/1
  # PUT /favorites/1.xml
  def update
    @favorite = Favorite.find(params[:id])

    respond_to do |format|
      if @favorite.update_attributes(params[:favorite])
        flash[:notice] = 'Favorite was successfully updated.'
        format.html { redirect_to(@favorite) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @favorite.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /favorites/1
  # DELETE /favorites/1.xml
  def destroy
    @favorite = Favorite.find(params[:id])
    @favorite.destroy

    respond_to do |format|
      format.html { redirect_to(favorites_url) }
      format.xml  { head :ok }
    end
  end
end

require 'rexml/document'

class AdminController < ApplicationController

  before_filter :authorize

  def export
    models = []
    Dir["#{RAILS_ROOT}/app/models/*.rb"].each do |file|
      models << File.basename(file,".rb").camelize.constantize
    end

    data = "<data>"
    models.each do |model|
      data += REXML::Document.new(model.find(:all).to_xml).root.to_s
    end
    data += "</data>"

    send_data data, :filename => "data_#{Time.now.to_i}.xml"
  end

  def set_default
    Post.find(:all).each do |post|
      # post.user_id = 1
      post.privacy = Post::PRIVACIES[0]
      post.category = Post::CATEGORIES[0]
      post.save
    end

#    Comment.find(:all).each do |comment|
#      comment.user_id = 1
#      comment.save
#    end

    render :text => "Set Default Successfully!"
  end

  def import
    puts params.inspect
    file = params[:file]['file']
    puts file.inspect
    render :text => file.inspect
  end
end

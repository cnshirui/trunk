require 'rexml/document'

class DataController < ApplicationController

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

    render :xml => data
  end

  def set_default_user_id
    Post.find(:all).each do |post|
      post.user_id = 1
      post.save
    end

    Comment.find(:all).each do |comment|
      comment.user_id = 1
      comment.save
    end

    render :text => "Set Default Successfully!"
  end
end

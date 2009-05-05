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
end

require 'rexml/document'
require 'utils/time'

class AdminController < ApplicationController

  before_filter :admin

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

    send_data data, :filename => "data_#{Time.now.convert_zone(8).to_s.gsub(" ", "_")}.xml"
  end

  def import
    begin
      xml = REXML::Document.new(params[:file].read)

      # clear data
      Dir["#{RAILS_ROOT}/app/models/*.rb"].each do |file|
        model = File.basename(file,".rb").camelize.constantize
        model.find(:all).each do |obj|
          obj.destroy
        end
      end

      # import data
      xml.elements.each('data/*/*') do |e|
        model = e.to_s.match(/<.+>/).to_s[1..-2].camelize.constantize
        obj = model.new
        obj.from_xml(e.to_s)
        obj.save
      end
      
      render :text => "Import Success!"
    rescue Exception => e
      puts e.to_s, e.backtrace.join("\n")
      render :text => "Import Error!"
    end
  end
end

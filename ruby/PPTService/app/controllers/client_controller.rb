class ClientController < ApplicationController

  web_client_api  :ppt_service,
                  :soap,
                  "http://localhost:4000/ppt_service/api"
                  
  def export  
    slide_arr = []
    
    content = {:type => 'photo', :source_num => 1, :sources => ['http://static.flickr.com/1040/1355638986_3c8713ddca.jpg']}
    slide = {:name => 'TestSlidePhoto', :description => 'Photo Description', :content_num => 1, :contents => [content]}
    slide_arr << slide;
    
    content = {:type => 'photo', :source_num => 1, :sources => ['http://static.flickr.com/1040/1355638986_3c8713ddca.jpg']}
    slide = {:name => 'TestSlideVideo', :description => 'Video Description', :content_num => 1, :contents => [content]}
    slide_arr << slide;
    
    content = {:type => 'photo', :source_num => 1, :sources => ['http://static.flickr.com/1040/1355638986_3c8713ddca.jpg']}
    slide = {:name => 'TestSlideFlash', :description => 'Flash Description', :content_num => 1, :contents => [content]}
    slide_arr << slide;
     
    pres = {:title => 'TestSlideShow', :submitter => 'Tester', :slide_num => 3, :slides => slide_arr}
    
    print "------------------", Time.now
    puts pres.class
    str = ppt_service.export_ppt(pres)
    puts str
  end  
  
end

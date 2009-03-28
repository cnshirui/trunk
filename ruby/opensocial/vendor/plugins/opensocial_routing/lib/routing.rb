module OpenSocial
  module Routing
    def opensocial_people(people_controller, &block)
      @opensocial_people = people_controller
      
      self.opensocial_person '/feeds/people/:id', :controller => people_controller.to_s, :action => 'show', :format => 'opensocial', :conditions => {:method => :get}
      self.opensocial_person '/feeds/people/:id', :controller => people_controller.to_s, :action => 'update', :format => 'opensocial', :conditions => {:method => :put}
      self.opensocial_person '/feeds/people/:id', :controller => people_controller.to_s, :action => 'destroy', :format => 'opensocial', :conditions => {:method => :delete}
      
      yield self if block_given?
    end
    
    def opensocial_friends(friends_controller)
      self.opensocial_person_friends "/feeds/people/:#{@opensocial_people.to_s.singularize}_id/friends", 
                                     :controller => friends_controller.to_s, 
                                     :action => 'index', 
                                     :format => 'opensocial', 
                                     :conditions => {:method => :get}
    end
    
    def opensocial_activities(activities_controller)
       self.opensocial_person_activities "/activities/feeds/activities/user/:#{@opensocial_people.to_s.singularize}_id", 
                                         :controller => activities_controller.to_s, 
                                         :action => 'index', 
                                         :format => 'opensocial', 
                                         :conditions => {:method => :get}
                                         
      self.opensocial_person_activities_source "/activities/feeds/activities/user/:#{@opensocial_people.to_s.singularize}_id/source/:source_id",
                                        :controller => 'activities', 
                                        :action => 'index', 
                                        :format => 'opensocial', 
                                        :conditions => {:method => :get}
                                        
      self.opensocial_person_activities_source "/activities/feeds/activities/user/:#{@opensocial_people.to_s.singularize}_id/source/:source_id",
                                        :controller => 'activities', 
                                        :action => 'create', 
                                        :format => 'opensocial', 
                                        :conditions => {:method => :post}
                                        
      self.opensocial_person_activities_source "/activities/feeds/activities/user/:#{@opensocial_people.to_s.singularize}_id/source/:source_id/:id",
                                        :controller => 'activities', 
                                        :action => 'update', 
                                        :format => 'opensocial', 
                                        :conditions => {:method => :put}
                                        
      self.opensocial_person_activities_source "/activities/feeds/activities/user/:#{@opensocial_people.to_s.singularize}_id/source/:source_id/:id",
                                        :controller => 'activities', 
                                        :action => 'destroy', 
                                        :format => 'opensocial', 
                                        :conditions => {:method => :delete}
    end
  end
end
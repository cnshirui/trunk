class ReviewsController < ApplicationController 
  
    def list 
      @reviews = Review.find :all 
      render :xml => @reviews.to_xml 
    end 
    
    def create 
      @review = Review.new(params[:review]) 
      @review.save 
      render :xml => @review.to_xml 
    end 

    def delete 
      @review = Review.find(params[:id]) 
      @review.destroy 
      render :xml => @review.to_xml 
    end 

    def update 
      puts  "update-----------------------------------------------"      
      @review = Review.find(params[:id]) 
      @review.update_attributes(params[:review]) 
      render :xml => @review.to_xml 
    end 
    
end 

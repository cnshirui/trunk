require File.dirname(__FILE__) + '/../test_helper'
require 'reviews_controller'

# Re-raise errors caught by the controller.
class ReviewsController; def rescue_action(e) raise e end; end

class ReviewsControllerTest < Test::Unit::TestCase
  def setup
    @controller = ReviewsController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new
  end

  # Replace this with your real tests.
  def test_truth
    assert true
  end
end

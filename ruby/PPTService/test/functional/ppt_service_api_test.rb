require File.dirname(__FILE__) + '/../test_helper'
require 'ppt_service_controller'

class PPTServiceController; def rescue_action(e) raise e end; end

class PPTServiceControllerApiTest < Test::Unit::TestCase
  def setup
    @controller = PPTServiceController.new
    @request    = ActionController::TestRequest.new
    @response   = ActionController::TestResponse.new
  end

  def test_export_ppt
    result = invoke :export_ppt
    assert_equal nil, result
  end
end

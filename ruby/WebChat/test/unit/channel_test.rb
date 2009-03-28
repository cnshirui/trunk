require File.dirname(__FILE__) + '/../test_helper'

class ChannelTest < Test::Unit::TestCase
  fixtures :channels

  # Replace this with your real tests.
  def test_truth
    assert_kind_of Channel, channels(:first)
  end
end

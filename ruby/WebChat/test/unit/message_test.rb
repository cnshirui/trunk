require File.dirname(__FILE__) + '/../test_helper'

class MessageTest < Test::Unit::TestCase
  fixtures :messages

  # Replace this with your real tests.
  def test_truth
    assert_kind_of Message, messages(:first)
  end
end

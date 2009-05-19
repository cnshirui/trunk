require File.dirname(__FILE__) + '/../test_helper'
require 'jzip'

class JZipTest < Test::Unit::TestCase
  def setup    
    @fixtures_dir = File.join(RAILS_ROOT, "test", "fixtures")
    FileUtils.rm_f  File.join(@fixtures_dir, 'document.xml')
    FileUtils.rm_f  File.join(@fixtures_dir, 'xldoc')
  end  
  
  def teardown  
    FileUtils.rm_f  File.join(@fixtures_dir, 'document.xml')
    FileUtils.rm_f  File.join(@fixtures_dir, 'xldoc')    
  end
  
  def test_extract
#    JZip::ZipFile.open()
    JZip::unzip(File.join(@fixtures_dir, 'whohar-whatif.xlf'), @fixtures_dir)
    assert File.exists?(File.join(@fixtures_dir, 'document.xml'))
    assert File.exists?(File.join(@fixtures_dir, 'xldoc'))
  end
end
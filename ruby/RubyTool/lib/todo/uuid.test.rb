# To change this template, choose Tools | Templates
# and open the template in the editor.

require 'java'
import 'java.util.UUID'

uuid = UUID.randomUUID().toString
puts uuid
uuid.gsub!(/-/, "")
puts uuid.upcase

def get_uuid
  return UUID.randomUUID().toString.gsub(/-/, "").upcase
end

puts "rshi", get_uuid
require 'yaml'

class Person
    attr_accessor :username,:password,:email

    def initialize(username,password,email)
        @username = username
        @password = password
        @email = email
    end
end

#p = Person.new('hlxwell','123321','hlxwell@china.com')
#
#File.open('a.yml','w') {|f|
#    f.write(p.to_yaml)
#}
puts Dir.pwd

person = YAML::load(File.new("../a.yml"))

puts person.inspect
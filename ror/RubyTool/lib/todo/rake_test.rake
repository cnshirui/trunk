namespace :rshi do
  
  desc "Rake Test By RSHI"
  task :test => :environment do
    puts "rshi rake ..."
    uuids = ["7looiSajhT2C3biuKkze0u", "iOJTraFijHOws2jNsgdc0i"]
    templates = Template.find_by_uuid(uuids)
    templates.each do |t|
      puts "#{t.id}: #{t.name}"
    end
  end
end
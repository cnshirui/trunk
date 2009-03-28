content = ""
File.open("urls.htm") do |file|
  file.each do |line|
    content += line[0..-2]
  end
end

content.scan(/<a href=.+?<\/a>/) do |match|
   words = match.split(/"|<|>/)
   puts "<outline text=\"#{words[6]}\""
   puts "   title=\"#{words[6]}\" type=\"rss\""
   puts "   xmlUrl=\"#{words[2]}\" htmlUrl=\"#{words[2]}\"/>"
#   puts "#{words[6]}: #{words[2]}"
end
# <a href=.+<\/a>

=begin
<outline text="Official Google Blog"
                title="Official Google Blog" type="rss"
                xmlUrl="http://googleblog.blogspot.com/atom.xml" htmlUrl="http://googleblog.blogspot.com/"/>
=end
        </outline>
            <outline text="itnews"
               title="itnews" type="rss"
               xmlUrl="http://www.cnblogs.com/itnews/" htmlUrl="http://www.cnblogs.com/itnews/"/>
            <outline text="TechCrunch"
               title="TechCrunch" type="rss"
               xmlUrl="http://www.techcrunch.com/" htmlUrl="http://www.techcrunch.com/"/>
            <outline text="Read Write Web"
               title="Read Write Web" type="rss"
               xmlUrl="http://www.readwriteweb.com/" htmlUrl="http://www.readwriteweb.com/"/>
            <outline text="Silicon Alley Insider"
               title="Silicon Alley Insider" type="rss"
               xmlUrl="http://www.alleyinsider.com/" htmlUrl="http://www.alleyinsider.com/"/>
            <outline text="O'Reilly Radar"
               title="O'Reilly Radar" type="rss"
               xmlUrl="http://radar.oreilly.com/" htmlUrl="http://radar.oreilly.com/"/>
            <outline text="GigaOm"
               title="GigaOm" type="rss"
               xmlUrl="http://gigaom.com/" htmlUrl="http://gigaom.com/"/>
            <outline text="ValleyWag"
               title="ValleyWag" type="rss"
               xmlUrl="http://valleywag.com/" htmlUrl="http://valleywag.com/"/>
            <outline text="VentureBeat"
               title="VentureBeat" type="rss"
               xmlUrl="http://venturebeat.com/" htmlUrl="http://venturebeat.com/"/>
            <outline text="BoomTown"
               title="BoomTown" type="rss"
               xmlUrl="http://kara.allthingsd.com/" htmlUrl="http://kara.allthingsd.com/"/>
            <outline text="Bits"
               title="Bits" type="rss"
               xmlUrl="http://bits.blogs.nytimes.com/" htmlUrl="http://bits.blogs.nytimes.com/"/>
            <outline text="The Tech Beat"
               title="The Tech Beat" type="rss"
               xmlUrl="http://www.businessweek.com/the_thread/techbeat/" htmlUrl="http://www.businessweek.com/the_thread/techbeat/"/>
            <outline text="Between the Lines"
               title="Between the Lines" type="rss"
               xmlUrl="http://blogs.zdnet.com/BTL/" htmlUrl="http://blogs.zdnet.com/BTL/"/>
            <outline text="The Social"
               title="The Social" type="rss"
               xmlUrl="http://news.cnet.com/the-social/" htmlUrl="http://news.cnet.com/the-social/"/>
            <outline text="Beyond Binary"
               title="Beyond Binary" type="rss"
               xmlUrl="http://news.cnet.com/beyond-binary/" htmlUrl="http://news.cnet.com/beyond-binary/"/>
            <outline text="Ars Technica"
               title="Ars Technica" type="rss"
               xmlUrl="http://arstechnica.com/index.ars" htmlUrl="http://arstechnica.com/index.ars"/>
            <outline text="The Register"
               title="The Register" type="rss"
               xmlUrl="http://www.theregister.co.uk/" htmlUrl="http://www.theregister.co.uk/"/>
            <outline text="WebProNews"
               title="WebProNews" type="rss"
               xmlUrl="http://www.webpronews.com/" htmlUrl="http://www.webpronews.com/"/>
            <outline text="Techdirt"
               title="Techdirt" type="rss"
               xmlUrl="http://www.techdirt.com/" htmlUrl="http://www.techdirt.com/"/>

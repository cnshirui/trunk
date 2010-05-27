# ruby invoke ffmpeg

Dir["*.flv"].each do |file|
	new_file = file.sub(".flv", ".mp4")
	puts file, new_file
	# system("ffmpeg -y -i #{file} -ac 2 -ab 64k -ar 48000 -vcodec mpeg4 -s 320x240 -padtop 0 -padbottom 0 -padleft 0 -padright 0 #{new_file}")
end	

exec("ffmpeg.exe -i \"flvs\\google io wave .flv\"")
puts "*********", $?
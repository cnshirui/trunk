require 'fileutils'

$level_count = 0

def rm_empty_dir(path)

	return unless(File.directory?(path))
	
	begin
		Dir.chdir(path)
	rescue
		puts "can't chdir to #{path}"
		return
	end
	$level_count += 1
	#puts "<-#{'---'*$level_count} #{path}"


	Dir["*"].each do |dir|
		
		# recursive
		rm_empty_dir(dir)
	end

	# do delete
	#puts "#{'---'*$level_count}-> #{path}"
	if(Dir["*"].length == 0)
	  puts "FileUtils.rm_rf '#{Dir.pwd}'"
	  FileUtils.chmod 0777, Dir.pwd
	  #Dir.rmdir(Dir.pwd)
	  FileUtils.rm_rf(Dir.pwd)
	end

	Dir.chdir("..")
	$level_count -= 1
end

rm_empty_dir("d:\\perforce\\git\\trunk")
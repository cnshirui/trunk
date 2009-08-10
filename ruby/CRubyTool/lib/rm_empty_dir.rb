require 'fileutils'

$level_count = 0

def rm_empty_dir(path)

	return unless(File.directory?(path))
	Dir.chdir(path)
  $level_count += 1
	#puts "<-#{'---'*$level_count} #{path}"


	Dir["*"].each do |dir|
		
		# recursive
		rm_empty_dir(dir)
	end

	# do delete
	#puts "#{'---'*$level_count}-> #{path}"
  if(Dir["*"].length == 0)
      puts Dir.pwd
      FileUtils.chmod 0777, Dir.pwd
      Dir.rmdir(Dir.pwd)
      #FileUtils.rm_rf(Dir.pwd)
  end

  Dir.chdir("..")
  $level_count -= 1
end

rm_empty_dir("d:\\perforce\\git\\trunk")
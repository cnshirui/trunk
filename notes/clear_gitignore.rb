require 'fileutils'

dirs = []
File.read("../.gitignore").each_line do |line|
	dirs << line[0...-1]
end

dirs.each do |dir|
	Dir[dir].each do |file|
		begin
			if(File.directory?(file))
				FileUtils.rm_r(file)
			else
				File.delete(file)
			end
			puts file, "done!"
		rescue Exception => e
			puts e.message, e.backtrace.join("\n")
		end
	end
end
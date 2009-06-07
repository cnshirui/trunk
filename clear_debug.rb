require 'fileutils'

dirs = ["*/*/bin/*", "*/*/bin-debug/*", "*/*/bin-release/*", "*/*/debug/*", "cpp/*/*/debug/*"]

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
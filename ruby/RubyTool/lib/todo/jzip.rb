require 'fileutils.rb'
require 'java'

import 'java.io.FileInputStream'
import 'java.io.FileOutputStream'
import 'java.nio.channels.FileChannel'
import 'java.nio.channels.Channels'
import 'java.util.Enumeration'
import 'java.util.zip.ZipEntry'
import 'java.util.zip.ZipInputStream'
import 'java.util.zip.ZipOutputStream'


JFile = java.io.File
JZipFile = java.util.zip.ZipFile

module JZip
  
  class ZipFile

    # open a zip file
    def initialize(file_name, create = nil)
      if(File.exists?(file_name))
        @zip_file = JZipFile.new(file_name) 
      elsif(create)
        # TODO: create a zip file
      else
	raise JZipError, "File #{file_name} not found"
      end
    end
    
    def get_name
      return @zip_file.getName()
    end

    # the same as ZipFile.new
    def ZipFile.open(fileName, create = nil)
      zipfile = ZipFile.new(fileName, create)
      if block_given?
	begin
	  yield zipfile
	ensure
	  zipfile.close
	end
      else
	zipfile
      end
    end

    # add an entry to zip
    def add(entry, src_file)
    end
    
    # remove an entry from zip
    def remove(entry)
    end
    
    # replace an entry in zip
    def replace(entry_name, src_file)
      tmp_file = get_name + ".tmp"
      zis = ZipInputStream.new(FileInputStream.new(get_name))
      zos = ZipOutputStream.new(FileOutputStream.new(tmp_file))
      begin
        while((entry = zis.getNextEntry()) != nil)
          name = entry.getName()
          if(name == entry_name)
            new_entry = ZipEntry.new(entry_name)
            zos.putNextEntry(new_entry)
            zos.write(File.new(src_file).read.to_java_bytes)
          else
            new_entry = ZipEntry.new(name)
            zos.putNextEntry(new_entry)
            
            buffer = Array.new(512).to_java(:byte)
            while((bytes = zis.read(buffer, 0, buffer.length)) > 0) do
              zos.write(buffer, 0, bytes)
            end
          end
          
          zis.closeEntry()
          zos.closeEntry()
        end
      rescue Exception => e
        raise JZipError, "File repalce failed!"
        puts  e.to_s, e.backtrace.join("\n"), ""   
      ensure
        zis.close unless zis.nil?
        zos.close unless zos.nil?
      end
      
      # update @zip_file
      FileUtils.copy(tmp_file, get_name)
      @zip_file = JZipFile.new(get_name) 
      puts "#{Time.now}: Replace '#{entry_name}' in '#{get_name}' with '#{src_file}'"
    end
    
    # extract an entry
    def extract(entry_name, dest_file)
      begin
        entry = @zip_file.getEntry(entry_name)
        in_channel = Channels.newChannel(@zip_file.getInputStream(entry))
        fos = FileOutputStream.new(JFile.new(dest_file), true)
        out_channel = fos.getChannel()
        out_channel.transferFrom(in_channel, 0, entry.getSize())
      rescue Exception => e
        raise JZipError, "File extract failed!"
        puts  e.to_s, e.backtrace.join("\n"), ""    
      ensure
        in_channel.close unless in_channel.nil?
        out_channel.close unless out_channel.nil?
      end      
      
      puts "#{Time.now}: Extract '#{entry_name}' as '#{dest_file}' from '#{get_name}'"
    end
    
    # interator
    def get_entries
      entries = []
      begin
        emu = @zip_file.entries
        while(emu.hasMoreElements)
          entry = emu.nextElement
          entries << entry.getName
        end
      rescue Exception => e
        raise JZipError, "zip get entries failed!"
        puts  e.to_s, e.backtrace.join("\n"), ""    
      end   

      return entries
    end

    # close the zip file
    def close
      @zip_file.close
    end
  end  
  
  class JZipError < StandardError ; end
end

require 'rubygems'
require 'RMagick'

dir = "D:/google_corp"
Dir.chdir(dir)

array = Dir["*.jpg"].map { |name| name.length }
n_max = array.max
Dir["*.jpg"].each do |name|
  # rename files
  if(name.length < n_max)
    new_name = "0" * (n_max - name.length) + name
    File.rename(name, new_name)
    name = new_name
  end

  # rotate
  image = Magick::ImageList.new(name)
  image = image.rotate(-90)

  # TODO: detect edge blank
  # remove edge blank
  image = image.crop(25, 65, 1040, 710, true)
  image.write("_" + name)
end




#include Magick
=begin
65 25 710 1040
clown = Magick::Image.read("clown.jpg")
clown = clown[0].vignette
clown.write('new-vignette.jpg')
cropped = img.crop(x, y, width, height, true)
img = img.shave(20, 25)
=end

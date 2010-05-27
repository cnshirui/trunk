require 'rubygems'
require 'RMagick'

#include Magick


image = Magick::ImageList.new('d:/01.jpg')

width = image.columns
height = image.rows
puts width, height

# red=255, green=255, blue=255, opacity=0
pix_rows = []
height.times do |row|
  sum = 0
  width.times do |col|
    pix = image.pixel_color(row, col)
    sum += 255 * 3 - (pix.red + pix.green + pix.blue)
  end
  pix_rows << sum
end

pix_cols = []
width.times do |col|
  sum = 0
  height.times do |row|
    pix = image.pixel_color(row, col)
    sum += 255 * 3 - (pix.red + pix.green + pix.blue)
  end
  pix_cols << sum
end

# puts pix_cols.inspect, pix_rows.inspect
pix_cols.each_index { |index| puts "#{index}: " + pix_cols[index].to_i  }
pix_rows.each_index { |index| puts "#{index}: " + pix_rows[index].to_i  }

left = 0
top = 0
buttom = height
right = width
pix_cols.each do |col|
  if(col > 0)
    break
  else
    left += 1
  end
end

pix_cols.reverse_each do |col|
  if(col > 0)
    break
  else
    right -= 1
  end
end

pix_rows.each do |row|
  if(row > 0)
    break
  else
    top += 1
  end
end

pix_rows.reverse_each do |row|
  if(row > 0)
    break
  else
    buttom -= 1
  end
end

cropped = image.crop(left, top, right - left, buttom - top, true)
cropped.write("d:/02.jpg")

#           870, 640
# 134, 83, 1020, 720, 1171, 824
puts left, top, right, buttom, width, height
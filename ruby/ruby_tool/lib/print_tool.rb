# puts a width-equal line, filling spaces with "..."
def print_line line
  line_width = 100
  len = line.length
  add = len>line_width ? "" : "."*(line_width-len)
  print "#{line}#{add}"
end

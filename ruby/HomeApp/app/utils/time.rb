require 'date'

class Time
  def to_datetime
    # Convert seconds + microseconds into a fractional number of seconds
    seconds = sec + Rational(usec, 10**6)

    # Convert a UTC offset measured in minutes to one measured in a
    # fraction of a day.
    offset = Rational(utc_offset, 60 * 60 * 24)
#    puts "----------------", year, month, day, hour, min, seconds, offset, "------------------"
    begin
      return DateTime.new(year, month, day, hour, min, seconds, offset)
    rescue Exception => e
      puts "------------------------------------------------------------"
      puts "DateTime.new(#{year}, #{month}, #{day}, #{hour}, #{min}, #{seconds}, #{offset})"
      puts e.message
      puts e.backtrace
      puts "------------------------------------------------------------"
#      puts e.backspace.join("\n")

      return DateTime.new
    end
  end

  def convert_zone(offset)
    local = self.to_datetime
    local.new_offset(Rational(offset, 24))
  end

=begin
  def convert_zone(to_zone)
    original_zone = ENV["TZ"]
    utc_time = dup.gmtime
    ENV["TZ"] = to_zone
    to_zone_time = utc_time.localtime
    ENV["TZ"] = original_zone
    return to_zone_time
  end
=end
end

require_relative "oracle"

if ARGV.length < 1
    puts "Usage: ruby sample.rb <filename>"
    exit
end

f = File.new(ARGV[0])
data = f.read
f.close

ctext = []
(0..data.length).step(2).each { |val|
    ctext.push(data[val,2].to_i(16))
}

Oracle_Connect()

rc = Oracle_Send(ctext, 3)
puts "Oracle returned: %d" % rc

Oracle_Disconnect()
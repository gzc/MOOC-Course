require_relative "oracle"

if ARGV.length < 1
    puts "Usage: ruby sample.rb <filename>"
    exit
end

f = File.new(ARGV[0])
data = f.read
f.close

message = []
(0..data.length-1).each do |val|
    message.push(data[val])
end

Oracle_Connect()

tag = Mac(message, message.length)

ret = Vrfy(message, message.length, tag)

if (ret==1)
    puts "Message verified successfully!"
else
    puts "Message verification failed."
end

Oracle_Disconnect()

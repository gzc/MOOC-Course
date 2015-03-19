require 'socket'

$s = nil

def Oracle_Connect
    hostname = '54.165.60.84'
    port = 80

    $s = TCPSocket.open(hostname,port)
    puts "Connected to server successfully."

    return 0
end

def Oracle_Disconnect
    if !$s
        puts "[WARNING]: You haven't connected to the server yet."
        return -1
    end

    $s.close
    puts "Connection closed succesfully."

    return 0
end

# Packet Structure: < num_blocks(1) || ctext(16*num_blocks) || null-terminator(1) >
def Oracle_Send (ctext, num_blocks)
    if !$s
        puts "[WARNING]: You haven't connected to the server yet."
        return -1
    end

    msg = [num_blocks.chr]
    ctext.each do |val| msg.push(val.chr) end
    msg.push(0.chr)

    $s.write(msg.join(""))
    return $s.read(2).to_i
end

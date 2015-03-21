require 'socket'

$mac_sock = nil
$vrfy_sock = nil

def Oracle_Connect
    hostname = '54.165.60.84'
    mac_port = 81
    vrfy_port = 82

    $mac_sock = TCPSocket.open(hostname,mac_port)
    $vrfy_sock = TCPSocket.open(hostname,vrfy_port)

    puts "Connected to server successfully."

    return 0
end

def Oracle_Disconnect
    if !$mac_sock or !$vrfy_sock
        puts "[WARNING]: You haven't connected to the server yet."
        return -1
    end

    $mac_sock.close
    $vrfy_sock.close

    puts "Connection closed succesfully."

    return 0
end

# Packet Structure: < mlength(1) || message(mlength) || null-terminator(1) >
def Mac(message, mlength)
    if !$mac_sock or !$vrfy_sock
        puts "[WARNING]: You haven't connected to the server yet."
        return -1
    end

    out = [mlength.chr]
    message.each do |val| out.push(val.chr) end
    out.push(0.chr)

    $mac_sock.write(out.join(""))
    tag =  $mac_sock.read(16)

    return tag
end

# Packet Structure: < mlength(1) || message(mlength) || tag(16) || null-terminator(1) >
def Vrfy(message, mlength, tag)
    if !$mac_sock or !$vrfy_sock
        puts "[WARNING]: You haven't connected to the server yet."
        return -1
    end

    out = [mlength.chr]
    message.each do |val| out.push(val.chr) end
    (0..tag.length-1).each do |val| out.push(tag[val].chr) end
    out.push(0.chr)

    $vrfy_sock.write(out.join(""))
    match =  $vrfy_sock.read(2)

    return match.to_i
end

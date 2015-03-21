mac_sock = None
vrfy_sock = None

def Oracle_Connect():
    import socket
    global mac_sock
    global vrfy_sock
    mac_sock, vrfy_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM), socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        mac_sock.connect(('54.165.60.84', 81))
        vrfy_sock.connect(('54.165.60.84', 82))
    except socket.error as e:
        print e
        return -1

    print "Connected to server successfully."

    return 0

def Oracle_Disconnect():
    if not mac_sock or not vrfy_sock:
        print "[WARNING]: You haven't connected to the server yet."
        return -1

    mac_sock.close()
    vrfy_sock.close()

    print "Connection closed successfully."

    return 0

# Packet Structure: < mlength(1) || message(mlength) || null-terminator(1) >
def Mac(message, mlength):
    if not mac_sock or not vrfy_sock:
        print "[WARNING]: You haven't connected to the server yet."
        return -1

    out = bytearray(message[:])
    out.insert(0, mlength)
    out.append(0)

    mac_sock.send(bytearray(out))
    tag = mac_sock.recv(16)

    return bytearray(tag)

# Packet Structure: < mlength(1) || message(mlength) || tag(16) || null-terminator(1) >
def Vrfy(message, mlength, tag):
    if not mac_sock or not vrfy_sock:
        print "[WARNING]: You haven't conected to the server yet."
        return -1

    out = bytearray(message[:])
    out.insert(0, mlength)
    out += tag
    out.append(0)

    vrfy_sock.send(bytearray(out))
    match = vrfy_sock.recv(2)

    return int(match.strip('\0'))

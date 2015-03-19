s = None

def Oracle_Connect():
    import socket
    global s
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        s.connect(('54.165.60.84', 80))
    except socket.error as e:
        print e
        return -1

    print "Connected to server successfully."

    return 0

def Oracle_Disconnect():
    if not s:
        print "[WARNING]: You haven't connected to the server yet."
        return -1

    s.close()
    print "Connection closed successfully."

    return 0

# Packet Structure: < num_blocks(1) || ciphertext(16*num_blocks) || null-terminator(1) >
def Oracle_Send(ctext, num_blocks):
    if not s:
        print "[WARNING]: You haven't connected to the server yet."
        return -1

    msg = ctext[:]
    msg.insert(0, num_blocks)
    msg.append(0)

    s.send(bytearray(msg))
    recvbit = s.recv(2)

    try:
        return int(recvbit)
    except ValueError as e:
        return int(recvbit[0])

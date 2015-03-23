sign_sock = None
vrfy_sock = None

MAX_PACKET_LEN = 8192
NOT_BINARY_STR_ERR = -1
MISSING_DELIMITER_ERR = -2
ORIGINAL_MSG_ERR = -3

def Oracle_Connect():
    import socket
    global sign_sock
    global vrfy_sock
    sign_sock, vrfy_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM), socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        sign_sock.connect(('54.165.60.84', 8080))
        vrfy_sock.connect(('54.165.60.84', 8081))
    except socket.error as e:
        print e
        return -1

    print "Connected to server successfully."

    return 0

def Oracle_Disconnect():
    if not sign_sock or not vrfy_sock:
        print "[WARNING]: You haven't connected to the server yet."
        return -1

    sign_sock.close()
    vrfy_sock.close()

    print "Connection closed successfully."

    return 0

# Packet Structure: < message >
# Message may be either a long integer, or a binary string
def Sign(msg):
    if not sign_sock or not vrfy_sock:
        print "[WARNING]: You haven't connected to the server yet."
        return -1
    if msg < 0:
        print "[ERROR]: Message cannot be negative!"

    if type(msg) is long or type(msg) is int:
        msg = bin(msg)[2:]

    pkt = msg + "X"
    sign_sock.send(pkt)

    resp = sign_sock.recv(MAX_PACKET_LEN)
    try:
        sigma = int(resp, 2)
    except ValueError as e:
        sigma = int(resp)

    if sigma == NOT_BINARY_STR_ERR:
        print "[ERROR]: Message was not a valid binary string."
    if sigma == ORIGINAL_MSG_ERR:
        print "[ERROR]: You cannot request a signature on the original messgae!"

    return sigma

# Packet Structure: < message | ":" | signature >
# Message and signature may be either long integers, or binary strings
def Verify(msg, sigma):
    if not sign_sock or not vrfy_sock:
        print "[WARNING]: You haven't conected to the server yet."
        return -1
    if msg < 0 or sigma < 0:
        print "[ERROR]: Message and signature cannot be negative!"
        return -1

    if type(msg) is long or type(msg) is int:
        msg = bin(msg)[2:]
    if type(sigma) is long or type(sigma) is int:
        sigma = bin(sigma)[2:]

    pkt = msg + ":" + sigma + "X"
    vrfy_sock.send(pkt)

    match = int(vrfy_sock.recv(MAX_PACKET_LEN))

    if match == NOT_BINARY_STR_ERR:
        print "[ERROR]: Message and/or signature were not valid binary strings."
    elif match == MISSING_DELIMITER_ERR:
        print "[ERROR]: Missing delimiter between message and signature."
    return match

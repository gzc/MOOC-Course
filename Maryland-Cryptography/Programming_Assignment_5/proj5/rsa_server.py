#!/usr/bin/env python
import SocketServer, sys, os
from threading import Thread
from Crypto.PublicKey import RSA

###############################################################################
### Globals
###############################################################################
# Errors
NOT_BINARY_STR_ERR = -1
MISSING_DELIMITER_ERR = -2
ORIGINAL_MSG_ERR = -3

# Message length caps
MAX_PACKET_LEN = 8192
MAX_MSG_LEN = 504
MAX_SIGMA_LEN = 1024

# RSA key (decimal)
N = 119077393994976313358209514872004186781083638474007212865571534799455802984783764695504518716476645854434703350542987348935664430222174597252144205891641172082602942313168180100366024600206994820541840725743590501646516068078269875871068596540116450747659687492528762004294694507524718065820838211568885027869

# the private key has been removed from this code
# d =

# public exponent (decimal)
e = 65537L

# Transforms m => (0..0m | 0..0m)
def transform(m):
    return bin(m)[2:].zfill(MAX_MSG_LEN + 8) + bin(m)[2:].zfill(MAX_MSG_LEN + 8)

# The integer below is the target message, converted to a decimal integer
ORIGINAL_MSG = transform(13798807383143328309401189395709069089466800774722248521458880654426000260215331913286146944721403272904836660348328334961162734996594917065253335033198)
###############################################################################



###############################################################################
### Handles message signature requests for RSASigningServer
###############################################################################
class SignService(SocketServer.BaseRequestHandler):
    def sign(self, m):
        return pow(int(transform(m),2), d, N)

    def recvall(self):
        data = ''
        while not data[-1:] is 'X':
            packet = self.request.recv(MAX_PACKET_LEN)
            if not packet:
                return None
            data += packet
        return data[:-1]

    def handle(self):
        data = ' '
        print "Signing server recieved connection from", self.client_address

        while len(data):
            data = self.recvall()
            if data is None:
                return

            # Accept only the first MAX_MSG_LEN "bits"
            try:
                msg = int(data[:MAX_MSG_LEN], 2)
            except ValueError as e:
                self.request.send(str(NOT_BINARY_STR_ERR))
                continue

            if transform(msg) == ORIGINAL_MSG:
                self.request.send(str(ORIGINAL_MSG_ERR))
                continue

            # Create signature on 0m|0m
            sigma = self.sign(msg)
            # Respond with signature
            self.request.send(bin(sigma)[2:])

        print self.client_address, "exited"

        self.request.close()
###############################################################################



###############################################################################
### Handles signature verification requests for RSAVerifyServer
###############################################################################
class VerifyService(SocketServer.BaseRequestHandler):
    def verify(self, m, sigma):
        return (int(transform(m),2) == pow(sigma, e, N))

    def recvall(self):
        data = ''
        while not data[-1:] is 'X':
            packet = self.request.recv(MAX_PACKET_LEN)
            if not packet:
                return None
            data += packet
        return data[:-1]

    def handle(self):
        data = ' '
        print "Verify server recieved connection from", self.client_address

        while len(data):
            data = self.recvall()
            if data is None:
                return

            # Parse input
            try:
                msg, sigma = data.split(":")
            except ValueError as e:
                self.request.send(str(MISSING_DELIMITER_ERR))
                continue
            # Accept only the first MAX_MSG_LEN "bits" of msg
            # and the first MAX_SIGMA_LEN "bits" of signature
            try:
                msg = int(msg[:MAX_MSG_LEN], 2)
                sigma = int(sigma[:MAX_SIGMA_LEN], 2)
            except ValueError as e:
                self.request.send(str(NOT_BINARY_STR_ERR))
                continue

            # Check if this is a valid signature for 0m|0m
            match = self.verify(msg, sigma)
            # Respond with 1 for true, 0 for false
            self.request.send(bin(match)[2:])

        print self.client_address, "exited"
        self.request.close()
###############################################################################

###############################################################################
### Multithreaded signing server, requests handled by SignService
###############################################################################
class RSASigningServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    pass
###############################################################################


###############################################################################
### Multithreaded verify server, requests handled by VerifyService
###############################################################################
class RSAVerifyServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    pass
###############################################################################


###############################################################################
### Main: fork, run signing server in child and verify server in parent
###############################################################################
def main():
    if len(sys.argv) < 3:
        print "Usage: python rsa_sign_server.py SIGN_PORT VERIFY_PORT"
        sys.exit(-1)

    pid = os.fork()

    if pid == 0: # child
        print "Child process starting signing server on port", sys.argv[1]
        rsa_signing_server = RSASigningServer(('',int(sys.argv[1])), SignService)
        rsa_signing_server.serve_forever()
    else: # parent
        print "Parent process starting verify server on port", sys.argv[2]
        rsa_verify_server = RSAVerifyServer(('', int(sys.argv[2])), VerifyService)
        rsa_verify_server.serve_forever()
###############################################################################

if __name__ == "__main__":
    main()

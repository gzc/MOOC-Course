#!/usr/bin/env python
# coding=utf-8

from Crypto.Cipher import AES
import Crypto.Util.Counter


cipher_hex1 = "4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81"

cipher_hex2 = "5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253"

cipher_hex3 = "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329"

cipher_hex4 = "770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451"

cbckey_hex = "140b41b22a29beb4061bda66b6747e14"
ctrkey_hex = "36f18357be4dbd77f050515c73fcf9f2"

# Converts the hex strings into character strings
cipher1 = cipher_hex1.decode('hex')
cipher2 = cipher_hex2.decode('hex')
cipher3 = cipher_hex3.decode('hex')
cipher4 = cipher_hex4.decode('hex')

cbckey = cbckey_hex.decode('hex')
ctrkey = ctrkey_hex.decode('hex')

# Sets IV as the first 16 bytes of the ciphertext
IV1 = cipher1[0:16]
IV2 = cipher2[0:16]
IV3 = cipher3[0:16]
IV4 = cipher4[0:16]

print AES.new(cbckey, AES.MODE_CBC, IV1).decrypt(cipher1)
print AES.new(cbckey, AES.MODE_CBC, IV2).decrypt(cipher2)
ctr1 = Crypto.Util.Counter.new(128, initial_value=long(IV3.encode("hex"), 16)-1)
ctr2 = Crypto.Util.Counter.new(128, initial_value=long(IV4.encode("hex"), 16)-1)
print AES.new(ctrkey, AES.MODE_CTR, counter=ctr1).decrypt(cipher3)
print AES.new(ctrkey, AES.MODE_CTR, counter=ctr2).decrypt(cipher4)




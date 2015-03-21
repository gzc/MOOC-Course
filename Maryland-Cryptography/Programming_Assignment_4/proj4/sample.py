from oracle import *
import sys

#if len(sys.argv) < 2:
#    print "Usage: python sample.py <filename>"
#    sys.exit(-1)

#f = open(sys.argv[1])
#data = f.read()
#f.close()

m1 = "I, the server, h"
m2 = "ereby agree that"
m3 = " I will pay $100"
m4 = " to this student"

Oracle_Connect()

data1 = m1+m2;
tag1 = Mac(data1, len(data1))

data2 = ""
for i in range(16):
	data2 += chr (tag1[i] ^ ord(m3[i]))

data2 = data2 + m4
tag = Mac(data2, len(data2))

data = m1+m2+m3+m4

ret = Vrfy(data, len(data), tag)
print 
print ret
if (ret==1):
    print "Message verified successfully!"
else:
    print "Message verification failed."

print tag
for c in tag:
	print hex(c)

Oracle_Disconnect()

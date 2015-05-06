#!/usr/bin/env python
# coding=utf-8

from Crypto.Hash import SHA256
import Crypto.Util.Counter

blocks=[]
h = ""

file_object = open('3.mp4', 'rb')
while True:
	chunk = file_object.read(1024)
	if not chunk:
		break
	blocks.append(chunk)
	
blocks = list(reversed(blocks))

for block in blocks:
	block = block + h
	sha = SHA256.new()
	sha.update(block)
	h = sha.hexdigest()
	h = h.decode('hex')
	
print h.encode('hex')




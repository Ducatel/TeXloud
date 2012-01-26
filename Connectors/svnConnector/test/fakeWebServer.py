#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 12 janv. 2012

@author: meva
'''
import socket

sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(('127.0.0.1', 6667))
sock.listen(5)

while 1:
    client, addr=sock.accept()
    
    taille=1
    completeMessage=""
    
    while taille>0:
        message=client.recv(1024)
        completeMessage+=message
        taille=len(message)
    
    print 'plop'
        
    f = open('/tmp/testFile', 'w')
    f.write(completeMessage)
    f.close()
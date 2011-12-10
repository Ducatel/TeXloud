#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 10 dec. 2011

@author: Davis Ducatel
'''
import socket
from re import match
import threading
import time

class Frontale(object):
    '''
    classdocs
    '''


    def __init__(self,adresse,port):
        '''
        Constructor
        '''
        regexAdresse="^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$"
        regexPort="^[0-9]{1,5}$"
        if match(regexAdresse,adresse) and match(regexPort,str(port)) :
            if isinstance(port, int):
                self._adresse = adresse
                self._port = port
            else:
                raise ValueError
        else:
            raise ValueError
        
        self._sock=socket.socket()
        self._sock.bind((self._adresse, self._port))
        self._sock.listen(5)
        
        
    def runServer(self):
        thread = threading.Thread(target=self.main_loop)
        thread.setDaemon(True) #Commentez cette ligne pour voir la différence
        thread.start()
        time.sleep(0.5)
        
    def main_loop(self):
        """Accepte les connexions et appelle pour chacune d'elle handler dans un nouveau thread"""
        while 1:
            client, addr=self._sock.accept()
            print ("Le client {0} se connecte au serveur".format(addr[0]))
            threading.Thread(target=self.handler,args=(client, addr)).start()

    def handler(self,client, addr):
        """Fonction chargée de prendre en main un client"""
        while 1:
            message=client.recv(1024)
            if not message: #arrive si la connexion est coupée
                break
            client.send(message[::-1])
        print ("Le client {0} est deconnecte du serveur".format(addr[0]))

#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 10 dec. 2011

@author: Davis Ducatel
'''
import socket
from re import match
import threading
import json

class FrontalServeur(object):
    '''
    Classe qui gere le serveur de la frontale de compilation grace à
    Son adresse IP
    Son port d'ecoute
    Son socket    
    '''


    def __init__(self,adresse,port):
        '''
        Constructeur du serveur de la frontale
        @param adresse: Adresse IP du serveur
        @param port: Numéro du port d'ecoute
        @raise ValueError: Declencher si le port ou l'adresse est incorrect
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
        
    def lanceServeur(self):
        """
        Methode qui lance la socket en ecoute
        Puis qui accepte les connexions et appelle pour chacune d'elle la methode voulu dans un nouveau thread
        """
        self._sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._sock.bind((self._adresse, self._port))
        self._sock.listen(5)
        
        while 1:
            client, addr=self._sock.accept()
            threading.Thread(target=self.gestionTravail,args=(client,addr)).start()
        

    def gestionTravail(self,client,addr):
        """
        Methode qui va recupere la demande du serveur web
        et la traiter
        """
        while 1:
            message=client.recv(1024)
            msg="{0}\nenvoyé depuis serveur {1}".format(json.loads(message.decode()),addr)
            print (msg)
            if not message: #arrive si la connexion est coupée
                return None
            client.send(message[::-1])
         

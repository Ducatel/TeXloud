#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 10 dec. 2011

@author: David Ducatel
'''
from re import match
import socket
import json

class FrontalClientData(object):
    '''
    class qui gere le client (socket) qui va ce connecté aux serveurs de données grace a
    Son adresse IP
    Son numero de port d'ecoute
    Son socket
    '''

    def __init__(self,adresse,port):
        '''
        Constructeur du client qui va communiqué avec le frontale des donnees
        @param adresse: Adresse IP du serveur distant
        @param port: Numéro du port d'ecoute distant
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
        
    
    def getData(self,nomProjet):
        """
        Methode qui permet de recupéré les données demandé
        @param nomProjet: nom du projet latex a recupere
        @return une archive zip contenant toutes les données utile a la compilation
        @return le nom du fichier maitre (celui sur lequelle on lance la compialtion) 
        """
        self._sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._sock.connect((self._adresse, self._port))
        self._sock.send(nomProjet)
        
        
        # recuperation de l'archive des fichiers
        taille=1
        archive=""
        while taille>0:
            msg=self._sock.recv(1024)
            archive+=msg
            taille=len(msg)
                
        # envoi d'un accusé de reception
        message={"message":"recv ok"}
        messageJson=json.dumps(message)
        messageJson=messageJson.encode()
        self._sock.send(messageJson)
        
        
        # recuperation du nom du fichier maitre
        taille=1
        fichierMaitre=""
        while taille>0:
            msg=self._sock.recv(1024)
            fichierMaitre+=msg
            taille=len(msg)       
        
        #TODO pour test
        print ("receiption du fichier: {0}\nle fichier maitre est : {1}".format(archive,fichierMaitre))
        
        return archive,fichierMaitre
    
    def __del__(self):
        self._sock.close()
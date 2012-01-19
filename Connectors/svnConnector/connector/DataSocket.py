#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 12 janv. 2012

@author: meva
'''
from abc import abstractmethod
import socket
import threading
import json

class DataSocket(object):
    '''
    Socket d'écoute permettant les traitements sur les données
    '''

    def __init__(self, address, port):
        self._address = address
        self._port = port
        self._messageSeparator = '+==\sep==+'
        
    def launch(self):
        self._sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._sock.bind((self._address, self._port))
        self._sock.listen(5)
        
        print 'serveur lancé sur ' + self._address + ':' + str(self._port)
        
        while 1:
            client, addr = self._sock.accept()
            threading.Thread(target=self.handleRequest,args=(client,addr)).start()
            
    def handleRequest(self, client, addr):
        taille=1
        completeMessage=""
        
        while taille>0:
            message=client.recv(1024)
            message=message.decode()
            completeMessage+=message
            taille=len(message)
      
        client.close()
        
        messageArray = completeMessage.split(self._messageSeparator)
        
        #récupère les informations sur la requete
        requestInfo = json.loads(messageArray[0])
        label = requestInfo['label']
        '''
        if(label=='create'):
        elif:
        elif:
        elif:
        '''
        print completeMessage
        #print client + ' ' + addr;
            
    @abstractmethod
    def getProject(self, path):
        return NotImplemented
        
    @abstractmethod
    def createProject(self, projectName, username):
        return NotImplemented
    
    @abstractmethod
    def compileProject(self, rootFile, path, compilerServerIp, compilerServerPort):
        return NotImplemented
    
    @abstractmethod
    def getFile(self, path):
        return NotImplemented
    
    @abstractmethod
    def deleteFile(self, path):
        return NotImplemented
    
    @abstractmethod
    def deleteProject(self, path):
        return NotImplemented
    
    @abstractmethod
    def sync(self):
        return NotImplemented
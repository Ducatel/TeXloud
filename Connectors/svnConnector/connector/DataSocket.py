'''
Created on 12 janv. 2012

@author: meva
'''
from abc import abstractmethod
import socket
import threading

class DataSocket(object):
    '''
    Socket d'écoute permettant les traitements sur les données
    '''

    def __init__(self):
        self._sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._sock.bind((self._adresse, self._port))
        self._sock.listen(5)
        
        while 1:
            client, addr=self._sock.accept()
            threading.Thread(target=self.gestionTravail,args=(client,addr)).start()
            
    @abstractmethod
    def getProject(self, path):
        return NotImplemented
        
    @abstractmethod
    def createProject(self, projectName, username, projectName):
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
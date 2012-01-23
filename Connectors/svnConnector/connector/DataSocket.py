#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 12 janv. 2012

@author: meva
'''
from abc import abstractmethod
import json
import os
import socket
import threading
import re

class DataSocket(object):
    '''
    Socket d'écoute permettant les traitements sur les données
    '''

    def __init__(self, address, port):
        self._address = address
        self._port = port
        self._messageSeparator = '+==\sep==+'
        self._trameEnd = '+==\endTrame==+'
        self._webserver_ip = '127.0.0.1'
        self._webserver_port = 6666
        self.wc_dir = '/tmp/'
        
    def sendCompilationFile(self, fileInfos, clientAddr, clientPort, path):
        ''' Envoi d'un fichier binaire à un client '''
        if not '..' in path:
            f = open(self.wc_dir + path, 'rb')
            data = f.read()
            f.close()
            
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.connect((clientAddr, clientPort))
    
            sock.sendall(fileInfos)
            sock.send(self._messageSeparator)
            sock.sendall(data)
            
            sock.close()
        
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
            
            completeMessage+=message
            taille=len(message)
            
            if(completeMessage.endswith(self._trameEnd)):
                completeMessage = self.cleanRequest(completeMessage)
                messageArray = completeMessage.split(self._messageSeparator)
                
                #récupère les informations sur la requete
                requestInfo = json.loads(messageArray[0])
                
                print 'chaine json -> ' + str(requestInfo)
                
                label = requestInfo['label']
        
                if(label=='create'):
                    print 'create'
                    self.createProject(requestInfo['projectName'], requestInfo['username'], client)
                elif(label=='getProject'):
                    print 'getProject'
                    self.getProject(requestInfo['path'], client)
                elif(label=='compile'):
                    print 'compile'
                    self.compileProject(requestInfo['rootFile'], requestInfo['path'], 
                                        requestInfo['compilerServerIp'], requestInfo['compilerServerPort'], client)
                elif(label=='getFile'):
                    print 'getFile'
                    self.getFile(requestInfo['path'], requestInfo['filename'], client)
                elif(label=='deleteFile'):
                    print 'deleteFile'
                    self.deleteFile(requestInfo['path'], requestInfo['filename'], client)
                elif(label=='createFile'):
                    print 'createFile'
                    self.createFile(requestInfo['path'], requestInfo['filename'], messageArray[1], client)
                elif(label=='deleteProject'):
                    print 'deleteProject'
                    self.deleteProject(requestInfo['path'], client)
                elif(label=='sync'):
                    print 'sync -> ' + str(requestInfo['files'])
                    self.sync(requestInfo['path'], requestInfo['currentFile'], requestInfo['files'], client)
                else:
                    print 'failed to retrieve action'
                    
                completeMessage = ""
      
    def sync(self, path, currentFile, files, client):
        if not '..' in path:
            for fdata in files:
                print 'fdata -> ' + str(fdata)
                if not '..' in fdata['filename']:
                    f = open(self.wc_dir + path + '/' + fdata['filename'], 'w')
                    f.write(fdata['content'])
                    f.close()
        #client.close()
            
    def cleanRequest(self, request):
        if(request.endswith(self._trameEnd)):
            return request[:-len(self._trameEnd)]
        
        return request
            
    @abstractmethod
    def getProject(self, path, client):
        return NotImplemented
        
    @abstractmethod
    def createProject(self, projectName, username, client):
        return NotImplemented
    
    @abstractmethod
    def compileProject(self, rootFile, path, compilerServerIp, compilerServerPort, client):
        return NotImplemented
    
    def getFile(self, path, filename, client):
        if not '..' in path and not '..' in filename:
            f = open(path + '/' + filename, 'rb')
            data = f.read()
            f.close()
            
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.connect((self._webserver_ip, self._webserver_port))
            sock.send(data)
            sock.close()
    
    @abstractmethod
    def deleteFile(self, path, filename, client):
        if not '..' in path and not '..' in filename:
            os.system('rm -rf ' + self.wc_dir + path)
    
    @abstractmethod
    def deleteProject(self, path, client):
        if not '..' in path:
            os.system('rm -rf ' + self.wc_dir + path)
    
    ''' Crée un fichier donc le contenu est passé dans file_content (prend les fichiers binaires) '''
    def createFile(self, path, filename, file_content, client):
        if not '..' in path and not '..' in filename:
            f = open(self.wc_dir + path + '/' + filename, 'w')
            f.write(file_content)
            f.close()

    ''' Connexion à la socket serveur pour le test
        getProject: sock.send('{"label":"getProject","path":"53d4ad39451eac91f7a983fd2d4ab34c"}')
        deleteProject: sock.send('{"label":"deleteProject","path":"53d4ad39451eac91f7a983fd2d4ab34c"}')
        sync: sock.send('{"label" : "sync", "path" : "53d4ad39451eac91f7a983fd2d4ab34c", "files" : [{"filename" : "bouh/bouhbouh.txt", "content" : "blahblah commit"}], "currentFile" : "rien"}')
       
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(('127.0.0.1', 31346))
        sock.send('{"label" : "sync", "path" : "53d4ad39451eac91f7a983fd2d4ab34c", "files" : [["pdfgsbvslop", "contenu"],["hdufk","gfyig"]], "currentFile" : "rien"}')
        sock.send('+==\endTrame==+')
        sock.close()
    '''
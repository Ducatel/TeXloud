#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 12 janv. 2012

@author: meva
'''
from abc import abstractmethod
from connector.DataSocket import DataSocket
import SvnConnector
import os

class SvnDataSocket(DataSocket):
    '''
    Socket d'écoute permettant les traitements sur les données des dépots svn
    '''

    def __init__(self, port, address):
        super(self.__class__,self).__init__(port, address)
        
    def cleanRequest(self, request):
        if(request.endswith(self._trameEnd)):
            return request[:-len(self._trameEnd)]
        
        return request
            
    def getProject(self, path, client):
        if not '..' in path:
            connection = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'meva', 'plop', path)
        
    def deleteProject(self, path, client):
        super(SvnDataSocket, self).deleteProject(path, client)
        print path + ' deleted'
        
    @abstractmethod
    def createProject(self, projectName, username, client):
        return NotImplemented
    
    @abstractmethod
    def compileProject(self, rootFile, path, compilerServerIp, compilerServerPort, client):
        return NotImplemented
   
    def deleteFile(self, path, filename, client):
        if not '..' in path and not '..' in filename:
            super(SvnDataSocket, self).deleteProject(path, client)
            
            connection = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'meva', 'plop', path, False)
            connection.remove_file(filename)
            connection.commit()
            print path + '/' + filename + ' deleted'
            
    def createFile(self, path, filename, file_content, client):
        super(SvnDataSocket, self).createFile(path, filename, file_content, client)
        connection = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'meva', 'plop', path, False)
        connection.full_commit()
        print 'file commited'
    
    def sync(self, path, currentFile, files, client):
        super(SvnDataSocket, self).sync(path, currentFile, files, client)
        
        if not '..' in path:
            connection = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'meva', 'plop', path, False)
            connection.full_commit()
            print 'files commited'
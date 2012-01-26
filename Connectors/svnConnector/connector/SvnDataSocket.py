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
            
    def getProject(self, path, username, password, client):
        super(SvnDataSocket, self).getProject(path, username, password, client)
        
        if not '..' in path:
            print path
            # connection = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'meva', 'plop', path)
            connection = SvnConnector.SvnConnector(path, username, password)
            self.save_user_conf(connection.get_public_dir_name(), username, password, connection.get_url())
        
    def deleteProject(self, path, client):
        super(SvnDataSocket, self).deleteProject(path, client)
        print path + ' deleted'
        
    #@abstractmethod
    #def createProject(self, projectName, username, client):
    #    return NotImplemented
    
    @abstractmethod
    def compileProject(self, rootFile, path, compilerServerIp, compilerServerPort, client):
        return NotImplemented
   
    def deleteFile(self, path, filename, client):
        if not '..' in path and not '..' in filename:
            super(SvnDataSocket, self).deleteFile(path, filename, client)
            
            connection = SvnConnector.SvnConnector(self.getUserProperty(path, 'repo'), self.getUserProperty(path, 'username'), 
                                                   self.getUserProperty(path, 'password'), path, False)
            connection.remove_file(filename)
            connection.commit()
            print path + '/' + filename + ' deleted'
            
    def createFile(self, path, filename, file_content, client):
        super(SvnDataSocket, self).createFile(path, filename, file_content, client)
        connection = SvnConnector.SvnConnector(self.getUserProperty(path, 'repo'), self.getUserProperty(path, 'username'), 
                                                   self.getUserProperty(path, 'password'), path, False)
        print 'commit to ' + self.getUserProperty(path, 'repo') + ', user -> ' + self.getUserProperty(path, 'username') + ', pwd -> ' + self.getUserProperty(path, 'password')
        connection.full_commit()
        print 'file commited'
    
    def sync(self, path, currentFile, files, client):
        super(SvnDataSocket, self).sync(path, currentFile, files, client)
        
        if not '..' in path:
            connection = SvnConnector.SvnConnector(self.getUserProperty(path, 'repo'), self.getUserProperty(path, 'username'), 
                                                   self.getUserProperty(path, 'password'), path, False)
            connection.full_commit()
            print 'files commited'
# -*- coding: UTF-8 -*-
'''
Created on 18 déc. 2011

@author: meva
'''
from connector.GenericConnector import GenericConnector

import os
import pysvn
import datetime
import md5
from numpy.f2py.auxfuncs import throw_error

class SvnConnector(GenericConnector):
    '''
    Connecteur à un serveur svn 
    '''   
    def __init__(self, url, login = '', passwd = '', wc_path = '', createWorkingCopy = True):
        super(self.__class__,self).__init__(url, login, passwd)
        
        now = datetime.datetime.now()
        
        dir_name = md5.new(str(now)).hexdigest()
        #self.wc_dir += dir_name
        
        self.client = pysvn.Client()
        
        self.client.callback_get_login = lambda x, y, z: (True, self.login, self.passwd, False)
        
        #self.client.set_default_password(self.login)
        #self.client.set_default_username(self.passwd)
        
        if(wc_path):
            self.wc_dir += wc_path
        else:
            self.wc_dir += dir_name
            
        self.client.checkout(self.url, self.wc_dir)

    def get_wc_dir(self):
        return self.__wc_dir


    def set_wc_dir(self, value):
        self.__wc_dir = value


    def del_wc_dir(self):
        del self.__wc_dir



    def get_client(self):
        return self.__client


    def set_client(self, value):
        self.__client = value


    def del_client(self):
        del self.__client

        
    def list_repo(self, targetDir = ''):
        if(targetDir!=''):
            if(targetDir.startswith('/')):
                listAddr = self.url + targetDir.substring(1)
            else:
                listAddr = self.url + targetDir
        else:
            listAddr = self.url
            
        self.client.ls(listAddr)
        
    def commit(self):
        try:
            self.client.checkin(self.get_wc_dir(), 'TeXloud Sync')
        except Exception, e:
            print 'erreur de commit: ' + str(e)
        
    #def try_merge(self, path):
        
        
    def resolve_conflict(self):
        changes = self.client.status(self.get_wc_dir())
        
        paths = [f.path for f in changes if f.text_status == pysvn.wc_status_kind.conflicted]
        
        print paths
        
    def add_all(self):
        changes = self.client.status(self.get_wc_dir())
        
        paths = [f.path for f in changes if f.text_status == pysvn.wc_status_kind.unversioned]
        
        for path in paths:
            self.client.add(path)
            
        paths = [f.path for f in changes if f.text_status == pysvn.wc_status_kind.deleted]
        
        for path in paths:
            self.client.remove(path)
        
    client = property(get_client, set_client, del_client, "client's docstring")
    wc_dir = property(get_wc_dir, set_wc_dir, del_wc_dir, "wc_dir's docstring")
        
    
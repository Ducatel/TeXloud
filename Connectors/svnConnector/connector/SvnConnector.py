# -*- coding: UTF-8 -*-
'''
Created on 18 déc. 2011

@author: meva
'''
from connector.GenericConnector import GenericConnector
import datetime
import md5
import pysvn

class SvnConnector(GenericConnector):
    '''
    Classe de connexion à un serveur SVN 
    '''   
    def __init__(self, url, login = '', passwd = '', wc_path = '', checkout = True):
        super(self.__class__,self).__init__(url, login, passwd)
        
        now = datetime.datetime.now()
        
        '''Génération d'un nom théoriquement unique pour la copie locale'''
        dir_name = md5.new(str(now)).hexdigest()
        #self.wc_dir += dir_name
        
        self.client = pysvn.Client()
        
        '''Callback appelé à la connexion si nécéssaire'''
        self.client.callback_get_login = lambda x, y, z: (True, self.login, self.passwd, False)
        
        #self.client.set_default_password(self.login)
        #self.client.set_default_username(self.passwd)
        
        '''
        Si un répertoire est passé en paramètre, met à jour,
        sinon créer le répertoire pour la copie locale
        '''
        if(wc_path):
            self.wc_dir += wc_path
        else:
            self.wc_dir += dir_name
            
        if(checkout):
            ''' Lance la copie avec les paramètres '''
            self.client.checkout(self.url, self.wc_dir)
            
        print 'copie locale enregistrée dans ' + self.wc_dir

    ''' Revient sur les changement locaux (non utilisé pour l'instant) '''
    def revert(self, path):
        self.client.revert(path)

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
        
        print 'listing directory: ' + listAddr
        
        
        return self.client.list(listAddr, recurse=True)
    
    def commit(self):
        try:
            self.client.checkin(self.get_wc_dir(), 'TeXloud Sync')
            print 'committed'
        except Exception, e:
            print 'erreur de commit: ' + str(e)
     
    def revertVersion(self, path):
        info_set = self.client.info(self.wc_dir + '/' + path)
        prevRev = pysvn.Revision(pysvn.opt_revision_kind.number, info_set['commit_revision'].number-1)
        
        self.client.merge(self.url + path, 
                          info_set['commit_revision'],
                          self.url + path,
                          prevRev,
                          self.wc_dir + '/' + path)
        
    def list_conflicts(self):
        conflicted = self.client.status(self.get_wc_dir())
        
        paths = [f.path for f in conflicted if f.text_status == pysvn.wc_status_kind.conflicted]
        conflict_list = []
        
        for path in paths:
            conflict_list.append(self.client.info(path))
            
        return conflict_list
    
    def remove_file(self, path):
        self.client.remove(self.wc_dir + '/' + path)
        
    def resolve_all(self):
        changes = self.client.status(self.get_wc_dir())
        
        paths = [f.path for f in changes if f.text_status == pysvn.wc_status_kind.conflicted]
        
        for path in paths:
            self.client.resolved(path)
        
    def full_commit(self):
        self.add_all()
        self.commit()
        self.resolve_all()
        self.commit()
        
    def add_all(self):
        changes = self.client.status(self.get_wc_dir())
        
        paths = [f.path for f in changes if f.text_status == pysvn.wc_status_kind.unversioned]
        
        for path in paths:
            self.client.add(path)
            
        paths = [f.path for f in changes if f.text_status == pysvn.wc_status_kind.deleted]
        
        for path in paths:
            self.client.remove(path)
            
    def cat(self, path):
        return self.client.cat(self.get_wc_dir() + '/' + path)
        
    client = property(get_client, set_client, del_client, "client's docstring")
    wc_dir = property(get_wc_dir, set_wc_dir, del_wc_dir, "wc_dir's docstring")
        
    
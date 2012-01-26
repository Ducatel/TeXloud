# -*- coding: UTF-8 -*-
'''
Created on 18 déc. 2011

@author: meva
'''

import socket

class GenericConnector(object):
    '''
    Classe mère de connecteur à un serveur de version
    '''

    def __init__(self, url, login = '', passwd = ''):
        '''
        url prend l'adresse du serveur svn
        login et passwd sont optionnelle mais permette de fournir des informations
        d'authentification à la conexion au serveur
        '''
        
        ''' wc_dir -> working copy directory '''
        self.wc_dir = '/tmp/'
        self.url = url;
        self._messageSeparator = '+==\sep==+'
        
        if(not self.url.endswith('/')):
            self.url += '/'
                    
        self.login = login
        self.passwd = passwd

    def get_wc_dir(self):
        return self.__wc_dir

    def get_url(self):
        return self.__url


    def get_login(self):
        return self.__login


    def get_passwd(self):
        return self.__passwd


    def set_wc_dir(self, value):
        self.__wc_dir = value


    def set_url(self, value):
        self.__url = value


    def set_login(self, value):
        self.__login = value


    def set_passwd(self, value):
        self.__passwd = value


    def del_wc_dir(self):
        del self.__wc_dir


    def del_url(self):
        del self.__url


    def del_login(self):
        del self.__login


    def del_passwd(self):
        del self.__passwd

        
    def __str__(self):
        return 'Connecteur svn associé au repo ' + self.url + \
               ', login: ' + self.login + ', passwd: ' + self.passwd 
               
    wc_dir = property(get_wc_dir, set_wc_dir, del_wc_dir, "wc_dir's docstring")
    url = property(get_url, set_url, del_url, "url's docstring")
    login = property(get_login, set_login, del_login, "login's docstring")
    passwd = property(get_passwd, set_passwd, del_passwd, "passwd's docstring")

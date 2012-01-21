#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 12 janv. 2012

@author: meva
'''
from connector.DataSocket import DataSocket

class SvnDataSocket(DataSocket):
    '''
    Socket d'écoute permettant les traitements sur les données des dépots svn
    '''

    def __init__(self, port, address):
        super(self.__class__,self).__init__(port, address)
        
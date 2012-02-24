#!/usr/bin/python
# -*-coding:utf-8 -*-
'''
Created on 19 févr. 2012

@author: hannibal
'''

import logging.handlers
import time
import os

class Logger(object):
    '''
    Class permettant de générer des logs avec rotation
    '''

    def __init__(self,logDir=os.getcwd(),filename='TeXloudCompilation.log',tailleLog=50000,nbFileLog=3):
        '''
        Constructeur de la class Loggeur
        @param logDir: chemin absolu vers le dossier ou vous voulez voir les fichiers de log
        @param filename: nom de base des fichiers de log
        @param tailleLog: taille maximal d'un fichier de log (en Byte)
        @param nbFileLog: nombre de fichier utilisé pour la rotation  
        '''
        if not os.path.exists(logDir):
            os.makedirs(logDir)

        self.logger = logging.getLogger('frontalLoggeur')
        self.logger.setLevel(logging.INFO)

        handler = logging.handlers.RotatingFileHandler(logDir+"/"+filename, backupCount=nbFileLog,maxBytes=tailleLog)

        self.logger.addHandler(handler)

    def write(self,message):
        """
        Methode permettant d'ecrire un message formaté dans le fichier de log
        @param message: message a ecrire dans le log
        """
        self.logger.info('[INFO] at {0} --> {1}'.format(time.asctime(),message))
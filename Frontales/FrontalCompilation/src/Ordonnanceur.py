#!/usr/bin/python

# -*-coding:utf-8 -*

'''
Created on 8 dec. 2011

@author: David Ducatel
'''
from re import match
from xml.dom.minidom import parse

class Ordonnanceur(object):
    '''
    Classe qui va permettre la gestion de l'ordonnancement 
    des serveurs (compilation ou donnees)
    '''

    def __init__(self,chemin):
        '''
        Constructeur de l'ordonnanceur
        @param chemin: chemin jusqu'au fichier XML des serveurs
        '''
        
        # On initialise la liste des serveur
        self.listeServeur=list()
        
        # On initialise le nombre de serveur gere
        self._nbServeur=0

        ########## RECUPERATION DE LA LISTE DES SERVEURS #############
        # On parse le document
        doc=parse(chemin)
        
        # On recupere la racine
        root=doc.documentElement
        
        # on va parcourir le fichier
        for serveur in root.getElementsByTagName('serveur'):
            if serveur.nodeType == serveur.ELEMENT_NODE:
                adresse=serveur.getElementsByTagName('adresse')[0].childNodes[0].nodeValue
                adresse=adresse.lstrip()
                adresse=adresse.rstrip()
                port=serveur.getElementsByTagName('port')[0].childNodes[0].nodeValue
                port=port.lstrip()
                port=port.rstrip()
                self.listeServeur.append((adresse,port))
                self._nbServeur+=1  
      
    
                
    def getServeur(self):
        """
        Methode qui retourne un serveur disponible
        @return serveur sous la forme d'un tuple (AdresseIP,port) si un serveur est disponible, sinon retourne None
        """
        if len(self.listeServeur)>0:
            return self.listeServeur.pop(0)
        else:
            return None
        
    def setServeur(self,adresse,port):
        """
        Methode qui permet de remettre un serveur dans l'ordonnanceur
        @param adresse: adresse IP du serveur
        @param port: numero du port du serveur  
        @return retourne true si l'ajout est effectue, sinon retourne false
        """
        regexAdresse="^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$"
        regexPort="^[0-9]{1,5}$"
        if match(regexAdresse,adresse):
            if match(regexPort,port):
                serveur=(adresse,port)
                self.listeServeur.append(serveur)
                return True
        return False
                
        
########################################################### METHODE D'AFFICHAGE ##################################################################

    def __repr__(self):
        aff="Nombre de serveurs gere l'ordonnanceur: {0}\n".format(self._getNbServeur())
        aff+="Nombre de serveurs disponible: {0}\n".format(len(self.listeServeur))

        for i,serveur in enumerate(self.listeServeur):
            aff+="Position dans l'ordonnanceur: {0}\tadresse IP: {1}\tnumero du port: {2}\n".format(i,serveur[0],serveur[1])
        
        return aff
                
    def __str__(self):
        return self.__repr__()
    
########################################################### METHODE GET ET SET ##################################################################

    
    def getNbServeurDispo(self):
        """
        Methode qui retourne le nombre de serveur disponible
        @return: le nombre de serveur disponible
        """
        return len(self.listeServeur)
                
    def _getNbServeur(self):
        """
        Methode qui retourne le nombre de serveur gere par l'ordonnanceur
        @return:  retourne le nombre de serveur gere par l'ordonnanceur
        """
        return self._nbServeur

    def _setNbServeur(self,nombre):
        """
        Methode qui permet de fixer le nombre de serveur gere par l'ordonnanceur
        @param nombre: Nombre de serveur a gere 
        @raise: retourne une exception type NotImplementedError
        """
        raise NotImplementedError()
    
    
    # Attribut les methodes get et set pour l'attribut _nbServeur
    nbServeur = property(_getNbServeur, _setNbServeur)

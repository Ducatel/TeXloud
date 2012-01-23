#!/usr/bin/python
# -*-coding:utf-8 -*

'''
Created on 8 dec. 2011

@author: David Ducatel
'''
from re import match

class Serveur(object):
    '''
    Class qui permet de gérer un serveur grâce a
    Son adresse IP
    Son Numero de port
    Son Indice de charge maximale
    Son indice de charge actuelle
    Son type (serveur de donnees ou serveur de compilation)
    '''


    def __init__(self, adresseIP, port, chargeMax=1,typ="data"):
        '''
        Constructeur du serveur
        @param adresseIP: Adresse IP du serveur
        @param port: Port de communication du serveur
        @param chargeMax: Nombre de compilation maximal acceptable a un instant t 
        @raise valueError:  declenche une valueError si l'un des parametre est incorrect
        '''
        regexAdresse="^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$"
        regexPort="^[0-9]{1,5}$"
        if match(regexAdresse,adresseIP) and match(regexPort,str(port)) and isinstance(port, int) and isinstance(chargeMax, int) and (typ=="compilation" or typ=="data"):
            if chargeMax > 0:
                self._adresseIP = adresseIP
                self._port = port
                self._chargeMax = chargeMax
                self._type=typ
            else:
                print("chargeMaxError")
                raise ValueError
        else:
            print("other")
            raise ValueError
        
        self._chargeActuelle = 0
      
      
    ########################################################### METHODE D'AFFICHAGE ##################################################################
  
    def __repr__(self):
        return "Adresse IP: {0}\tPort: {1}\tCharge maximale: {2}\tCharge actuelle: {3}\ttype: {4}".format(self._getAdresseIP(),self._getPort(),self._getChargeMax(),self._getChargeActuelle(),self._getType())
    
    def __str__(self):
        return self.__repr__()
    
    ########################################################### METHODE GET ET SET ##################################################################
    
    def _getAdresseIP(self):
        """
        Methode qui permet de recuperer l'adresse IP du serveur
        @return: retourne l'adresse IP du serveur
        """
        return self._adresseIP
    
    def _setAdresseIP(self, adresseIP):
        """
        Methode qui permet de fixer l'adresse IP du serveur
        @param adresseIP: adresseIP du serveur 
        @raise: retourne une exception type NotImplementedError
        """
        raise NotImplementedError()
    
    def _getChargeMax(self):
        """
        Methode qui permet de recuperer le nombre de compilation concurente maximale
        gerable par le serveur
        @return: retourne le nombre de compilation concurente maximale
        """
        return self._chargeMax
    
    def _setChargeMax(self,chargeMax):
        """
        Methode qui permet de fixer le nombre de compilation concurente maximale
        gerable par le serveur
        @param chargeMax: nombre de compilation concurente maximale
        @raise: retourne une exception type NotImplementedError
        """
        raise NotImplementedError()
    
    def _getPort(self):
        """
        Methode qui permet de recuperer le numero de port du serveur
        @return: retourne le numero de port du serveur
        """
        return self._port
    
    def _setPort(self,port):
        """
        Methode qui permet de fixer le port du serveur
        @param port: port du serveur
        @raise: retourne une exception type NotImplementedError
        """
        raise NotImplementedError()
    
    def _getChargeActuelle(self):
        """
        Methode qui permet de recuperer la charge actuelle du serveur
        @return: retourne la charge actuelle du serveur
        """
        return self._chargeActuelle
    
    def _setChargeActuelle(self,chargeActuelle):
        """
        Methode qui permet de fixer la charge actuelle du serveur
        @param chargeActuelle: Charge actuelle du serveur
        """
        raise NotImplementedError()
    
    def _getType(self):
        """
        Methode qui permet de recuperer le type de service sur le serveur
        @return: retourne le type de service sur le serveur
        """
        return self._type
    
    def _setType(self,typ):
        """
        Methode qui permet de fixer le type de service sur le serveur
        @param typ: type de service sur le serveur
        """
        raise NotImplementedError()
   
    #Attribut les methodes get et set 
    adresseIP = property(_getAdresseIP, _setAdresseIP)
    chargeMax=property(_getChargeMax, _setChargeMax)
    port=property(_getPort, _setPort)
    chargeActuelle=property(_getChargeActuelle, _setChargeActuelle)
    typ=property(_getType, _setType)

    ############################################################ METHODE MAGIQUE ###########################################################################


    def __getattr__(self, nom):
        print("Il n'y a pas d'attribut {0} dans cette classe.".format(nom))
         
    def __delattr__(self, nom_attr):
        raise AttributeError("Vous ne pouvez supprimer aucun attribut de cette classe")
    
    def __eq__(self, serveur):
        if serveur.adresseIP ==self._getAdresseIP() and serveur.port==self._getPort():
            return True
        else:
            return False
        
        
    ############################################################ AUTRE METHODE ###########################################################################

    
    def increaseCharge(self):
        """
        Methode qui permet d'augmenter la charge actuelle du serveur
        @return true si le serveur accepte une charge supplementaire, sinon retourne false
        """
        if self._getChargeMax()>self._getChargeActuelle():
            self._chargeActuelle+=1
            return True
        else:
            return False
        
    def decreaseCharge(self):
        """
        Methode qui permet de reduire la charge actuelle du serveur
        @return true si le serveur accepte une charge la modification, sinon retourne false
        """
        if self._getChargeActuelle()>0:
            self._chargeActuelle-=1
            return True
        else:
            return False
        
    
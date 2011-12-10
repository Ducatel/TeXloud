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
    '''


    def __init__(self, adresseIP, port, chargeMax):
        '''
        Constructeur du serveur
        @param adresseIP: Adresse IP du serveur
        @param port: Port de communication du serveur
        @param chargeMax: Nombre de compilation maximal acceptable a un instant t 
        @raise valueError:  declenche une valueError si l'un des parametre est incorrect
        '''
        regexAdresse="^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$"
        regexPort="^[0-9]{1,5}$"
        
        if match(regexAdresse,adresseIP):
            self._adresseIP = adresseIP
            if match(regexPort,port):
                self._port = port
                if isinstance(chargeMax, int):
                    if chargeMax > 0:
                        self._chargeMax = chargeMax
                    else:
                        raise ValueError
                else:
                    raise ValueError
            else:
                raise ValueError
        else:
            raise ValueError
                
        
        self._chargeActuelle = 0
      
      
    ########################################################### METHODE D'AFFICHAGE ##################################################################
  
    def __repr__(self):
        return "Adresse IP: {0} Port: {1} Charge maximale: {2} Charge actuelle: {3}".format(self._getAdresseIP(),self._getPort(),self._getChargeMax(),self._getChargeActuelle())
    
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
   
    #Attribut les methodes get et set 
    adresseIP = property(_getAdresseIP, _setAdresseIP)
    chargeMax=property(_getChargeMax, _setChargeMax)
    port=property(_getPort, _setPort)
    chargeActuelle=property(_getChargeActuelle, _setChargeActuelle)
    
    
    ############################################################ AUTRE METHODE ###########################################################################

    def __getattr__(self, nom):
        print("Il n'y a pas d'attribut {0} dans cette classe.".format(nom))
         
    def __delattr__(self, nom_attr):
        raise AttributeError("Vous ne pouvez supprimer aucun attribut de cette classe")
    
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
'''
Created on 10 d�c. 2011

@author: David Ducatel
'''
from re import match

class FrontalClientData(object):
    '''
    class qui gere le client (socket) qui va ce connect� au frontales des donn�es grace a
    Son adresse IP
    Son numero de port d'ecoute
    Son socket
    '''


    def __init__(self,adresse,port):
        '''
        Constructeur du client qui va communiqu� avec le frontale des donn�es
        @param adresse: Adresse IP du serveur
        @param port: Num�ro du port d'ecoute
        @raise ValueError: Declencher si le port ou l'adresse est incorrect
        '''
        
        regexAdresse="^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$"
        regexPort="^[0-9]{1,5}$"
        if match(regexAdresse,adresse) and match(regexPort,str(port)) :
            if isinstance(port, int):
                self._adresse = adresse
                self._port = port
            else:
                raise ValueError
        else:
            raise ValueError
        
        
#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 8 dec. 2011

@author: David Ducatel
'''


# main du programme
import Ordonnanceur


################################ CREATION ###################################

# Creation de l'ordonnanceur
ordo=Ordonnanceur.Ordonnanceur('./../fichierServeur.xml')

# Affichage de l'ordonnanceur
print (ordo)

############################## RECUPERATION ###############################

# Recuperation d'un serveur
serveur=ordo.getServeur()

# Affichage du serveur recuperer
print (serveur)

# Affichage de l'ordonnanceur
print(ordo)

############################ REMISE EN PLACE ##############################

serveur.increaseCharge()
print (serveur)

# On remet en place le serveur precedent
ordo.setServeur(serveur)

    
# Affichage de l'ordonnanceur
print(ordo)


################################## resultat d'execution ##################
"""
Nombre de serveurs gere l'ordonnanceur: 8
Nombre de serveurs disponible: 8
Position dans l'ordonnanceur: 0    Adresse IP: 192.168.1.2    Port: 4242    Charge maximale: 5    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 1    Adresse IP: 192.168.1.3    Port: 4343    Charge maximale: 2    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 2    Adresse IP: 192.168.1.4    Port: 4444    Charge maximale: 4    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 3    Adresse IP: 192.168.1.5    Port: 5555    Charge maximale: 6    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 4    Adresse IP: 192.168.1.6    Port: 6666    Charge maximale: 6    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 5    Adresse IP: 192.168.1.7    Port: 7777    Charge maximale: 1    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 6    Adresse IP: 192.168.1.8    Port: 8888    Charge maximale: 7    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 7    Adresse IP: 192.168.1.9    Port: 9999    Charge maximale: 10    Charge actuelle: 0    type: compilation

Adresse IP: 192.168.1.2    Port: 4242    Charge maximale: 5    Charge actuelle: 0    type: compilation
Nombre de serveurs gere l'ordonnanceur: 8
Nombre de serveurs disponible: 7
Position dans l'ordonnanceur: 0    Adresse IP: 192.168.1.3    Port: 4343    Charge maximale: 2    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 1    Adresse IP: 192.168.1.4    Port: 4444    Charge maximale: 4    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 2    Adresse IP: 192.168.1.5    Port: 5555    Charge maximale: 6    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 3    Adresse IP: 192.168.1.6    Port: 6666    Charge maximale: 6    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 4    Adresse IP: 192.168.1.7    Port: 7777    Charge maximale: 1    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 5    Adresse IP: 192.168.1.8    Port: 8888    Charge maximale: 7    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 6    Adresse IP: 192.168.1.9    Port: 9999    Charge maximale: 10    Charge actuelle: 0    type: compilation

Adresse IP: 192.168.1.2    Port: 4242    Charge maximale: 5    Charge actuelle: 1    type: compilation
Nombre de serveurs gere l'ordonnanceur: 8
Nombre de serveurs disponible: 8
Position dans l'ordonnanceur: 0    Adresse IP: 192.168.1.3    Port: 4343    Charge maximale: 2    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 1    Adresse IP: 192.168.1.4    Port: 4444    Charge maximale: 4    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 2    Adresse IP: 192.168.1.5    Port: 5555    Charge maximale: 6    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 3    Adresse IP: 192.168.1.6    Port: 6666    Charge maximale: 6    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 4    Adresse IP: 192.168.1.7    Port: 7777    Charge maximale: 1    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 5    Adresse IP: 192.168.1.8    Port: 8888    Charge maximale: 7    Charge actuelle: 0    type: data
Position dans l'ordonnanceur: 6    Adresse IP: 192.168.1.9    Port: 9999    Charge maximale: 10    Charge actuelle: 0    type: compilation
Position dans l'ordonnanceur: 7    Adresse IP: 192.168.1.2    Port: 4242    Charge maximale: 5    Charge actuelle: 1    type: compilation

"""

import Frontale
f=Frontale.Frontale("127.0.0.1",4242)


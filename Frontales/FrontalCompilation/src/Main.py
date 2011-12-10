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
Nombre de serveurs gere l'ordonnanceur: 3
Nombre de serveurs disponible: 3
Position dans l'ordonnanceur: 0    adresse IP: 192.168.1.2    numero du port: 4242    charge maximal: 5    charge actuelle: 0
Position dans l'ordonnanceur: 1    adresse IP: 192.168.1.3    numero du port: 4343    charge maximal: 2    charge actuelle: 0
Position dans l'ordonnanceur: 2    adresse IP: 192.168.1.4    numero du port: 4444    charge maximal: 4    charge actuelle: 0

Adresse IP: 192.168.1.2 Port: 4242 Charge maximale: 5 Charge actuelle: 0
Nombre de serveurs gere l'ordonnanceur: 3
Nombre de serveurs disponible: 2
Position dans l'ordonnanceur: 0    adresse IP: 192.168.1.3    numero du port: 4343    charge maximal: 2    charge actuelle: 0
Position dans l'ordonnanceur: 1    adresse IP: 192.168.1.4    numero du port: 4444    charge maximal: 4    charge actuelle: 0

Adresse IP: 192.168.1.2 Port: 4242 Charge maximale: 5 Charge actuelle: 1
Nombre de serveurs gere l'ordonnanceur: 3
Nombre de serveurs disponible: 3
Position dans l'ordonnanceur: 0    adresse IP: 192.168.1.3    numero du port: 4343    charge maximal: 2    charge actuelle: 0
Position dans l'ordonnanceur: 1    adresse IP: 192.168.1.4    numero du port: 4444    charge maximal: 4    charge actuelle: 0
Position dans l'ordonnanceur: 2    adresse IP: 192.168.1.2    numero du port: 4242    charge maximal: 5    charge actuelle: 1
"""



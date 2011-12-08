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

# On remet en place le serveur precedent
if ordo.setServeur(serveur[0], serveur[1]):
    print ("Ajout sans probleme")
else:
    print ("Une erreur dans le numero de port ou l'adresse IP")
    
# Affichage de l'ordonnanceur
print(ordo)


################################## resultat d'execution ##################
"""
Nombre de serveurs gere l'ordonnanceur: 3
Nombre de serveurs disponible: 3
Position dans l'ordonnanceur: 0    adresse IP: 192.168.1.2    numero du port: 4242
Position dans l'ordonnanceur: 1    adresse IP: 192.168.1.3    numero du port: 4343
Position dans l'ordonnanceur: 2    adresse IP: 192.168.1.4    numero du port: 4444

('192.168.1.2', '4242')
Nombre de serveurs gere l'ordonnanceur: 3
Nombre de serveurs disponible: 2
Position dans l'ordonnanceur: 0    adresse IP: 192.168.1.3    numero du port: 4343
Position dans l'ordonnanceur: 1    adresse IP: 192.168.1.4    numero du port: 4444

Ajout sans probleme
Nombre de serveurs gere l'ordonnanceur: 3
Nombre de serveurs disponible: 3
Position dans l'ordonnanceur: 0    adresse IP: 192.168.1.3    numero du port: 4343
Position dans l'ordonnanceur: 1    adresse IP: 192.168.1.4    numero du port: 4444
Position dans l'ordonnanceur: 2    adresse IP: 192.168.1.2    numero du port: 4242
"""


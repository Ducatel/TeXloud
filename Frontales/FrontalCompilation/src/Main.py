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





#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 8 dec. 2011

@author: David Ducatel
'''


# main du programme
import Ordonnanceur


# Creation de l'ordonnanceur
ordo=Ordonnanceur.Ordonnanceur('./../fichierServeur.xml')

# Affichage de l'ordonnanceur
print (ordo)

# recuperation d'un serveur
serveur=ordo.getServeur()
print(ordo)

# on indique que le serveur a fini son travail
ordo.finTravail(serveur)
print (ordo)



# en cas de surcharge (trop de damande de travail et tous les serveurs occupé), une exception est leve
for i in range(1,100):
    serveur=ordo.getServeur()
    print(ordo)








#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 8 dec. 2011

@author: David Ducatel
'''


##################################### TEST DE L'ORDONNANCEUR #####################################################
"""
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
    print(ordo)"""



##################################### TEST DU SERVEUR #####################################################

import threading
import Frontal

def lanceServeur():
    f=Frontal.Frontal('127.0.0.1',12800)
    f.lanceServeur()


thread=threading.Thread(group=None, target=lanceServeur, name=None, args=(), kwargs={})
thread.start()

print("server start on 12800")







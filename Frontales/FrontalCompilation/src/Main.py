#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 8 dec. 2011

@author: David Ducatel
'''

'''
import Frontal

f=Frontal.Frontal('127.0.0.1',12800)
f.lanceServeur()'''


'''
import json

dico=dict()
dico['label']='unLabel'
dico['test']='chaineDeTest'
jsonDico=json.dumps(dico)

print(jsonDico)
t=json.loads(jsonDico)

print(t['label'])
print(t['test'])
'''


import Ordonnanceur

o=Ordonnanceur.Ordonnanceur("./../fichierServeur.xml","data")
serveur=o.getServeur()
print serveur.adresseIP
print serveur.port

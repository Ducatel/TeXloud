#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 19 janv. 2012

@author: David Ducatel
'''
'''
import ServiceCompilation

ServiceCompilation=ServiceCompilation.ServiceCompilation('172.16.21.186',12800)
ServiceCompilation.lanceServeur()
'''
'''
import subprocess
import os

path="/tmp/rapport/"
masterFile="rapport.tex"


os.chdir(path)
valSortie=subprocess.call(["latexmk","-bibtex","-pdf","-quiet","rapport.tex"])
print(valSortie)

if valSortie==0:
    print ('fichier compiler sans probleme')
else:
    print('fichier non compiler, erreur')
    '''

import os
import subprocess    
s="/tmp/rapport.zip"
nomFichier="rapport.tex"

nomArchive=s.split('/tmp/')[1]
pathDir='/tmp/'+nomArchive[0:len(nomArchive)-4]+"/"

os.chdir(pathDir)

print(nomArchive)
print(pathDir)
logFile=pathDir+nomFichier[0:len(nomFichier)-3]+"log"
print(logFile)

for line in open(logFile):
    if "Output written on" in line:
        print (line)




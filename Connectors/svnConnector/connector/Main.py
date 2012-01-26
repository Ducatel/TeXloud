#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 8 dec. 2011

@author: meva
'''

from connector import SvnDataSocket
import glob
import json
import os
import zipfile

dsock = SvnDataSocket.SvnDataSocket('172.16.21.185', 6668)
dsock.launch()

'''
target = '/tmp/texloud/users.conf'
f = open(target, 'r+')
usersJSON = f.read()

print 'conf -> ' + usersJSON

users = json.loads(usersJSON)
wcdir = 'cd0d1a129f019d1e43fe78867751282d'
prop = 'username'

if(wcdir in users and prop in users[wcdir]):
    print users[wcdir][prop]

def compresse(filezip, pathzip):
    """
    Fonction qui permet de compresser un dossier
    Exemple d'utilisation: compresse('/home/david/Bureau/TeXloud.zip', '/home/david/TeXloud')
    @param filezip: Nom de l'archive qui va etre generer
    @param pathzip: Chemin vers l'arborescence qui doit etre compressé
    """
    lenpathparent = len(pathzip)+1   ## utile si on veut stocker les chemins relatifs
    def _compresse(zfile, path):
        for i in glob.glob(path+'\\*'):
            if os.path.isdir(i): _compresse(zfile, i )
            else:
                zfile.write(i, i[lenpathparent:]) ## zfile.write(i) pour stocker les chemins complets
    zfile = zipfile.ZipFile(filezip,'w',compression=zipfile.ZIP_DEFLATED)
    _compresse(zfile, pathzip)
    zfile.close()
    
compresse('/tmp/plop.zip', '/tmp/115acd6be30aedd124c924314651fe4d/')
'''
#requestInfo = json.loads('{"label" : "getProject", "path" : "/tmp"}')
#requestInfo = json.loads('["foo", {"bar":["baz", null, 1.0, 2]}]')

#print requestInfo['label']

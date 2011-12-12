#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 12 déc. 2011
Fichier qui contient les fonctions de 
compression et decompression ZIP

@author: David Ducatel
'''

import zipfile
import os.path
import glob

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


def decompresse(filezip, pathdst = ''):
    """
    Fonction qui permet de decompresser une archive
    Exemple d'utilisation: decompresse('/home/david/Bureau/TeXloud.zip', '/home/david/Bureau/TeXloudExtract')
    @param filezip: Archive a decompresser
    @param pathdst: Chemin d'extraction 
    """
    if pathdst == '': pathdst = os.getcwd()  ## on dezippe dans le repertoire locale
    zfile = zipfile.ZipFile(filezip, 'r')
    for i in zfile.namelist():  ## On parcourt l'ensemble des fichiers de l'archive
        if os.path.isdir(i):   ## S'il s'agit d'un repertoire, on se contente de creer le dossier
            try: os.makedirs(pathdst + os.sep + i)
            except: pass
        else:
            try: os.makedirs(pathdst + os.sep + os.path.dirname(i))
            except: pass
            data = zfile.read(i)                   ## lecture du fichier compresse
            fp = open(pathdst + os.sep + i, "wb")  ## creation en local du nouveau fichier
            fp.write(data)                         ## ajout des donnees du fichier compresse dans le fichier local
            fp.close()
    zfile.close()
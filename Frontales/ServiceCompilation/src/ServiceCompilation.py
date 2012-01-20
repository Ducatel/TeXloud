#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 19 janv. 2012

@author: David Ducatel
'''

import socket
from re import match
import threading
import os
import zipfile
import subprocess

class ServiceCompilation(object):
    '''
        Classe qui gere les demandes de compilations
    '''
    
    def __init__(self,adresse,port):
        '''
        Constructeur du service de gestion des compilation
        @param adresse: Adresse IP du serveur
        @param port: Numéro du port d'ecoute
        @raise ValueError: Declencher si le port ou l'adresse est incorrect
        '''
        
        regexAdresse="^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$"
        regexPort="^[0-9]{1,5}$"
        if match(regexAdresse,adresse) and match(regexPort,str(port)):
            if isinstance(port, int):
                self._adresse = adresse
                self._port = port
            else:
                raise ValueError
        else:
            raise ValueError
        
        self._messageSeparator='+==\sep==+'
        
    def lanceServeur(self):
        """
        Methode qui lance la socket en ecoute
        Puis qui accepte les connexions et appelle pour chacune d'elle la methode 
        De gestion des compilation dans un nouveau thread
        """
        self._sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._sock.bind((self._adresse, self._port))
        self._sock.listen(5)
        
        while 1:
            client, addr=self._sock.accept()
            threading.Thread(target=self.compilationManager,args=(client,addr)).start()
            
    def compilationManager(self,client,addr):
        '''
            Méthode qui gere tout le processus de compilation
        '''
        
        # On recupere les données
        nomFichier,adresseArchive=self.getDataforCompilation(client,addr)
        
        # On compile le projet
        msgCompile,addrFichierPdf=self.latexCompilation(nomFichier, adresseArchive)

            
    def getDataforCompilation(self,client,addr):
        """
            Methode qui permet de recuperer le nom du fichier maitre et
            l'archive du projet a compiler 
            @return: le nom du fichier maitre
            @return: l'adresse de l'archive
        """
        nomFichier=""
        taille=1
        messageComplet=""
        
        while taille>0:
            message=client.recv(1024)
            messageComplet+=message
            taille=len(message)
            if messageComplet.endswith(self._messageSeparator):
                tabTmp=messageComplet.split(self._messageSeparator)
                nomFichier=tabTmp[0]
                messageComplet=tabTmp[1]
                
        client.close()
    
        os.chdir("/tmp")
        
        uniqueId=os.urandom(10)
        with open(str(uniqueId)+'.zip', 'wb') as fichier:
            fichier.write(messageComplet)
        
        return nomFichier,"/tmp/{0}.zip".format(str(uniqueId))
                    
            
    def latexCompilation(self,nomFichier,adresseArchive):
        '''
            Méthode qui va compiler le projet latex
            @param nomFichier: nom du fichier maitre (sert a lancer la compilation)
            @param adresseArchive: adresse de l'archive qui contient le projet a compiler (/tmp/riuhgf.zip)
            @return: Le message de compilation (message d'erreur de compilation, ou message standard)
            @return: L'adresse du fichier PDF compiler (peut etre None si erreur de compilation) 
        '''
        
        nomArchive=adresseArchive.split('/tmp/')[1]
        pathDir='/tmp/'+nomArchive[0:len(nomArchive)-4]+"/"

        #Décompression de l'archive
        self.decompresse(adresseArchive,pathDir)
        
        # on change de repertoire de travail
        os.chdir(pathDir)
        
        # Appel au script latexmk pour la compilation
        valSortie=subprocess.call(["latexmk","-bibtex","-pdf","-quiet",nomFichier])
        
        # Si la sortie du script est egal a 0, il n'y a aucun probleme lors de la compilation
        if valSortie==0:
            # Ici la compilation est ok
            logFile=pathDir+nomFichier[0:len(nomFichier)-3]+"log"

            # on recupere une ligne du log en temps que message de retour 
            for line in open(logFile):
                if "Output written on" in line:
                    messageRetour=line
            
            #TODO recuperer le contenue binaire du fichier pdf
            pdfFileContent=""
            
            #TODO supprimer le dossier
        else:
            print('fichier non compiler, erreur')
        
        
        return messageRetour,pdfFileContent

    def decompresse(self,filezip, pathdst = ''):
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


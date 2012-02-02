#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 12 janv. 2012

@author: meva
'''
from abc import abstractmethod
import datetime
import glob
import json
import md5
import os
import re
import socket
import threading
import zipfile

class DataSocket(object):
    '''
    Socket d'écoute permettant les traitements sur les données
    '''

    def __init__(self, address, port):
        self._address = address
        self._port = port
        self._messageSeparator = '+==\sep==+'
        self._trameEnd = '+==\endTrame==+'
        self._webserver_ip = '127.0.0.1'
        self._webserver_port = 6667
        self.wc_dir = '/tmp/'
        self.working_copy_user_index = '/tmp/texloud/users.conf'
        self._repo_dir = '/tmp/texloud/repos/'
        
        if(not os.path.isdir(self._repo_dir)):
            os.makedirs(self._repo_dir)
            
            if(not os.path.exists(self.working_copy_user_index)):
                f = open(self.working_copy_user_index, 'w')
                f.close()
            
        
    def getUserProperty(self, wc_dir, prop):
        f = open(self.working_copy_user_index, 'r')
        usersJSON = f.read()
        f.close()
        
        if(len(usersJSON)>0):
            users = json.loads(usersJSON)
            
            if(wc_dir in users and prop in users[wc_dir]):
                return users[wc_dir][prop]
        
        return None
    
    def remove_user_prop(self, key):
        f = open(self.working_copy_user_index, 'r+')
        usersJSON = f.read()
        
        users = json.loads(usersJSON)
        
        del users[key]
        
        f.seek(0, 0)
        f.truncate()
        
        usersJSON = json.dumps(users)
        
        f.write(usersJSON)
        f.close()
        
    def save_user_conf(self, wc_dir, username, password, repo):
        f = open(self.working_copy_user_index, 'r+')
        usersJSON = f.read()
        
        print 'conf -> ' + usersJSON
        
        if(len(usersJSON)>0):
            print 'récupération d\'infos'
            users = json.loads(usersJSON)
        else:
            users = dict()
            
        userInfos = dict()
            
        userInfos['username'] = username
        userInfos['password'] = password
        userInfos['repo'] = repo
        
        users[wc_dir] = userInfos
        
        f.seek(0, 0)
        f.truncate()
        
        usersJSON = json.dumps(users)
        
        f.write(usersJSON)
        f.close()
        
    def zip_dir(self, pathzip):
        """
        Fonction qui permet de compresser un dossier
        Exemple d'utilisation: zip_dir('/home/david/Bureau/TeXloud.zip', '/home/david/TeXloud')
        @param filezip: Nom de l'archive qui va etre generer
        @param pathzip: Chemin vers l'arborescence qui doit etre compressé
        """
        now = datetime.datetime.now()
        
        '''Génération d'un nom théoriquement unique pour la copie locale'''
        filezip = self.wc_dir + md5.new(str(now)).hexdigest() + '.zip'
    
        lenpathparent = len(pathzip)+1   ## utile si on veut stocker les chemins relatifs
        
        def _zip_dir(zfile, path):
            for i in glob.glob(path+'/*'):
                if os.path.isdir(i): _zip_dir(zfile, i )
                else:
                    zfile.write(i, i[lenpathparent:]) ## zfile.write(i) pour stocker les chemins complets
                    
        zfile = zipfile.ZipFile(filezip,'w',compression=zipfile.ZIP_DEFLATED)
        _zip_dir(zfile, pathzip)
        zfile.close()
        
        return filezip
        
    def compile(self, rootFile, servCompileIp, servCompilePort, path, httpPort, client):
        ''' Envoi d'un fichier binaire à un client '''
        if not '..' in path:
            rootFileJSON = '{"label" : "compile", "rootFile" : "' + rootFile + '", "returnIp" : "' + self._address \
                            + '", "returnPort" : "' + str(self._port) + '", "httpPort":"' +httpPort + '"}';
            
            fname = self.zip_dir(self.wc_dir + path);
            
            f = open(fname, 'rb')
            data = f.read()
            f.close()
            
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.connect((servCompileIp, int(servCompilePort)))
    
            sock.sendall(rootFileJSON)
            sock.send(self._messageSeparator)
            sock.send(data)
            sock.send(self._trameEnd)
            
            sock.close()
            
            os.unlink(fname)
            #os.system('rm ' + fname)
        
    def sendPdfToWebServer(self, log, pdf, httpPort):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((self._webserver_ip, int(httpPort)))

        sock.send('{"log" : "' + log + '"}' + self._messageSeparator + pdf)
        sock.send(pdf)
        
        sock.close()
        #sock.send(self._messageSeparator)
        #sock.sendall(data)
        
    def sendAsciiMessage(self, request, ip, port):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((ip, int(port)))
        sock.send(request)
        sock.close()
        
    def launch(self):
        self._sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._sock.bind((self._address, self._port))
        self._sock.listen(5)
        
        print 'serveur lancé sur ' + self._address + ':' + str(self._port)
        
        while 1:
            client, addr = self._sock.accept()
            print 'nouvelle connexion'
            threading.Thread(target=self.handleRequest,args=(client,addr)).start()
            
    def handleRequest(self, client, addr):
        taille=1
        completeMessage=""
        
        while taille>0:
            message=client.recv(1024)
            
            completeMessage+=message
            taille=len(message)
        
            if((completeMessage.endswith(self._trameEnd) and len(completeMessage)>0) or (taille==0 and len(completeMessage)>0)):
                completeMessage = self.cleanRequest(completeMessage)
                messageArray = completeMessage.split(self._messageSeparator)
                
                print 'partie 1 -> ' + str(messageArray[0])
                
                #récupère les informations sur la requete
                requestInfo = json.loads(messageArray[0])
                
                print 'objet -> ' + str(requestInfo)
                
                label = requestInfo['label']
        
                if(label=='create'):
                    print 'create'
                    self.createProject(requestInfo['projectName'], requestInfo['httpPort'], client)
                #tested - work
                elif(label=='getProject'):
                    print 'getProject'
                    self.getProject(requestInfo['path'], requestInfo['username'], requestInfo['password'], requestInfo['httpPort'], client)
                elif(label=='compile'):
                    print 'compile'
                    self.compile(requestInfo['rootFile'], requestInfo['servCompileIp'],
                                requestInfo['servCompilePort'], requestInfo['path'], requestInfo['httpPort'], client)
                #tested - work
                elif(label=='getFile'):
                    print 'getFile'
                    self.getFile(requestInfo['path'], requestInfo['filename'], requestInfo['httpPort'], client)
                #tested - work
                elif(label=='deleteFile'):
                    print 'deleteFile'
                    self.deleteFile(requestInfo['path'], requestInfo['filename'], requestInfo['httpPort'], client)
                #tested - work
                elif(label=='createFile'):
                    print 'createFile'
                    self.createFile(requestInfo['path'], requestInfo['filename'], messageArray[1], requestInfo['httpPort'], client)
                #tested - work
                elif(label=='deleteProject'):
                    print 'deleteProject'
                    self.deleteProject(requestInfo['path'], requestInfo['httpPort'], client)
                #tested - work
                elif(label=='sync'):
                    print 'sync -> ' + str(requestInfo['files'])
                    self.sync(requestInfo['path'], requestInfo['files'], requestInfo['httpPort'], client)
                elif(label=="backCompile"):
                    print 'send compiled file to server'
                    self.sendPdfToWebServer(requestInfo['log'], messageArray[1], requestInfo['httpPort'])
                else:
                    print 'failed to retrieve action'
                    
                completeMessage = ""
        
        print 'fin de connexion'
                
      
    ''' synchronise plusieurs fichiers sur la copie locale puis commit '''
    def sync(self, path, files, httpPort, client):
        if not '..' in path:
            for fdata in files:
                print 'fdata -> ' + str(fdata)
                if not '..' in fdata['filename']:
                    fparts = fdata['filename'].split(os.sep)
                    filePath = '/' + fdata['filename'][:-len(fparts[-1])]
                    
                    if not os.path.isdir(self.wc_dir + path + filePath):
                        os.system("mkdir -p " + self.wc_dir + path + filePath )
                        
                    f = open(self.wc_dir + path + '/' + fdata['filename'], 'w')
                    f.write(fdata['content'])
                    f.close()
        #client.close()
            
    def cleanRequest(self, request):
        if(request.endswith(self._trameEnd)):
            return request[:-len(self._trameEnd)]
        
        return request
    
    @abstractmethod        
    def getProject(self, path, username, password, httpPort, client):
        return NotImplemented
        
    def createProject(self, projectName, httpPort, client):
        print 'nom du dépot' + projectName
        
        if not '..' in projectName:
            os.system('svnadmin create ' + self._repo_dir + projectName)
            print 'repo addr -> ' + "file://" + self._repo_dir + projectName
            self.getProject("file://" + self._repo_dir + projectName, '', '', httpPort, client)
    
    def getFile(self, path, filename, httpPort, client):
        if not '..' in path and not '..' in filename:
            f = open(self.wc_dir + path + '/' + filename, 'rb')
            data = f.read()
            f.close()
            
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.connect((self._webserver_ip, int(httpPort)))
            sock.send(data)
            sock.close()
    
    @abstractmethod
    def deleteFile(self, path, filename, client):
        if not '..' in path and not '..' in filename:
            os.system('rm -rf ' + self.wc_dir + path + '/' + filename)
    
    @abstractmethod
    def deleteProject(self, path, httpPort, client):
        if not '..' in path:
            self.remove_user_prop(path)
            os.system('rm -rf ' + self.wc_dir + path)
    
    ''' Crée un fichier donc le contenu est passé dans file_content (prend les fichiers binaires) '''
    def createFile(self, path, filename, file_content, httpPort, client):
        if not '..' in path and not '..' in filename:
            fparts = filename.split(os.sep)
            filePath = '/' + filename[:-len(fparts[-1])]
                    
            if not os.path.isdir(self.wc_dir + path + filePath):
                os.system("mkdir -p " + self.wc_dir + path + filePath )
                
            f = open(self.wc_dir + path + '/' + filename, 'w')
            f.write(file_content)
            f.close()

    ''' Connexion à la socket serveur pour le test
        depot de test: file:///home/meva/test_repo/, file:///tmp/texloud/repos/plop/
        getProject: sock.send('{"label":"getProject","path":"file:///home/meva/test_repo/"}')
        deleteProject: sock.send('{"label":"deleteProject","path":"53d4ad39451eac91f7a983fd2d4ab34c"}')
        sync: sock.send('{"label" : "sync", "path" : "53d4ad39451eac91f7a983fd2d4ab34c", "files" : [{"filename" : "bouh/bouhbouh.txt", "content" : "blahblah commit"}]}')
        compile: {"label":"compile","path":"65a3c9a8fa66596de53bec9377a89aef", "rootFile" : "rapport.tex", "servCompileIp" : "127.0.0.1","servCompilePort":"6667"}
       
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)z
        sock.connect(('127.0.0.1', 31346))
        sock.send('{"label" : "sync", "path" : "53d4ad39451eac91f7a983fd2d4ab34c", "files" : [["pdfgsbvslop", "contenu"],["hdufk","gfyig"]]s}')
        sock.send('+==\endTrame==+')
        sock.close()
    '''
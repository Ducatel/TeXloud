'''
Created on 23 janv. 2012

@author: hannibal
'''
import socket
import json

adresseIP="172.16.21.186"
port=12800
req=dict()

#req['label']="create"
#req['username']="UnNom"
#req['projectName']="projetToto"


req['label']="getProject"
req['path']="/home/hannibal/test"
req['servDataIp']="172.16.21.185"
req['servDataPort']=6666

#req['label']="compile"
#req['path']="/home/hannibal/test"
#req['rootFile']="fichier maitre"
#req['servDataIp']="192.168.0.1"
#req['servDataPort']=4242

#req['label']="getFile"
#req['path']="/home/hannibal/test"
#req['servDataIp']="192.168.0.1"
#req['servDataPort']=4242

s=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((adresseIP,port))
s.send(json.dumps(req))
s.close()

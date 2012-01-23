#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 8 dec. 2011

@author: meva
'''

import json
from connector import SvnDataSocket

dsock = SvnDataSocket.SvnDataSocket('127.0.0.1', 31346)
dsock.launch()


#requestInfo = json.loads('{"label" : "getProject", "path" : "/tmp"}')
#requestInfo = json.loads('["foo", {"bar":["baz", null, 1.0, 2]}]')

#print requestInfo['label']
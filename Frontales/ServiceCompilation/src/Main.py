#!/usr/bin/python
# -*-coding:utf-8 -*
'''
Created on 19 janv. 2012
@author: David Ducatel
'''

import ServiceCompilation

ip='172.16.21.186'
port=12800
print 'Server run on {0} port {1}\n'.format(ip,str(port))

ServiceCompilation=ServiceCompilation.ServiceCompilation(ip,port)
ServiceCompilation.lanceServeur()

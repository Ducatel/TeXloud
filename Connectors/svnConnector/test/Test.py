# -*- coding: UTF-8 -*-
'''
Created on 18 dec. 2011

@author: meva
'''
from connector import SvnConnector

if __name__ == '__main__':
    #conn = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'meva', 'plop', '8cc936c66a3b4ebea2ac57bdca3f1b2d')
    conn = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'nyopnyop', 'plipplop', '892325c87b5219f287f6c4eac3b939b6')
    #conn.add_all()
    #conn.commit()
    conn.resolve_conflict()
    print(conn)
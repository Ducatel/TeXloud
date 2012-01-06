# -*- coding: UTF-8 -*-
'''
Created on 18 dec. 2011

@author: meva
'''
from connector import SvnConnector
import pprint
import pysvn

if __name__ == '__main__':
    conn = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'meva', 'plop', '8cc936c66a3b4ebea2ac57bdca3f1b2d')
    #conn = SvnConnector.SvnConnector('svn://87.98.144.200/test_svn', 'nyopnyop', 'plipplop', '892325c87b5219f287f6c4eac3b939b6')
    #conn.add_all()
    
    
    #list = conn.list_repo()
    
    #for i in list:
    #     print i
    
    #print conn.revert('un_chameau')
    #conn.revertVersion('un_dromadaire')
    #print 'get infos'
    
    #conflicts = conn.list_conflicts()
    
    #for c in conflicts:
    #    print c.data

    conn.resolve_all();

#    conn.get_client().merge('svn://87.98.144.200/test_svn/un_chameau', 
#                            info_set['commit_revision'],
#                            'svn://87.98.144.200/test_svn/un_chameau',
#                            prevRev,
#                            '/tmp/8cc936c66a3b4ebea2ac57bdca3f1b2d/un_chameau')
            
    #for i in info_set.data:
    #     print str(i) + ' -> ' + str(info_set.data[i])
            
    conn.commit()
    
    print(conn)
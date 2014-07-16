##
#
#  Copyright 2014 Netflix, Inc.
#
#     Licensed under the Apache License, Version 2.0 (the "License");
#     you may not use this file except in compliance with the License.
#     You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
#     Unless required by applicable law or agreed to in writing, software
#     distributed under the License is distributed on an "AS IS" BASIS,
#     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#     See the License for the specific language governing permissions and
#     limitations under the License.
#
##

import sys
sys.path.append('../utils')

import time
import eureka
import json
import os
import uuid
import restclient


def addApplicationToCommand(commandId,applicationId):
    print "Adding Application [%s] to Command [%s] " % (applicationId, commandId) 

    # get the serviceUrl from the eureka client
    serviceUrl = eureka.EurekaClient().getServiceBaseUrl() + '/genie/v2/config/commands/' + commandId + '/applications'
    print "Service Url isi %s" % serviceUrl 
    
    #cmds = json.dumps([{'id':'prodhive11_mr1'},{'id':'pig11_mr1'},{'id':'hadoop103'}])
    cmds = json.dumps([{'id':applicationId}])
    payload = cmds 
    print payload
    print "\n"
    
    print restclient.post(serviceUrl=serviceUrl, payload=payload, contentType='application/json')

# driver method for all tests                
if __name__ == "__main__":
    if(len(sys.argv) != 3):
        print "Usage: addApplicationToCommand commandId applicationId"
        sys.exit(-1)

    addApplicationToCommand(sys.argv[1],sys.argv[2])
###############################################################################
#
# Copyright (c) 2011 - 2013 Talend Inc. - www.talend.com
# All rights reserved.
#
# This program and the accompanying materials are made available
# under the terms of the Apache License v2.0
# which accompanies this distribution, and is available at
# http://www.apache.org/licenses/LICENSE-2.0
#
###############################################################################

Library Tutorial Example
=================================
This is a web application example.
This example demonstrates using of different message exchange patterns on the base of Library service.

Example contains single module: library-tutorial.
Deployment:
- tomcat or jetty web container for service
- standalone for client

Running the Example
-------------------
From the base directory of this example (i.e., where this README file is
located), the maven pom.xml file can be used to build this example. 

Using maven commands on either UNIX/Linux or Windows:
(JDK 1.7.0 and Maven 3.0.3 or later required)


a) Without Service Registry:

   1. Prepare TESB container
      - start TESB container
      - run following command in container:

      tesb:start-aux-store

      - run the following command if you have not started an external ActiveMQ broker

      features:install activemq-broker


   2. Run service:
      In the command line from the "war" directory run:  mvn -Pservice

   3. Run client:
      In the command line from the "client" directory run:  mvn -Pclient


b) *** This option is only applicable to the users of Talend Enterprise ESB *** 
   With Service Registry:

   1. Prepare TESB container
      - start TESB container
      - run following commands in container:

      tesb:start-aux-store
      tesb:start-registry
      tesb:start-sts
      tesb:start-sam
      tesb:switch-sts-jaas

      - uploads into the service registry
      - <sr-resources-dir> is "<library-service-dir>/common/src/main/sr-resources"
      - <library-service-dir> is the directory which contains the present README

      tregistry:create wsdl <sr-resources-dir>/Library.wsdl

      - depending on the policy you want to activate,
      - do one of the following policy document uploads
      - (by default, you should do policy upload (a)):

      - (a) SAML authentication and SAM enabling:

      tregistry:create ws-policy <sr-resources-dir>/policies/ws-policy-saml-and-sam-enabling.xml
      tregistry:create ws-policy-attach <sr-resources-dir>/policies/ws-policy-attach-saml-and-sam-enabling.xml
      tregistry:create ws-policy-attach <sr-resources-dir>/policies/ws-policy-attach-saml-and-sam-enabling-callback.xml

      - (b) SAML authentication only:

      tregistry:create ws-policy <sr-resources-dir>/policies/ws-policy-saml.xml
      tregistry:create ws-policy-attach <sr-resources-dir>/policies/ws-policy-attach-saml.xml
      tregistry:create ws-policy-attach <sr-resources-dir>/policies/ws-policy-attach-saml-callback.xml

      - (c) SAM enabling only:

      tregistry:create ws-policy <sr-resources-dir>/policies/ws-policy-sam-enabling.xml
      tregistry:create ws-policy-attach <sr-resources-dir>/policies/ws-policy-attach-sam-enabling.xml
      tregistry:create ws-policy-attach <sr-resources-dir>/policies/ws-policy-attach-sam-enabling-callback.xml


   2. Run service:
      In the command line from the "war" directory run:  mvn -Pservice-sr

   3. Run client:
      In the command line from the "client" directory run:  mvn -Pclient-sr


To run client/service from eclipse:
a) Without Service Registry:
   mvn eclipse:eclipse
   Export library-service, library-client and library-common projects into eclipse.
   Use LibraryServer.java LibraryClient.java main() methods to start service and client.
b) With Service Registry:
   mvn eclipse:eclipse -Pservice-registry
   Export library-service, library-client and library-common projects into eclipse.
   Use LibraryServer.java LibraryClient.java main() methods to start service and client.



Usage of IBM WebSphere MQ as a MOM
---------------------------------
*** This option is only applicable to the users of Talend Enterprise ESB ***

1. To use WSMQ as a MOM, JMS URI's should be changed. For example,
   to connect to "exampleQueue" which is running on localhost:1414
   using channel EXAMPLE.CHANNEL.SVRCONN following URI may be used:

       jms:queue:exampleQueue?jndiInitialContextFactory=org.talend.esb.jms.wmq.Nojndi&jndiConnectionFactoryName=connectQueueManager(QMGR)binding(client)clientChannel(EXAMPLE.CHANNEL.SVRCONN)clientConnection(localhost:1414)

   Topic connection URI looks like the one below:

       jms:topic:exampleTopic?jndiInitialContextFactory=org.talend.esb.jms.wmq.Nojndi&jndiConnectionFactoryName=connectQueueManager(QMGR)binding(client)clientChannel(EXAMPLE.CHANNEL.SVRCONN)clientConnection(localhost:1414)&jndi-destination-type=topic


   NOTE: In xml and wsdl files symbol "&" in URI should be replaced with  "&amp;"  (without double quotes).



2. Following dependency should be added to client and service classpath:

       <dependency>
           <groupId>org.talend.esb</groupId>
           <artifactId>tesb-jms-transport-enterprise</artifactId>
           <version>${project.version}</version>
       </dependency>


3. As WS MQ client library jars provided by IBM are not supplied with Talend Enterprise ESB,
   they should be added to client and service classpath manually.

###############################################################################
#
# Copyright (c) 2011 - 2014 Talend Inc. - www.talend.com
# All rights reserved.
#
# This program and the accompanying materials are made available
# under the terms of the Apache License v2.0
# which accompanies this distribution, and is available at
# http://www.apache.org/licenses/LICENSE-2.0
#
###############################################################################

Library Tutorial OSGi Example
=================================
This is an example of OSGi applications.
This example demonstrates using of different message exchange patterns on the base of Library service.

Example contains 4 modules:
- service   Contains code of the service provider
- client    Contains code of the service consumer
- common    Contains code common for provider and consumer      
- features  Contains Karaf features to install service provider and consumer


Deployment:
- TESB container

Running the Example
-------------------
From the base directory of this example (i.e., where this README file is
located), the maven pom.xml file can be used to build this example. 

Using maven commands on either UNIX/Linux or Windows:
(JDK 1.7.0 and Maven 3.0.3 or later required)


a) Without Service Registry
   1. Build the example:

      mvn clean install

   2. Prepare the TESB container
      - start the TESB container
      - run the following commands in the container:

      tesb:start-aux-store

      features:addurl mvn:org.talend.esb.examples.library-service/library-features/<version>/xml

      - run the following command if you have not started an external ActiveMQ broker

      features:install activemq-broker

   3. Start the service provider
      - run the following commands in the container:

      features:install library-service

   4. Start the service consumer
      - run the following commands in the container:

      features:install library-client


b) *** This option is only applicable to the users of Talend Enterprise ESB *** 
   With Service Registry:

   1. Build the example:

      mvn -Pservice-registry clean install

   2. Prepare the TESB container
      - start the TESB container
      - run the following commands in the container:

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

      - after service registry document upload, prepare installation of the service feature

      features:addurl mvn:org.talend.esb.examples.library-service/library-features/<version>/xml

      - run the following command if you have not started an external ActiveMQ broker

      features:install activemq-broker

   3. Start service provider
      - run the following commands in the container:

      features:install library-service

   4. Start service consumer
      - run the following commands in the container:

      features:install library-client



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


2. As WS MQ client library jars provided by IBM are not supplied with Talend Enterprise ESB,
   they should be deployed to the container manually.


3. Run the follwing command in the container:

       features:install tesb-jms-transport-enterprise


4. Follow the steps described in a) or b) in this ReadMe file.

/*
 * #%L
 * Service Locator Client for CXF
 * %%
 * Copyright (C) 2011 - 2012 Talend Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.talend.esb.servicelocator.cxf.internal;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientLifeCycleListener;
import org.apache.cxf.endpoint.ClientLifeCycleManager;
import org.apache.cxf.endpoint.ConduitSelectorHolder;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.extension.BusExtension;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.talend.esb.servicelocator.client.SLProperties;
import org.talend.esb.servicelocator.client.SLPropertiesMatcher;

@OsgiServiceProvider
@Singleton
public class ServiceLocatorManager implements BusExtension {

    private LocatorRegistrar locatorRegistrar;

    private LocatorClientEnabler clientEnabler;

    public void listenForAllServers(Bus anotherBus) {
        locatorRegistrar.startListenForServers(anotherBus);
    }

    public void registerServer(Server server, Bus anotherBus) {
        locatorRegistrar.registerServer(server, anotherBus);
    }

    public void registerServer(Server server, SLProperties props, Bus anotherBus) {
        locatorRegistrar.registerServer(server, props, anotherBus);
    }

    public void listenForAllClients(Bus anotherBus) {
        listenForAllClients(anotherBus, null);
    }

    public void listenForAllClients(Bus anotherBus, String selectionStrategy) {
        ClientLifeCycleManager clcm = anotherBus.getExtension(ClientLifeCycleManager.class);
        clcm.registerListener(new ClientLifeCycleListenerForLocator());
    }

    public void enableClient(Client client) {
        enableClient(client, null);
    }

    public void enableClient(final Client client, SLPropertiesMatcher matcher) {
        enableClient(client, matcher, null);
    }

    public void enableClient(final Client client, SLPropertiesMatcher matcher, String selectionStrategy) {
        clientEnabler.enable(client, matcher, selectionStrategy);
    }

    public void enableClient(ConduitSelectorHolder conduitSelectorHolder) {
        enableClient(conduitSelectorHolder, null);
    }

    public void enableClient(final ConduitSelectorHolder conduitSelectorHolder, SLPropertiesMatcher matcher) {
        enableClient(conduitSelectorHolder, matcher, null);
    }

    public void enableClient(final ConduitSelectorHolder conduitSelectorHolder,
            SLPropertiesMatcher matcher,
            String selectionStrategy) {
        clientEnabler.enable(conduitSelectorHolder, matcher, selectionStrategy);
    }

    @Inject
    public void setLocatorRegistrar(LocatorRegistrar locatorRegistrar) {
        this.locatorRegistrar = locatorRegistrar;
    }

    @Inject
    public void setLocatorClientEnabler(LocatorClientEnabler locatorClientEnabler) {
        clientEnabler = locatorClientEnabler;
    }

    @Override
    public Class<?> getRegistrationType() {
        return ServiceLocatorManager.class;
    }

    class ClientLifeCycleListenerForLocator implements ClientLifeCycleListener {

        @Override
        public void clientCreated(Client client) {
            enableClient(client);
        }

        @Override
        public void clientDestroyed(Client client) {
        }
    }
}

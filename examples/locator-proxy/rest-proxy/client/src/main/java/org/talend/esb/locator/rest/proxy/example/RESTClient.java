/*
 * #%L
 * REST Service Locator Proxy :: Example
 * %%
 * Copyright (C) 2011 Talend Inc.
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
package org.talend.esb.locator.rest.proxy.example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.talend.services.esb.locator.rest.v1.LocatorProxyService;
import org.talend.schemas.esb.locator.rest._2011._11.BindingType;
import org.talend.schemas.esb.locator.rest._2011._11.EndpointReferenceList;
import org.talend.schemas.esb.locator.rest._2011._11.RegisterEndpointRequest;
import org.talend.schemas.esb.locator.rest._2011._11.TransportType;

public final class RESTClient {
	
	private RESTClient() throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "META-INF/spring/beans.xml" });
		LocatorProxyService client = (LocatorProxyService) context.getBean("restClient");
		try {
			System.out.println("************************ Register 3 endpoints ****************************");
			registerExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl", "http://services.talend.org/TestEndpoint1");
			registerExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl", "http://services.talend.org/TestEndpoint2");
			registerExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl", "http://services.talend.org/TestEndpoint3");
			System.out.println("************************ Get all endpoints from service ****************************");
			lookupEndpointsExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl");
			System.out.println("************************ Get one random endpoint from service ****************************");
			lookupEndpointExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl");
			lookupEndpointExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl");
			lookupEndpointExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl");
			System.out.println("************************ Unregister 3 endpoints ****************************");
			unregisterExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl", "http://services.talend.org/TestEndpoint1");
			unregisterExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl", "http://services.talend.org/TestEndpoint2");
			unregisterExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl", "http://services.talend.org/TestEndpoint3");
			System.out.println("************************ Get all endpoints from service (Expect 404 error ****************************");
			lookupEndpointsExample(client, "{http://service.proxy.locator.esb.talend.org}LocatorProxyServiceImpl");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws java.lang.Exception {
		new RESTClient();
	}
	
	private void registerExample(LocatorProxyService client, String service, String endpoint) {
		System.out.println("------------------------------");
		System.out.println("Register service with endpoint");
		System.out.println("ServiceName: ".concat(service));
		System.out.println("EndpointURL: ".concat(endpoint));
		RegisterEndpointRequest registerEndpointRequest = new RegisterEndpointRequest();
		registerEndpointRequest.setBinding(BindingType.JAXRS);
		registerEndpointRequest.setTransport(TransportType.HTTPS);
		registerEndpointRequest.setEndpointURL(endpoint);
		registerEndpointRequest.setServiceName(service);
		try {
			client.registerEndpoint(registerEndpointRequest);
			System.out.println("Endpoint registered successfully");
		} catch (ServerWebApplicationException ex) {
			System.err.println(ex.getMessage());
		}
		
	}
	
	private void unregisterExample(LocatorProxyService client, String service, String endpoint) throws UnsupportedEncodingException {
		System.out.println("------------------------------");
		System.out.println("Unregister endpoint");
		System.out.println("ServiceName: ".concat(service));
		System.out.println("EndpointURL: ".concat(endpoint));
		try {
			client.unregisterEndpoint(URLEncoder.encode(service, "UTF-8"), URLEncoder.encode(endpoint, "UTF-8"));	
		} catch (ServerWebApplicationException ex) {
			System.err.println(ex.getResponse().getStatus() + ": " + ex.getMessage());
		}
		
	}
	
	private void lookupEndpointsExample(LocatorProxyService client, String service) throws IOException {
		System.out.println("------------------------------");
		System.out.println("LookupEndpoints");
		try {
			EndpointReferenceList endpointReferenceList = client.lookupEndpoints(URLEncoder.encode(service, "UTF-8"), null);
			if(endpointReferenceList.getReturn().size() > 0)
			{
				for (W3CEndpointReference w3cEndpointReference : endpointReferenceList.getReturn()) {
					System.out.println(w3cEndpointReference.toString());
				}	
			}
		} catch(ServerWebApplicationException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void lookupEndpointExample(LocatorProxyService client, String service) throws IOException {
		System.out.println("------------------------------");
		System.out.println("LookupEndpoint");
		try {
			W3CEndpointReference w3cEndpointReference = client.lookupEndpoint(URLEncoder.encode(service, "UTF-8"), null);
			System.out.println(w3cEndpointReference.toString());
		} catch(ServerWebApplicationException ex) {
			System.out.println(ex.getMessage());
		}
	}

}

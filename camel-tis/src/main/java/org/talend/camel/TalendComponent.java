/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package org.talend.camel;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * <p>
 * Represents the component that manages {@link TalendEndpoint}.
 * </p>
 */
public class TalendComponent extends DefaultComponent {
    public TalendComponent() {
    }

    public TalendComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new TalendEndpoint(uri, remaining, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}

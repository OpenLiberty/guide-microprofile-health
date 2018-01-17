// tag::comment[]
/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::comment[]

package io.openliberty.guides.microprofile;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.openliberty.guides.microprofile.util.InventoryUtil;

// tag::healthCheck[]
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
// end::healthCheck[]

@Health
@ApplicationScoped
@Default
public class InventoryHealth implements HealthCheck {
    // tag::config[]
    @Inject
    InventoryConfig config;
    // end::config[]

    // tag::isHealthy[]
    public boolean isHealthy() {
        if (config.isInMaintenance()) {
            return false;
        }
        String url = InventoryUtil.buildUri("localhost").toString();
        Client client = ClientBuilder.newClient();
        Response response = client.target(url).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200) {
            return false;
        }
        return true;
    }
    // end::isHealthy[]

    // tag::HealthCheckResponse[]
    @Override
    public HealthCheckResponse call() {
        if (!isHealthy()) {
            return HealthCheckResponse.named(InventoryResource.class.getSimpleName()).down().build();
        }
        return HealthCheckResponse.named(InventoryResource.class.getSimpleName()).up().build();
    }
    // end::HealthCheckResponse[]
}

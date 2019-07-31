// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::InventoryReadinessCheck[]
package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Readiness
@ApplicationScoped
public class InventoryReadinessCheck implements HealthCheck {
  @Inject
  InventoryConfig config;

  public boolean isHealthy() {
    if (config.isInMaintenance()) {
      return false;
    }
    try {
      String url = InventoryUtils.buildUrl("http", "localhost",
          Integer.parseInt(System.getProperty("default.http.port")),
          "/system/properties");
      Client client = ClientBuilder.newClient();
      Response response = client.target(url).request(MediaType.APPLICATION_JSON).get();
      if (response.getStatus() != 200) {
        return false;
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public HealthCheckResponse call() {
    if (!isHealthy()) {
      return HealthCheckResponse.named(InventoryResource.class.getSimpleName() + "Readiness")
                                .withData("services", "not available").down()
                                .build();
    }
    return HealthCheckResponse.named(InventoryResource.class.getSimpleName() + "Readiness")
                              .withData("services", "available").up().build();
  }

}
// end::InventoryReadinessCheck[]
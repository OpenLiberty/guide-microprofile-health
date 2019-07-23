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
// tag::InventoryHealth[]
package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;

import org.eclipse.microprofile.health.Liveness;

import io.openliberty.guides.system.SystemResource;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Liveness
@ApplicationScoped
public class InventoryLivenessCheck implements HealthCheck {
 
  @Override
  public HealthCheckResponse call() {
      MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
      long memUsed = memBean.getHeapMemoryUsage().getUsed();
      long memMax = memBean.getHeapMemoryUsage().getMax();

      return HealthCheckResponse.named(InventoryResource.class.getSimpleName())
                                .withData("memory used", memUsed)
                                .withData("memory max", memMax)
                                .state(memUsed < memMax * 0.9).build();
  }
}
// end::InventoryHealth[]

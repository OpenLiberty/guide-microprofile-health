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
// tag::SystemReadinessCheck[]
package io.openliberty.guides.system;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

// tag::Readiness[]
@Readiness
// end::Readiness[]
// tag::ApplicationScoped[]
@ApplicationScoped
// end::ApplicationScoped[]
public class SystemReadinessCheck implements HealthCheck {
  @Override
// tag::HealthCheckResponse[]
  public HealthCheckResponse call() {
    // tag::defaultServer[]
    if (!System.getProperty("wlp.server.name").equals("defaultServer")) {
    // end::defaultServer[]
      // tag::HealthCheckResponse-DOWN[]
      // tag::HealthCheckResponse-named[]
      return HealthCheckResponse.named(SystemResource.class.getSimpleName() + "Readiness")
      // end::HealthCheckResponse-named[]
                                .withData("default server", "not available").down()
                                .build();
      // end::HealthCheckResponse-DOWN[]
    }
    // tag::HealthCheckResponse-UP[]
    return HealthCheckResponse.named(SystemResource.class.getSimpleName() + "Readiness")
                              .withData("default server", "available").up().build();
    // end::HealthCheckResponse-UP[]
  }
// end::HealthCheckResponse[]
}
// end::SystemReadinessCheck[]

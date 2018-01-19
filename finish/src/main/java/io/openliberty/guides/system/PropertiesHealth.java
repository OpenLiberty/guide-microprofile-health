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
package io.openliberty.guides.system;

import javax.enterprise.context.ApplicationScoped;

//tag::healthAPI[]
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
//end::healthAPI[]

// tag::requiredAnnotation[]
@Health
@ApplicationScoped
// end::requiredAnnotation[]

// tag::PropertiesHealth[]
public class PropertiesHealth implements HealthCheck {
  // end::PropertiesHealth[]

  // tag::healthResponse[]
  @Override
  public HealthCheckResponse call() {
    // Simulate health check by checking if the default server is used.
    if (!System.getProperty("wlp.server.name").equals("defaultServer")) {
      return HealthCheckResponse.named(PropertiesResource.class.getSimpleName())
                                .down().build();
    }
    return HealthCheckResponse.named(PropertiesResource.class.getSimpleName())
                              .up().build();
  }
  // end::healthResponse[]
}

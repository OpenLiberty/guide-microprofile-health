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
// tag::HealthIT[]
package it.io.openliberty.guides.health;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import javax.json.JsonArray;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HealthIT {

  private JsonArray servicesStates;
  private static HashMap<String, String> endpointData;

  private String HEALTH_ENDPOINT = "health";
  private String READINESS_ENDPOINT = "health/ready";
  private String LIVENES_ENDPOINT = "health/live";

  @BeforeEach
  public void setup() {
    endpointData = new HashMap<String, String>();
  }

  @Test
  // tag::testIfServicesAreUp[]
  public void testIfServicesAreUp() {
    endpointData.put("SystemResource Readiness Check", "UP");
    endpointData.put("SystemResource Liveness Check", "UP");
    endpointData.put("InventoryResource Readiness Check", "UP");
    endpointData.put("InventoryResource Liveness Check", "UP");

    servicesStates = HealthITUtil.connectToHealthEnpoint(200, HEALTH_ENDPOINT);
    checkStates(endpointData, servicesStates);
  }
  // end::testIfServicesAreUp[]

  @Test
  // tag::testReadiness[]
  public void testReadiness() {
    endpointData.put("SystemResource Readiness Check", "UP");
    endpointData.put("InventoryResource Readiness Check", "UP");

    servicesStates = HealthITUtil.connectToHealthEnpoint(200, READINESS_ENDPOINT);
    checkStates(endpointData, servicesStates);
  }
  // end::testReadiness[]

  @Test
  // tag::testLiveness[]
  public void testLiveness() {
    endpointData.put("SystemResource Liveness Check", "UP");
    endpointData.put("InventoryResource Liveness Check", "UP");

    servicesStates = HealthITUtil.connectToHealthEnpoint(200, LIVENES_ENDPOINT);
    checkStates(endpointData, servicesStates);
  }
  // end::testLiveness[]

  @Test
  // tag::testIfInventoryServiceIsDown[]
  public void testIfInventoryServiceIsDown() {
    endpointData.put("SystemResource Readiness Check", "UP");
    endpointData.put("SystemResource Liveness Check", "UP");
    endpointData.put("InventoryResource Readiness Check", "UP");
    endpointData.put("InventoryResource Liveness Check", "UP");

    servicesStates = HealthITUtil.connectToHealthEnpoint(200, HEALTH_ENDPOINT);
    checkStates(endpointData, servicesStates);

    endpointData.put("InventoryResource Readiness Check", "DOWN");
    HealthITUtil.changeInventoryProperty(HealthITUtil.INV_MAINTENANCE_FALSE,
        HealthITUtil.INV_MAINTENANCE_TRUE);
    servicesStates = HealthITUtil.connectToHealthEnpoint(503, HEALTH_ENDPOINT);
    checkStates(endpointData, servicesStates);
  }
  // end::testIfInventoryServiceIsDown[]

  private void checkStates(HashMap<String, String> testData, JsonArray servStates) {
    testData.forEach((service, expectedState) -> {
      assertEquals(expectedState, HealthITUtil.getActualState(service, servStates),
          "The state of " + service + " service is not matching.");
    });
  }

  @AfterEach
  public void teardown() {
    HealthITUtil.cleanUp();
  }

}
// end::HealthIT[]

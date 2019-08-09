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
// tag::HealthTest[]
package it.io.openliberty.guides.health;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import javax.json.JsonArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HealthTest {

    private JsonArray servicesStates;
    private static HashMap<String, String> endpointData;
 
    private String HEALTH_ENDPOINT = "/health";
    private String READINESS_ENDPOINT = "/health/ready";
    private String LIVENES_ENDPOINT = "/health/live";

    @Before
    public void setup() {
        endpointData = new HashMap<String, String>();
    }

    @Test
    public void testIfServicesAreUp() {
        endpointData.put("SystemResourceReadiness", "UP");
        endpointData.put("SystemResourceLiveness", "UP");
        endpointData.put("InventoryResourceReadiness", "UP");
        endpointData.put("InventoryResourceLiveness", "UP");

        servicesStates = HealthTestUtil.connectToHealthEnpoint(200, HEALTH_ENDPOINT);
        checkStates(endpointData, servicesStates);
    }

    @Test
    public void testReadiness() {
        endpointData.put("SystemResourceReadiness", "UP");
        endpointData.put("InventoryResourceReadiness", "UP");

        servicesStates = HealthTestUtil.connectToHealthEnpoint(200, READINESS_ENDPOINT);
        checkStates(endpointData, servicesStates);
    }

    @Test
    public void testLiveness() {
        endpointData.put("SystemResourceLiveness", "UP");
        endpointData.put("InventoryResourceLiveness", "UP");

        servicesStates = HealthTestUtil.connectToHealthEnpoint(200, LIVENES_ENDPOINT);
        checkStates(endpointData, servicesStates);
    }

    @Test
    public void testIfInventoryServiceIsDown() {
        endpointData.put("SystemResourceReadiness", "UP");
        endpointData.put("SystemResourceLiveness", "UP");
        endpointData.put("InventoryResourceReadiness", "UP");
        endpointData.put("InventoryResourceLiveness", "UP");

        servicesStates = HealthTestUtil.connectToHealthEnpoint(200, HEALTH_ENDPOINT);
        checkStates(endpointData, servicesStates);

        endpointData.put("InventoryResourceReadiness", "DOWN");
        HealthTestUtil.changeInventoryProperty(HealthTestUtil.INV_MAINTENANCE_FALSE, 
                                               HealthTestUtil.INV_MAINTENANCE_TRUE);
        servicesStates = HealthTestUtil.connectToHealthEnpoint(503, HEALTH_ENDPOINT);
        checkStates(endpointData, servicesStates);
   }

    private void checkStates(HashMap<String, String> testData, JsonArray servStates) {
        testData.forEach((service, expectedState) -> {
            assertEquals("The state of " + service + " service is not matching.", 
                         expectedState, 
                         HealthTestUtil.getActualState(service, servStates));
        });
    }

    @After
    public void teardown() {
        HealthTestUtil.cleanUp();
    }

}
// end::HealthTest[]

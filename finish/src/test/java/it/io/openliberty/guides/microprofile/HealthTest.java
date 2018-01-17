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
package it.io.openliberty.guides.microprofile;

import static org.junit.Assert.*;
import java.util.HashMap;
import javax.json.JsonArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HealthTest {
    // tag::data[]
    private JsonArray servicesStates;
    private HashMap<String, String> dataWhenServicesUP, dataWhenInventoryDown;

    @Before
    public void setup() {
        dataWhenServicesUP = new HashMap<String, String>();
        dataWhenInventoryDown = new HashMap<String, String>();

        dataWhenServicesUP.put("PropertiesResource", "UP");
        dataWhenServicesUP.put("InventoryResource", "UP");

        dataWhenInventoryDown.put("PropertiesResource", "UP");
        dataWhenInventoryDown.put("InventoryResource", "DOWN");
    }
    // end::data[]

    // tag::testIfServicesStatesUp[]
    @Test
    public void testIfServicesStatesUp() {
        servicesStates = HealthTestUtil.connectToHealthEnpoint(200);
        checkServicesStates(dataWhenServicesUP, servicesStates);
    }
    // end::testIfServicesStatesUp[]

    // tag::testIfInventoryTemporarilyDown[]
    @Test
    public void testIfInventoryTemporarilyDown() {
        servicesStates = HealthTestUtil.connectToHealthEnpoint(200);
        checkServicesStates(dataWhenServicesUP, servicesStates);
        HealthTestUtil.changeInventoryProperty("inMaintenance\": false", "inMaintenance\": true");
        servicesStates = HealthTestUtil.connectToHealthEnpoint(503);
        checkServicesStates(dataWhenInventoryDown, servicesStates);
    }
    // end::testIfInventoryTemporarilyDown[]

    // tag::checkServicesStates[]
    private void checkServicesStates(HashMap<String, String> testData, JsonArray servicesStates) {
        testData.forEach((service, expectedState) -> {
            assertEquals("The state of " + service + " service is not matching the ",
                          expectedState, 
                          HealthTestUtil.getActualState(service, servicesStates));
        });

    }
    // end::checkServicesStates[]

    // tag::After[]
    @After
    public void teardown() {
        HealthTestUtil.cleanUp();
    }
    // end::After[]

}

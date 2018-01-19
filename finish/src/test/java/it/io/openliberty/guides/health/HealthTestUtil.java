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
package it.io.openliberty.guides.health;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;

public class HealthTestUtil {

  // tag::connectToHealthEnpoint[]
  public static JsonArray connectToHealthEnpoint(int expectedResponseCode) {
    String url = "http://localhost:9080/health";
    Client client = ClientBuilder.newClient().register(JsrJsonpProvider.class);
    Response response = client.target(url).request().get();
    assertEquals("Response code is not matching " + url, expectedResponseCode,
                 response.getStatus());
    JsonArray servicesStates = response.readEntity(JsonObject.class)
                                       .getJsonArray("checks");
    response.close();
    client.close();
    return servicesStates;
  }
  // end::connectToHealthEnpoint[]

  // tag::getActualState[]
  public static String getActualState(String service,
      JsonArray servicesStates) {
    String state = "";
    for (Object obj : servicesStates) {
      if (obj instanceof JsonObject) {
        if (service.equals(((JsonObject) obj).getString("name"))) {
          state = ((JsonObject) obj).getString("state");
        }
      }
    }
    return state;
  }
  // end::getActualState[]

  // tag::changeInventoryProperty[]
  public static void changeInventoryProperty(String oldLine, String newLine) {
    try {
      String fileName = System.getProperty("user.dir").split("target")[0]
          + "/resource/CustomConfigSource.json";
      BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
      String line = "";
      String oldContent = "", newContent = "";
      while ((line = reader.readLine()) != null) {
        oldContent += line + "\r\n";
      }
      reader.close();
      newContent = oldContent.replaceAll(oldLine, newLine);
      FileWriter writer = new FileWriter(fileName);
      writer.write(newContent);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // end::changeInventoryProperty[]

  // tag::cleanUp[]
  public static void cleanUp() {
    changeInventoryProperty(HealthTest.INV_MAINTENANCE + "\": true",
                            HealthTest.INV_MAINTENANCE + "\": false");
  }
  // end::cleanUp[]

}

// tag::copyright[]
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
// end::copyright[]

// tag::InventoryConfig[]
package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.inject.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.openliberty.guides.config.Email;

@ApplicationScoped
public class InventoryConfig {

  @Inject
  @ConfigProperty(name = "io_openliberty_guides_port_number")
  private int portNumber;

  @Inject
  @ConfigProperty(name = "io_openliberty_guides_inventory_inMaintenance")
  private Provider<Boolean> inMaintenance;

  @Inject
  @ConfigProperty(name = "io_openliberty_guides_email")
  private Provider<Email> email;

  public int getPortNumber() {
    return portNumber;
  }

  public boolean isInMaintenance() {
    return inMaintenance.get();
  }

  public Email getEmail() {
    return email.get();
  }

}
// end::InventoryConfig[]

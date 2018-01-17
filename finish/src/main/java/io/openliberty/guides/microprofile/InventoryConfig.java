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
import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class InventoryConfig {

    // tag::configProperty[]
    @Inject
    @ConfigProperty(name = "io_openliberty_guides_inventory_inMaintenance")
    Provider<Boolean> inMaintenance;
    // end::configProperty[]

    // tag::isInMaintenance[]
    public boolean isInMaintenance() {
        return inMaintenance.get();
    }
    // end::isInMaintenance[]
}

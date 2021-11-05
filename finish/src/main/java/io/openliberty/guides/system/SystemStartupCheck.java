// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::SystemStartupCheck[]
package io.openliberty.guides.system;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Startup;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

// tag::Startup[]
@Startup
// end::Startup[]
@ApplicationScoped
public class SystemStartupCheck implements HealthCheck {

    private static final String STARTUP_CHECK = SystemResource.class.getSimpleName()
                                               + " Startup Check";

    @Override
    public HealthCheckResponse call() {
        OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean)
        ManagementFactory.getOperatingSystemMXBean();
        getCpuUsage();
        if (bean.getProcessCpuLoad() < 0.9) {
           return HealthCheckResponse.up(STARTUP_CHECK);
        } else {
           return HealthCheckResponse.down(STARTUP_CHECK);
        }
    }
}

// end::SystemStartupCheck[]

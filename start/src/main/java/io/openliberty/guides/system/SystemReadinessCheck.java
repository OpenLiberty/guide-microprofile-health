package io.openliberty.guides.system;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Readiness
@ApplicationScoped
public class SystemReadinessCheck implements HealthCheck {

  private static final String readinessCheck = SystemResource.class.getSimpleName()
                                               + " Readiness Check";

  @Override
  public HealthCheckResponse call() {
    if (!System.getProperty("wlp.server.name").equals("defaultServer")) {
      return HealthCheckResponse.down(readinessCheck);
    }
    return HealthCheckResponse.up(readinessCheck);
  }
}
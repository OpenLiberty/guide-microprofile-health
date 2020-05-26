package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Readiness
@ApplicationScoped
public class InventoryReadinessCheck implements HealthCheck {

  private static final String readinessCheck = InventoryResource.class.getSimpleName()
                                               + " Readiness Check";

  @Inject
  InventoryConfig config;

  public boolean isHealthy() {
    if (config.isInMaintenance()) {
      return false;
    }
    try {
      String url = InventoryUtils.buildUrl("http", "localhost", config.getPortNumber(),
          "/system/properties");
      Client client = ClientBuilder.newClient();
      Response response = client.target(url).request(MediaType.APPLICATION_JSON).get();
      if (response.getStatus() != 200) {
        return false;
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public HealthCheckResponse call() {
    if (!isHealthy()) {
      return HealthCheckResponse
          .down(readinessCheck);
    }
    return HealthCheckResponse
        .up(readinessCheck);
  }

}
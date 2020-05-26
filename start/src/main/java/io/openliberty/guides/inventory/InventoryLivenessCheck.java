package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;

import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;

import org.eclipse.microprofile.health.Liveness;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Liveness
@ApplicationScoped
public class InventoryLivenessCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
      MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
      long memUsed = memBean.getHeapMemoryUsage().getUsed();
      long memMax = memBean.getHeapMemoryUsage().getMax();

      return HealthCheckResponse.named(InventoryResource.class.getSimpleName() + " Liveness Check")
                                .withData("memory used", memUsed)
                                .withData("memory max", memMax)
                                .state(memUsed < memMax * 0.9).build();
  }
}
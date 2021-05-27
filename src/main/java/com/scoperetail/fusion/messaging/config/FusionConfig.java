/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fusion")
@Data
public class FusionConfig {
  private final List<Broker> brokers = new ArrayList<>();
  private final List<RetryPolicy> retryPolicies = new ArrayList<>();
  private final List<UseCaseConfig> usecases = new ArrayList<>();
  private Credentials credentials;
  private TemplateEngine templateEngine;

  public enum TemplateEngine {
    FTL, VELOCITY
  }
}

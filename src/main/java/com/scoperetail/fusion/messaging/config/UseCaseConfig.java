/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UseCaseConfig {
  private String activeConfig;
  private final List<Config> configs = new ArrayList<>();
  private String name;
  private String version;
}

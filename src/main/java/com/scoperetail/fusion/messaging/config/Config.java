/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Config {
  private final List<Adapter> adapters = new ArrayList<>();
  private String name;
}

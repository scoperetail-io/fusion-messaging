/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

import lombok.Data;

@Data
public class RetryPolicy {
  private PolicyType policyType;
  private Short maxAttempt;
  private Integer backoffMS;
  private String type;

  enum PolicyType {
    REALTIME,
    OFFLINE,
  }
}

package com.scoperetail.fusion.messaging.config;

/*-
 * *****
 * fusion-messaging
 * -----
 * Copyright (C) 2018 - 2021 Scope Retail Systems Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

public class AmqpRedeliveryPolicy {
  private Double backOffMultiplier;
  private Integer maxDeliveries;
  private String queueNameRegex;
  private Long redeliveryDelay;
  private Boolean useExponentialBackOff;
  private Long maxDeliveryDelay;
  private Integer initialRedeliveryDelay;

  public AmqpRedeliveryPolicy() {
    backOffMultiplier = 1.0;
    maxDeliveries = 3;
    queueNameRegex = "*";
    redeliveryDelay = 3000l;
    useExponentialBackOff = true;
    maxDeliveryDelay = 3600000l;
    initialRedeliveryDelay = 0;
  }

  public Double getBackOffMultiplier() {
    return backOffMultiplier;
  }

  public void setBackOffMultiplier(final Double backOffMultiplier) {
    this.backOffMultiplier = backOffMultiplier;
  }

  public Integer getMaxDeliveries() {
    return maxDeliveries;
  }

  public void setMaxDeliveries(final Integer maxDeliveries) {
    this.maxDeliveries = maxDeliveries;
  }

  public String getQueueNameRegex() {
    return queueNameRegex;
  }

  public void setQueueNameRegex(final String queueNameRegex) {
    this.queueNameRegex = queueNameRegex;
  }

  public Long getRedeliveryDelay() {
    return redeliveryDelay;
  }

  public void setRedeliveryDelay(final Long redeliveryDelay) {
    this.redeliveryDelay = redeliveryDelay;
  }

  public Boolean getUseExponentialBackOff() {
    return useExponentialBackOff;
  }

  public void setUseExponentialBackOff(final Boolean useExponentialBackOff) {
    this.useExponentialBackOff = useExponentialBackOff;
  }

  public Long getMaxDeliveryDelay() {
    return maxDeliveryDelay;
  }

  public void setMaxDeliveryDelay(final Long maxDeliveryDelay) {
    this.maxDeliveryDelay = maxDeliveryDelay;
  }

  public Integer getInitialRedeliveryDelay() {
    return initialRedeliveryDelay;
  }

  public void setInitialRedeliveryDelay(final Integer initialRedeliveryDelay) {
    this.initialRedeliveryDelay = initialRedeliveryDelay;
  }
}

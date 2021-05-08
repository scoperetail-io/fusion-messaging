/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

import com.scoperetail.fusion.messaging.adapter.JmsProvider;
import lombok.Data;

@Data
public class Broker {
  private String channel;
  private String hostUrl;
  private String brokerId;
  private JmsProvider jmsProvider;
  private String queueManagerName;
  private Integer sendSessionCacheSize;
  private String userName;
  private Owner owner;

  public enum Owner {
    SCOPE,
    MCS,
    GIF
  }
}

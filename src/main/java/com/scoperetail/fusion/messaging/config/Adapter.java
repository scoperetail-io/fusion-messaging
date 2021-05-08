/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;
import lombok.Data;

@Data
public class Adapter {
  public enum AdapterType {
    INBOUND,
    OUTBOUND
  }

  public enum TransportType {
    JMS,
    REST
  }

  private AdapterType adapterType;
  private String boQuename;
  // JMS
  private String brokerId;
  private String errorQueName;
  // REST
  private String hostName;
  private String methodType;
  private Integer port;
  private String protocol;
  private String queueName;
  private String requestBodyTemplate;
  private String requestHeaderTemplate;
  private TransportType trasnportType;

  private String uriTemplate;

  private UsecaseResult usecaseResult;
}

/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.config;

import static com.scoperetail.fusion.messaging.config.Adapter.TransformationType.NONE;

import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;

import lombok.Data;

@Data
public class Adapter {

	public Adapter() {
		transformationType = NONE;
	}

	public enum AdapterType {
		INBOUND, OUTBOUND
	}

	public enum TransportType {
		JMS, REST
	}

  public enum TransformationType {
    DOMAIN_EVENT_FTL_TRANSFORMER, DOMAIN_EVENT_VELOCITY_TRANSFORMER, FTL_TEMPLATE_TRANSFORMER, VELOCITY_TEMPLATE_TRANSFORMER, NONE
  }

	public TransformationType transformationType;
	private AdapterType adapterType;
	private String boQuename;
	// JMS
	private String template;
	private String brokerId;
	private String errorQueName;
	// REST
	private String hostName;
	private String methodType;
	private Integer port;
	private String protocol;
	private String queueName;
	private String requestBodyTemplate;
	private String templateCustomizer;
	private String requestHeaderTemplate;
	private TransportType trasnportType;

	private String uriTemplate;

	private UsecaseResult usecaseResult;
}

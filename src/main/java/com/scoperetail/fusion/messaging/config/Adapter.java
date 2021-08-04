/* ScopeRetail (C)2021 */
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

import static com.scoperetail.fusion.messaging.config.Adapter.TransformationType.NONE;

import com.scoperetail.fusion.messaging.application.port.in.UsecaseResult;

import lombok.Data;

@Data
public class Adapter {

	public Adapter() {
		transformationType = NONE;
	}

	public enum AdapterType {
		INBOUND, OUTBOUND,
	}

	public enum TransportType {
		JMS, REST, MAIL, KAFKA,
	}

	public enum TransformationType {
		DOMAIN_EVENT_FTL_TRANSFORMER, DOMAIN_EVENT_VELOCITY_TRANSFORMER, FTL_TEMPLATE_TRANSFORMER,
		VELOCITY_TEMPLATE_TRANSFORMER, NONE
	}

	public TransformationType transformationType;
	private AdapterType adapterType;
	private UsecaseResult usecaseResult;
	// JMS
	private String boQuename;
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
	// MAIL
	private String hostId;
	private String fromTemplate;
	private String replyToTemplate;
	private String toTemplate;
	private String ccTemplate;
	private String bccTemplate;
	private String sentDateTemplate;
	private String subjectTemplate;
	private String textTemplate;
	// KAFKA
	private String topicName;
}

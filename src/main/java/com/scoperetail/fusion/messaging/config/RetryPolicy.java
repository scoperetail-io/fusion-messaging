package com.scoperetail.fusion.messaging.config;

import lombok.Data;

@Data
public class RetryPolicy {
	private String policyType;
	private Short maxAttempt;
	private Integer backoffMS;
	private String type;
}

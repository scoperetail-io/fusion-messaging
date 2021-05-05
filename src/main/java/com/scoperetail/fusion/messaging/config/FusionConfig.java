package com.scoperetail.fusion.messaging.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "fusion")
@Data
public class FusionConfig {
	private List<Broker> brokers = new ArrayList<>();
	private List<RetryPolicy> retryPolicies = new ArrayList<>();;
	private List<UseCase> usecases = new ArrayList<>();;
}
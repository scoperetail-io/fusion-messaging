package com.scoperetail.fusion.messaging.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UseCaseConfig {
	private String name;
	private String version;
	private String activeConfig;
	private List<Config> configs = new ArrayList<>();
}

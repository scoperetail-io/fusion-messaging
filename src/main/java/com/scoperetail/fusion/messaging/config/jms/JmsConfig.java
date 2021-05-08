package com.scoperetail.fusion.messaging.config.jms;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = { JmsAutoConfiguration.class })
public class JmsConfig {

}

package com.scoperetail.fusion.messaging.adapter.out.messaging.jms;

import org.springframework.jms.core.JmsTemplate;

public interface MessageRouter {

	boolean send(String brokerId, String queue, String payload);

	void registerTemplate(String brokerId, JmsTemplate jmsTemplate);

}

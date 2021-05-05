package com.scoperetail.fusion.messaging.adapter.out.messaging.jms;

import org.springframework.jms.core.JmsTemplate;

public interface MessageRouter {

	boolean send(String brokerId, String queue, String payload);

	default boolean auditAndSend(String brokerId, String queue, String payload) {
		send(brokerId, queue, payload);
		send(brokerId, "AUDTI.IN", payload);
		return true;
	}

	void registerTemplate(String brokerId, JmsTemplate jmsTemplate);

}

/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms;

import org.springframework.jms.core.JmsTemplate;

public interface MessageRouterSender {

	void send(String brokerId, String queue, String payload);

	void registerTemplate(String brokerId, JmsTemplate jmsTemplate);
}

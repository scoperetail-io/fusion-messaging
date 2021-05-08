/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterSender;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageRouterSenderService implements MessageRouterSender {

	private final Map<String, JmsTemplate> jmsTemplateByBrokerIdMap = new HashMap<>(1);

	@Override
	public void send(final String brokerId, final String queue, final String payload) {
		final JmsTemplate jmsTemplate = jmsTemplateByBrokerIdMap.get(brokerId);
		jmsTemplate.convertAndSend(queue, payload);
	}

	@Override
	public void registerTemplate(final String brokerId, final JmsTemplate jmsTemplate) {
		jmsTemplateByBrokerIdMap.put(brokerId, jmsTemplate);
	}
}

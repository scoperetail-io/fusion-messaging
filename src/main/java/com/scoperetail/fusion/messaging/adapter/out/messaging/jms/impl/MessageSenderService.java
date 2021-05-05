package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageSenderService implements MessageRouter {

	private Map<String, JmsTemplate> jmsTemplateByBrokerIdMap = new HashMap<>(1);

	@Override
	public boolean auditAndSend(String brokerId, String queue, String payload) {
		JmsTemplate jmsTemplate = jmsTemplateByBrokerIdMap.get(brokerId);
		jmsTemplate.convertAndSend(queue, payload);
		jmsTemplate.convertAndSend("AUDIT.IN", payload);
		return true;
	}

	@Override
	public boolean send(String brokerId, String queue, String payload) {
		return false;
	}

	@Override
	public void registerTemplate(String brokerId, JmsTemplate jmsTemplate) {
		jmsTemplateByBrokerIdMap.put(brokerId, jmsTemplate);
	}

}

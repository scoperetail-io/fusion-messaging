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

	//private ApplicationContext applicationContext;
	private Map<String, JmsTemplate> jmsTemplateByBrokerIdMap = new HashMap<>(1);

	//private Map<String, MessageListener<String>> listenersByQueueMap = new HashMap<>(1);

	@Override
	public boolean send(String brokerId, String queue, String payload) {
		JmsTemplate jmsTemplate = jmsTemplateByBrokerIdMap.get(brokerId);
		jmsTemplate.convertAndSend(queue, payload);
		return true;
	}

	@Override
	public void registerTemplate(String brokerId, JmsTemplate jmsTemplate) {
		jmsTemplateByBrokerIdMap.put(brokerId, jmsTemplate);
	}

}

package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

import javax.jms.Queue;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageReceiver;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.RouterHelper;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageRouterReceiverService implements MessageRouterReceiver {

	RouterHelper routerHelper;

	@Override
	public void registerListener(String brokerId, String queueName, MessageListener<String> messageListener) {
		final DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
		dmlc.setConnectionFactory(routerHelper.getConnectionFactory(brokerId));
		dmlc.setMessageListener(new MessageReceiver(queueName, brokerId, messageListener));
		Queue queue = routerHelper.getQueue(queueName);
		dmlc.setDestination(queue);
		dmlc.setCacheLevelName("CACHE_CONSUMER");
		dmlc.setConcurrency("5-10");

		dmlc.setAutoStartup(true);
		dmlc.setSessionTransacted(false);
		// start calls initialize
		dmlc.afterPropertiesSet();
		dmlc.start();
	}
}

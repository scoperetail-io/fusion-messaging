/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

import javax.jms.Queue;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageReceiver;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.RouterHelper;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MessageRouterReceiverService implements MessageRouterReceiver {

	RouterHelper routerHelper;

	@Override
	public void registerListener(final String brokerId, final String queueName,
			final MessageListener<String> messageListener) {
		final DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer.setConnectionFactory(routerHelper.getConnectionFactory(brokerId));
		defaultMessageListenerContainer.setMessageListener(new MessageReceiver(queueName, brokerId, messageListener));
		final Queue queue = routerHelper.getQueue(queueName);
		defaultMessageListenerContainer.setDestination(queue);
		defaultMessageListenerContainer.setCacheLevelName("CACHE_CONSUMER");
		defaultMessageListenerContainer.setConcurrency("5-10");
		defaultMessageListenerContainer.setAutoStartup(true);
		defaultMessageListenerContainer.setSessionTransacted(false);
		defaultMessageListenerContainer.afterPropertiesSet();
		defaultMessageListenerContainer.start();
		log.info("Initialized listener on queue: {} for brokerId:{}", queueName, brokerId);
	}
}

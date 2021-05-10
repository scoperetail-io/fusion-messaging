/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageReceiver;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.RouterHelper;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Queue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MessageRouterReceiverService implements MessageRouterReceiver {

  private RouterHelper routerHelper;
  private final Map<Endpoint, MessageReceiver> dmlcByBrokerAndQueueMap = new HashMap<>(1);

  @Override
  public void registerListener(
      final String brokerId,
      final String queueName,
      final MessageListener<String> messageListener) {
    final Endpoint endpoint = new Endpoint(brokerId, queueName);
    if (!dmlcByBrokerAndQueueMap.containsKey(endpoint)) {
      final DefaultMessageListenerContainer defaultMessageListenerContainer =
          new DefaultMessageListenerContainer();
      defaultMessageListenerContainer.setConnectionFactory(
          routerHelper.getConnectionFactory(brokerId));
      final MessageReceiver messageReceiver =
          new MessageReceiver(queueName, brokerId, messageListener);
      defaultMessageListenerContainer.setMessageListener(messageReceiver);
      final Queue queue = routerHelper.getQueue(queueName);
      defaultMessageListenerContainer.setDestination(queue);
      defaultMessageListenerContainer.setCacheLevelName("CACHE_CONSUMER");
      defaultMessageListenerContainer.setConcurrency("5-10");
      defaultMessageListenerContainer.setAutoStartup(true);
      defaultMessageListenerContainer.setSessionTransacted(false);
      defaultMessageListenerContainer.afterPropertiesSet();
      defaultMessageListenerContainer.start();
      dmlcByBrokerAndQueueMap.put(endpoint, messageReceiver);
    } else {
      final MessageReceiver messageReceiver = dmlcByBrokerAndQueueMap.get(endpoint);
      messageReceiver.registerListener(messageListener);
    }
    log.info(
        "{} is listening on Queue: {} for BrokerId: {}",
        messageListener.getClass().getSimpleName(),
        queueName,
        brokerId);
  }

  @Data
  @AllArgsConstructor
  class Endpoint {
    String brokerId;
    String queueName;
  }
}

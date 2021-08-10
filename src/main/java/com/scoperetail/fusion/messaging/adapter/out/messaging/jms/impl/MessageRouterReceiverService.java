/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

import java.util.HashMap;
import java.util.Map;
import javax.jms.Queue;
import javax.jms.Session;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

/*-
 * *****
 * fusion-messaging
 * -----
 * Copyright (C) 2018 - 2021 Scope Retail Systems Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageReceiver;
import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.RouterHelper;
import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterReceiver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
      defaultMessageListenerContainer.setSessionTransacted(true);
      defaultMessageListenerContainer.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
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

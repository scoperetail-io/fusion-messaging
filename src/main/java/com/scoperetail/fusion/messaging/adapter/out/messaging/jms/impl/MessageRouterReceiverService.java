/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

import java.util.HashMap;
import java.util.Map;
import javax.jms.Queue;
import javax.jms.Session;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;
import com.scoperetail.fusion.config.FusionConfig;

/*-
 * *****
 * fusion-messaging
 * -----
 * Copyright (C) 2018 - 2021 Scope Retail Systems Inc.
 * -----
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
  private final FusionConfig fusionConfig;

  @Override
  public void registerListener(
      final String brokerId,
      final String queueName,
      final String readConcurrency,
      final MessageListener<String> messageListener) {
    final Endpoint endpoint = new Endpoint(brokerId, queueName);
    if (!dmlcByBrokerAndQueueMap.containsKey(endpoint)) {
      final DefaultMessageListenerContainer defaultMessageListenerContainer =
          new DefaultMessageListenerContainer();
      defaultMessageListenerContainer.setConnectionFactory(
          routerHelper.getConnectionFactory(brokerId));
      final MessageReceiver messageReceiver =
          new MessageReceiver(fusionConfig, queueName, brokerId, messageListener);
      defaultMessageListenerContainer.setMessageListener(messageReceiver);
      final Queue queue = routerHelper.getQueue(queueName);
      defaultMessageListenerContainer.setDestination(queue);
      defaultMessageListenerContainer.setCacheLevelName("CACHE_CONSUMER");
      defaultMessageListenerContainer.setConcurrency(readConcurrency);
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

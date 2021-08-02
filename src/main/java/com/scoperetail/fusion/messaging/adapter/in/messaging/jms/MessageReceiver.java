/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

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

import static com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult.DISCARD;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageReceiver implements SessionAwareMessageListener<Message> {
  private static final String JMSXDELCOUNT = "JMSXDeliveryCount";
  private final String queue;
  private final String brokerId;
  private final List<MessageListener<String>> messageListeners = new ArrayList<>(1);

  public MessageReceiver(
      final String queue, final String brokerId, final MessageListener<String> messageListener) {
    this.queue = queue;
    this.brokerId = brokerId;
    registerListener(messageListener);
  }

  @Override
  public void onMessage(final Message message, final Session session) throws JMSException {
    final UUID corelationId = UUID.randomUUID();
    final SimpleMessageConverter smc = new SimpleMessageConverter();
    final String strMessage = String.valueOf(smc.fromMessage(message));

    log.info(
        "Message received CorelationId: {} Queue: {} for BrokerId: {} Message: {}",
        corelationId,
        queue,
        brokerId,
        strMessage);
    int noOfListenersHandledMessage = 0;
    for (final MessageListener<String> messageListener : messageListeners) {
      TaskResult taskResult = DISCARD;
      try {
        if (messageListener.canHandle(strMessage)) {
          noOfListenersHandledMessage++;
          taskResult = messageListener.doTask(strMessage);
          if (DISCARD.equals(taskResult)) {
            log.error(
                "Sending message to validation failure handler for broker: {}, Queue: {} , Message: {} ",
                brokerId,
                queue,
                strMessage);
            messageListener.handleValidationFailure(strMessage);
          }
          log.info("Message handling status for corelationId: {} is: {}", corelationId, taskResult);
        }
      } catch (final Throwable e) {
        log.info("Message handling status for corelationId: {} is: {}", corelationId, taskResult);
        log.error(
            "=================ERROR MESSAGE DUMP START(corelationId: {})=========================",
            corelationId);
        log.error(
            "Unable to handle error message having corelationId: {} Message: {} due to exception: {}",
            corelationId,
            message,
            e);
        log.error(
            "=================ERROR MESSAGE DUMP END(corelationId: {})=========================",
            corelationId);
        final long reDeliveryCount = message.getLongProperty(JMSXDELCOUNT);

        if (reDeliveryCount >= 3) {
          log.error("Redelivery count exceeded for corelationId: {}", corelationId);
          messageListener.handleFailure(strMessage);
        } else {
          throw new JMSException(e.getMessage());
        }
      }
    }
    if (noOfListenersHandledMessage == 0) {
      log.warn("None of the listeners handled the message having corelationId: {}", corelationId);
      log.warn(
          "=================INVALID MESSAGE DUMP START(corelationId: {})=========================",
          corelationId);
      log.warn(
          "Unable to handle invalid message having corelationId: {}  Message: {} ",
          corelationId,
          message);
      log.warn(
          "=================INVALID MESSAGE DUMP END(corelationId: {})=========================",
          corelationId);
    }
  }

  public void registerListener(final MessageListener<String> messageListener) {
    messageListeners.add(messageListener);
  }
}

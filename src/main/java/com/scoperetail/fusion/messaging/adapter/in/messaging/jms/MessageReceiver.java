/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

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

import static com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult.DISCARD;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import com.scoperetail.fusion.config.AmqpRedeliveryPolicy;
import com.scoperetail.fusion.config.Broker;
import com.scoperetail.fusion.config.FusionConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageReceiver implements SessionAwareMessageListener<Message> {
  private static final String JMSXDELCOUNT = "JMSXDeliveryCount";
  private final String queue;
  private final String brokerId;
  private final List<MessageListener<String>> messageListeners = new ArrayList<>(1);
  private final FusionConfig fusionConfig;

  public MessageReceiver(
      final FusionConfig fusionConfig,
      final String queue,
      final String brokerId,
      final MessageListener<String> messageListener) {
    this.fusionConfig = fusionConfig;
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
          messageListener.auditMessage(strMessage);
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
        final Optional<Broker> optionalBroker = fusionConfig.getBroker(brokerId);
        if (optionalBroker.isPresent()) {
          final Broker broker = optionalBroker.get();
          final AmqpRedeliveryPolicy amqpRedeliveryPolicy = broker.getAmqpRedeliveryPolicy();
          final Integer maxDeliveries = amqpRedeliveryPolicy.getMaxDeliveries();
          if (maxDeliveries != -1 && reDeliveryCount >= maxDeliveries) {
            log.error("Redelivery count exceeded for corelationId: {}", corelationId);
            messageListener.handleFailure(strMessage);
          } else {
            throw new JMSException(e.getMessage());
          }
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

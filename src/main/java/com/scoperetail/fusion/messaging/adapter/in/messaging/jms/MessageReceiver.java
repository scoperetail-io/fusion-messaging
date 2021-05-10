/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

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

	private final String queue;
	private final String brokerId;
	private final List<MessageListener<String>> messageListeners = new ArrayList<>(1);

	public MessageReceiver(final String queue, final String brokerId, final MessageListener<String> messageListener) {
		this.queue = queue;
		this.brokerId = brokerId;
		registerListener(messageListener);
	}

	@Override
	public void onMessage(final Message message, final Session session) throws JMSException {
		final UUID corelationId = UUID.randomUUID();
		final SimpleMessageConverter smc = new SimpleMessageConverter();
		final String strMessage = String.valueOf(smc.fromMessage(message));
		log.info("Message received CorelationId: {} Queue: {} for BrokerId: {} Message: {}", corelationId, queue,
				brokerId, strMessage);
		int noOfListenersHandledMessage = 0;
		for (final MessageListener<String> messageListener : messageListeners) {
			TaskResult taskResult = DISCARD;
			try {
				if (messageListener.canHandle(strMessage)) {
					noOfListenersHandledMessage++;
					taskResult = messageListener.doTask(strMessage);
					log.info("Message handling status for corelationId: {} is: {}", corelationId, taskResult);
				}
			} catch (final Throwable e) {
				log.info("Message handling status for corelationId: {} is: {}", corelationId, taskResult);
				log.error("=================ERROR MESSAGE DUMP START(corelationId: {})=========================",
						corelationId);
				log.error("Unable to handle error message having corelationId: {} Message: {} due to exception: {}",
						corelationId, message, e);
				log.error("=================ERROR MESSAGE DUMP END(corelationId: {})=========================",
						corelationId);
			}
		}
		if (noOfListenersHandledMessage == 0) {
			log.error("None of the listeners handled the message having corelationId: {}", corelationId);
			log.error("=================INVALID MESSAGE DUMP START(corelationId: {})=========================",
					corelationId);
			log.error("Unable to handle invalid message having corelationId: {}  Message: {} ", corelationId, message);
			log.error("=================INVALID MESSAGE DUMP END(corelationId: {})=========================",
					corelationId);
		}
	}

	public void registerListener(final MessageListener<String> messageListener) {
		messageListeners.add(messageListener);
	}
}

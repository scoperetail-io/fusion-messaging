/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

import static com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult.DISCARD;

import java.util.ArrayList;
import java.util.List;

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
		log.info("Message received on queue: {} for brokerId: {}", queue, brokerId);
		final SimpleMessageConverter smc = new SimpleMessageConverter();
		final String strMessage = String.valueOf(smc.fromMessage(message));
		TaskResult taskResult = DISCARD;
		int noOfListenersHandledMessage = 0;
		for (final MessageListener<String> messageListener : messageListeners) {
			try {
				if (messageListener.canHandle(strMessage)) {
					noOfListenersHandledMessage++;
					taskResult = messageListener.doTask(strMessage);
					log.info("Message handling status is: {}", taskResult);
				}
			} catch (final Throwable e) {
				log.error("=================ERROR MESSAGE DUMP START=========================");
				log.error("Unable to handle error message: {} due to exception:{}", message, e);
				log.error("=================ERROR MESSAGE DUMP END=========================");
			}
		}
		if (noOfListenersHandledMessage == 0) {
			log.error("None of the listeners handled the message");
			log.error("=================INVALID MESSAGE DUMP START=========================");
			log.error("Unable to handle invalid message: {} ", message);
			log.error("=================INVALID MESSAGE DUMP END=========================");
		}
	}

	public void registerListener(final MessageListener<String> messageListener) {
		messageListeners.add(messageListener);
	}
}

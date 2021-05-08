/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

import static com.scoperetail.fusion.messaging.adapter.in.messaging.jms.TaskResult.DISCARD;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class MessageReceiver implements SessionAwareMessageListener<Message> {

	private String queue;
	private String brokerId;
	private MessageListener<String> messageListener;

	@Override
	public void onMessage(final Message message, final Session session) throws JMSException {
		log.info("Message received on queue: {} for brokerId: {}", queue, brokerId);
		final SimpleMessageConverter smc = new SimpleMessageConverter();
		final String strMessage = String.valueOf(smc.fromMessage(message));
		TaskResult taskResult = DISCARD;
		try {
			taskResult = messageListener.doTask(strMessage);
		} catch (final Throwable e) {
			log.error("=================ERROR MESSAGE DUMP START=========================");
			log.error("Unable to handle message: {} due to exception:{}", message, e);
			log.error("=================ERROR MESSAGE DUMP END=========================");
		}
		log.info("Message handling status is: {}", taskResult);
	}
}

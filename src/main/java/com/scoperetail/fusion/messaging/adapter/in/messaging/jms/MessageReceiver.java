package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageReceiver implements SessionAwareMessageListener<Message> {

	private String queue;
	private String brokerId;
	private MessageListener<String> messageListener;

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		System.out.println("JAI HANUMAN");
		System.out.println(brokerId + "-------" + queue+"-------------Message received");
		SimpleMessageConverter smc = new SimpleMessageConverter();
		String strMessage = String.valueOf(smc.fromMessage(message));
		messageListener.doTask(strMessage);
	}

}
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms;

public interface MessageSender {

	boolean send(String brokerId, String queue, String payload);

	default boolean auditAndSend(String brokerId, String queue, String payload) {
		send(brokerId, queue, payload);
		send(brokerId, "AUDTI.IN", payload);
		return true;
	}

}

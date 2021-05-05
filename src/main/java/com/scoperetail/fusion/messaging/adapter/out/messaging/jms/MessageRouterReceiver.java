package com.scoperetail.fusion.messaging.adapter.out.messaging.jms;

import com.scoperetail.fusion.messaging.adapter.in.messaging.jms.MessageListener;

public interface MessageRouterReceiver {
	void registerListener(String brokerId, String queueName, MessageListener<String> messageListener);

}

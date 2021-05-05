package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

public interface MessageListener<T> {
	TaskResult doTask(final T message);
}

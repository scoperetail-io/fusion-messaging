/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

public interface MessageListener<T> {

	boolean canHandle(final T message);

	TaskResult doTask(final T message) throws Exception;
}

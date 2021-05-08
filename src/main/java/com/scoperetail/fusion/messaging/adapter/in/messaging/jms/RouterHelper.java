/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.in.messaging.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

public interface RouterHelper {
	ConnectionFactory getConnectionFactory(String brokerId);

	Queue getQueue(String queueName);
}

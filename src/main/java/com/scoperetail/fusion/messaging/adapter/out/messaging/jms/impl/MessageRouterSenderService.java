/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.messaging.adapter.out.messaging.jms.impl;

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

import java.util.HashMap;
import java.util.Map;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.scoperetail.fusion.messaging.adapter.out.messaging.jms.MessageRouterSender;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageRouterSenderService implements MessageRouterSender {

	private final Map<String, JmsTemplate> jmsTemplateByBrokerIdMap = new HashMap<>(1);

	@Override
	public void send(final String brokerId, final String queue, final String payload) {
		final JmsTemplate jmsTemplate = jmsTemplateByBrokerIdMap.get(brokerId);
		jmsTemplate.convertAndSend(queue, payload);
	}

	@Override
	public void registerTemplate(final String brokerId, final JmsTemplate jmsTemplate) {
		jmsTemplateByBrokerIdMap.put(brokerId, jmsTemplate);
	}
}

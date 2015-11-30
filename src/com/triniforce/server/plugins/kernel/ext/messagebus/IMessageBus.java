/*
 * Copyright(C) Triniforce
 * All Rights Reserved.
 *
 */ 
package com.triniforce.server.plugins.kernel.ext.messagebus;

import com.triniforce.utils.IMessageHandler;

public interface IMessageBus {
	public void publish(Object message);
	public <T> void  subscribe(Class<T> msgClass, IMessageHandler<T> handler);
	public <T> void  unsubscribe(Class<T> msgClass, IMessageHandler<T> handler);
}

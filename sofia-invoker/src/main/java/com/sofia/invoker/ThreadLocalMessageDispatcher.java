package com.sofia.invoker;


/**
 * Thread Local Message Factory
 * 
 * @author bhlangonijr
 *
 */
public class ThreadLocalMessageDispatcher extends ThreadLocal<MessageDispatcher> {

	final long serviceTtl;
	
	public ThreadLocalMessageDispatcher(long serviceTtl) {
		this.serviceTtl=serviceTtl;
	}

	@Override
	protected MessageDispatcher initialValue() {
		return new MessageDispatcher(serviceTtl);
	} 
}
package com.gateway.comm;

/**
 * @author chenglutao on 2019/10/25
 */
public abstract interface PEventListener {
	public abstract void handle(PEvent paramPEvent);
}
package com.gateway.comm;

import java.net.ProtocolException;

/**
 * @author chenglutao on 2019/10/25
 */
public class PException extends ProtocolException {
	public PException(String message) {
		super(message);
	}
}
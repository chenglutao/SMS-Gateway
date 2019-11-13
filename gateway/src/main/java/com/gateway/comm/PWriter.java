package com.gateway.comm;

import java.io.IOException;

/**
 * @author chenglutao on 2019/10/25
 */
public abstract class PWriter {
	public abstract void write(PMessage paramPMessage) throws IOException;
}
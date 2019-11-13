package com.sms.gateway.comm.cmpp;

import java.io.IOException;
import java.io.OutputStream;

import com.gateway.comm.PMessage;
import com.gateway.comm.PWriter;
import com.sms.gateway.comm.cmpp.message.CMPPMessage;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPWriter extends PWriter {
	protected OutputStream out;

	public CMPPWriter(OutputStream out) {
		this.out = out;
	}

	public void write(PMessage message) throws IOException {
		CMPPMessage msg = (CMPPMessage) message;
		this.out.write(msg.getBytes());
	}

	public void writeHeartbeat() throws IOException {
	}
}

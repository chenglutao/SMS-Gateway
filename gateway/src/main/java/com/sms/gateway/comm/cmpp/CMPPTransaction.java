package com.sms.gateway.comm.cmpp;

import com.gateway.comm.PException;
import com.gateway.comm.PLayer;
import com.gateway.comm.PMessage;
import com.sms.gateway.comm.cmpp.message.CMPPMessage;
import com.sms.gateway.utils.Debug;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPTransaction extends PLayer {
	private CMPPMessage receive;
	private int sequenceId;

	public CMPPTransaction(PLayer connection) {
		super(connection);
		this.sequenceId = super.id;
	}

	public synchronized void onReceive(PMessage msg) {
		this.receive = (CMPPMessage) msg;
		this.sequenceId = this.receive.getSequenceId();
		if (CMPPConstant.debug) {
			Debug.dump(this.receive.toString());
		}

		this.notifyAll();
	}

	public void send(PMessage message) throws PException {
		CMPPMessage mes = (CMPPMessage) message;
		mes.setSequenceId(this.sequenceId);
		super.parent.send(message);
		if (CMPPConstant.debug) {
			Debug.dump(mes.toString());
		}

	}

	public CMPPMessage getResponse() {
		return this.receive;
	}

	public synchronized void waitResponse() {
		if (this.receive == null) {
			try {
				this.wait((long) ((CMPPConnection) super.parent).getTransactionTimeout());
			} catch (InterruptedException var2) {
			}
		}

	}
}

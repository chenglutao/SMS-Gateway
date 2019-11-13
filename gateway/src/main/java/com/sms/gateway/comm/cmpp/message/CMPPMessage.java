package com.sms.gateway.comm.cmpp.message;

import com.gateway.comm.PMessage;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public abstract class CMPPMessage extends PMessage implements Cloneable {
	protected byte[] buf;
	protected int sequence_Id;

	public Object clone() {
		CMPPMessage localCMPPMessage1;
		try {
			CMPPMessage m = (CMPPMessage) super.clone();
			m.buf = ((byte[]) this.buf.clone());
			return m;
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
			localCMPPMessage1 = null;
		}
		return localCMPPMessage1;
	}

	public abstract String toString();

	public abstract int getCommandId();

	public int getSequenceId() {
		return this.sequence_Id;
	}

	public void setSequenceId(int sequence_Id) {
		this.sequence_Id = sequence_Id;
		TypeConvert.int2byte(sequence_Id, this.buf, 8);
	}

	public byte[] getBytes() {
		return this.buf;
	}
}
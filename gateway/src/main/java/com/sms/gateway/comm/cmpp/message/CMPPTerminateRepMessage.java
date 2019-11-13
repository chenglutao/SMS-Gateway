package com.sms.gateway.comm.cmpp.message;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPTerminateRepMessage extends CMPPMessage {
	public CMPPTerminateRepMessage() {
		int len = 12;

		this.buf = new byte[len];

		TypeConvert.int2byte(len, this.buf, 0);
		TypeConvert.int2byte(-2147483646, this.buf, 4);
	}

	public CMPPTerminateRepMessage(byte[] buf) throws IllegalArgumentException {
		this.buf = new byte[4];
		if (buf.length != 4) {
			throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
		}
		System.arraycopy(buf, 0, this.buf, 0, 4);
		this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
	}

	public String toString() {
		String tmpStr = "CMPP_Terminate_REP: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		return tmpStr;
	}

	public int getCommandId() {
		return -2147483646;
	}
}
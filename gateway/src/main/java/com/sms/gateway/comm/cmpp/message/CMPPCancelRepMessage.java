package com.sms.gateway.comm.cmpp.message;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPCancelRepMessage extends CMPPMessage {
	public CMPPCancelRepMessage(byte[] buf) throws IllegalArgumentException {
		this.buf = new byte[5];
		if (buf.length != 5) {
			throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
		}

		System.arraycopy(buf, 0, this.buf, 0, 5);
		this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
	}

	public int getSuccessId() {
		return this.buf[4];
	}

	public String toString() {
		String tmpStr = "CMPP_Cancel_REP: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append(",SuccessId=").append(String.valueOf(this.buf[4]))));
		return tmpStr;
	}

	public int getCommandId() {
		return -2147483641;
	}
}
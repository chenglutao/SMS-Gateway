package com.sms.gateway.comm.cmpp.message;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPDeliverRepMessage extends CMPPMessage {
	private String outStr;

	public CMPPDeliverRepMessage(byte[] msg_Id, int result) throws IllegalArgumentException {
		if (msg_Id.length > 8) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.DELIVER_REPINPUT_ERROR)))
							.append(":msg_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
		}
		if ((result < 0) || (result > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.DELIVER_REPINPUT_ERROR)))
							.append(":result").append(CMPPConstant.INT_SCOPE_ERROR))));
		}

		int len = 21;
		this.buf = new byte[len];

		TypeConvert.int2byte(len, this.buf, 0);
		TypeConvert.int2byte(-2147483643, this.buf, 4);

		System.arraycopy(msg_Id, 0, this.buf, 12, msg_Id.length);

		this.buf[20] = ((byte) result);

		this.outStr = ",result=".concat(String.valueOf(String.valueOf(result)));
	}

	public String toString() {
		String tmpStr = "CMPP_Deliver_REP: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
		return tmpStr;
	}

	public int getCommandId() {
		return -2147483643;
	}
}
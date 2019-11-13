package com.sms.gateway.comm.cmpp.message;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPActiveRepMessage extends CMPPMessage {
	public CMPPActiveRepMessage(int success_Id) throws IllegalArgumentException {
		if ((success_Id <= 0) || (success_Id > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.ACTIVE_REPINPUT_ERROR)))
							.append(":success_Id").append(CMPPConstant.INT_SCOPE_ERROR))));
		}

		int len = 13;

		this.buf = new byte[len];

		TypeConvert.int2byte(len, this.buf, 0);
		TypeConvert.int2byte(-2147483640, this.buf, 4);

		this.buf[12] = ((byte) success_Id);
	}

	public CMPPActiveRepMessage(byte[] buf) throws IllegalArgumentException {
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
		String tmpStr = "CMPP_Active_Test_REP: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append(",SuccessId=").append(String.valueOf(this.buf[4]))));
		return tmpStr;
	}

	public int getCommandId() {
		return -2147483640;
	}
}
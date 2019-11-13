package com.sms.gateway.comm.cmpp.message;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPSubmitRepMessage extends CMPPMessage {
	public CMPPSubmitRepMessage(byte[] buf) throws IllegalArgumentException {

		this.buf = new byte[13];
		if (buf.length != 13) {
			throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
		}
		System.arraycopy(buf, 0, this.buf, 0, 13);
		this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
	}

	public byte[] getMsgId() {
		byte[] tmpMsgId = new byte[8];
		System.arraycopy(this.buf, 4, tmpMsgId, 0, 8);
		return tmpMsgId;
	}

	public int getResult() {
		return this.buf[12];
	}

	public String toString() {
		String tmpStr = "CMPP_Submit_REP: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr))))
				.append(",MsgId=").append(TypeConvert.bytesToHexString(this.getMsgId(), this.getMsgId().length))));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Result=").append(getResult())));
		return tmpStr;
	}

	public int getCommandId() {
		return -2147483644;
	}

}
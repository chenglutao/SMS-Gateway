package com.sms.gateway.comm.cmpp.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPQueryMessage extends CMPPMessage {
	private String outStr;

	public CMPPQueryMessage(Date time, int query_Type, String query_Code, String reserve)
			throws IllegalArgumentException {
		if ((query_Type != 0) && (query_Type != 1)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR)))
							.append(":query_Type").append(CMPPConstant.VALUE_ERROR))));
		}
		if (query_Code.length() > 10) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR)))
							.append(":query_Code").append(CMPPConstant.STRING_LENGTH_GREAT).append("10"))));
		}
		if (reserve.length() > 8) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR)))
							.append(":reserve").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
		}

		int len = 39;

		this.buf = new byte[len];

		TypeConvert.int2byte(len, this.buf, 0);
		TypeConvert.int2byte(6, this.buf, 4);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		System.arraycopy(dateFormat.format(time).getBytes(), 0, this.buf, 12, dateFormat.format(time).length());

		this.buf[20] = ((byte) query_Type);

		System.arraycopy(query_Code.getBytes(), 0, this.buf, 21, query_Code.length());

		System.arraycopy(reserve.getBytes(), 0, this.buf, 31, reserve.length());

		this.outStr = ",time=".concat(String.valueOf(String.valueOf(dateFormat.format(time))));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",query_Type=").append(query_Type)));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",query_Code=").append(query_Code)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",reserve=").append(reserve)));
	}

	public String toString() {
		String tmpStr = "CMPP_Query: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
		return tmpStr;
	}

	public int getCommandId() {
		return 6;
	}
}
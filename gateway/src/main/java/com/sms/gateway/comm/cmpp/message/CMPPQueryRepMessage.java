package com.sms.gateway.comm.cmpp.message;

import java.text.SimpleDateFormat;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPQueryRepMessage extends CMPPMessage {
	public CMPPQueryRepMessage(byte[] buf) throws IllegalArgumentException {
		this.buf = new byte[55];
		if (buf.length != 55) {
			throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
		}
		System.arraycopy(buf, 0, this.buf, 0, buf.length);
		this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
	}

	public java.util.Date getDate() {
		byte[] tmpstr = new byte[4];
		System.arraycopy(this.buf, 4, tmpstr, 0, 4);
		String tmpYear = new String(tmpstr);

		byte[] tmpstr1 = new byte[2];
		System.arraycopy(this.buf, 8, tmpstr1, 0, 2);
		String tmpMonth = new String(tmpstr1);

		System.arraycopy(this.buf, 10, tmpstr1, 0, 2);
		String tmpDay = new String(tmpstr1);

		java.util.Date date = java.sql.Date
				.valueOf(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpYear)))
						.append("-").append(tmpMonth).append("-").append(tmpDay))));
		return date;
	}

	public int getQueryType() {
		return this.buf[12];
	}

	public String getQueryCode() {
		byte[] tmpQC = new byte[10];
		System.arraycopy(this.buf, 13, tmpQC, 0, 10);
		return new String(tmpQC).trim();
	}

	public int getMtTlmsg() {
		return TypeConvert.byte2int(this.buf, 23);
	}

	public int getMtTlusr() {
		return TypeConvert.byte2int(this.buf, 27);
	}

	public int getMtScs() {
		return TypeConvert.byte2int(this.buf, 31);
	}

	public int getMtWt() {
		return TypeConvert.byte2int(this.buf, 35);
	}

	public int getMtFl() {
		return TypeConvert.byte2int(this.buf, 39);
	}

	public int getMoScs() {
		return TypeConvert.byte2int(this.buf, 43);
	}

	public int getMoWt() {
		return TypeConvert.byte2int(this.buf, 47);
	}

	public int getMoFl() {
		return TypeConvert.byte2int(this.buf, 51);
	}

	public String toString() {
		String tmpStr = "CMPP_Query_REP: ";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Time=")
				.append(dateFormat.format(getDate()))));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append(",AuthenticatorISMG=").append(getQueryType())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",QueryCode=").append(getQueryCode())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtTlmsg=").append(getMtTlmsg())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtTlusr=").append(getMtTlusr())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtScs=").append(getMtScs())));
		tmpStr = String.valueOf(String
				.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtWt=").append(getMtWt())));
		tmpStr = String.valueOf(String
				.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MtFl=").append(getMtFl())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MoScs=").append(getMoScs())));
		tmpStr = String.valueOf(String
				.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MoWt=").append(getMoWt())));
		tmpStr = String.valueOf(String
				.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MoFl=").append(getMoFl())));
		return tmpStr;
	}

	public int getCommandId() {
		return -2147483642;
	}
}
package com.sms.gateway.comm.cmpp.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.SecurityTools;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPConnectMessage extends CMPPMessage {
	private String outStr;

	public CMPPConnectMessage(String source_Addr, int version, String shared_Secret, Date timestamp)
			throws IllegalArgumentException {
		if (source_Addr == null) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR))))
							.append(":source_Addr").append(CMPPConstant.STRING_NULL))));
		} else if (source_Addr.length() > 6) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR))))
							.append(":source_Addr").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
		} else if (version >= 0 && version <= 255) {
			int len = 39;
			super.buf = new byte[len];
			TypeConvert.int2byte(len, super.buf, 0);
			TypeConvert.int2byte(1, super.buf, 4);
			System.arraycopy(source_Addr.getBytes(), 0, super.buf, 12, source_Addr.length());
			if (shared_Secret != null) {
				len = source_Addr.length() + 19 + shared_Secret.length();
			} else {
				len = source_Addr.length() + 19;
			}

			byte[] tmpbuf = new byte[len];
			System.arraycopy(source_Addr.getBytes(), 0, tmpbuf, 0, source_Addr.length());
			int tmploc = source_Addr.length() + 9;
			if (shared_Secret != null) {
				System.arraycopy(shared_Secret.getBytes(), 0, tmpbuf, tmploc, shared_Secret.length());
				tmploc += shared_Secret.length();
			}
			// TODO
			String tmptime = "0008080808";
			System.arraycopy(tmptime.getBytes(), 0, tmpbuf, tmploc, 10);

			SecurityTools.md5(tmpbuf, 0, len, this.buf, 18);

			this.buf[34] = ((byte) version);

			TypeConvert.int2byte(8080808, this.buf, 35);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String times = sdf.format(timestamp);
			this.outStr = ",source_Addr=".concat(String.valueOf(String.valueOf(source_Addr)));
			this.outStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(this.outStr))))
					.append(",version=").append(version)));
			this.outStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(this.outStr))))
					.append(",shared_Secret=").append(shared_Secret)));
			this.outStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(this.outStr))))
					.append(",timeStamp=").append(times)));
		} else {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR))))
							.append(":version").append(CMPPConstant.INT_SCOPE_ERROR))));
		}
	}

	public String toString() {
		String tmpStr = "CMPP_Connect: ";
		tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr))))
				.append("Sequence_Id=").append(this.getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
		return tmpStr;
	}

	public int getCommandId() {
		return 1;
	}

}

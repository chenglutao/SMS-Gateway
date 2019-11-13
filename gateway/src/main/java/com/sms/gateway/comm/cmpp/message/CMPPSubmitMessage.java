package com.sms.gateway.comm.cmpp.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPSubmitMessage extends CMPPMessage {
	private String outStr;

	public CMPPSubmitMessage(int pk_Total, int pk_Number, int registered_Delivery, int msg_Level, String service_Id,
			int fee_UserType, String fee_Terminal_Id, int tp_Pid, int tp_Udhi, int msg_Fmt, String msg_Src,
			String fee_Type, String fee_Code, Date valid_Time, Date at_Time, String src_Terminal_Id,
			String[] dest_Terminal_Id, byte[] msg_Content, String reserve) throws IllegalArgumentException {
		if ((pk_Total < 1) || (pk_Total > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":").append(CMPPConstant.PK_TOTAL_ERROR))));
		}

		if ((pk_Number < 1) || (pk_Number > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":").append(CMPPConstant.PK_NUMBER_ERROR))));
		}

		if ((registered_Delivery < 0) || (registered_Delivery > 2)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":").append(CMPPConstant.REGISTERED_DELIVERY_ERROR))));
		}

		if ((msg_Level < 0) || (msg_Level > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":msg_Level").append(CMPPConstant.INT_SCOPE_ERROR))));
		}

		if (service_Id.length() > 10) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":service_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("10"))));
		}

		if ((fee_UserType < 0) || (fee_UserType > 3)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":").append(CMPPConstant.FEE_USERTYPE_ERROR))));
		}

		if (fee_Terminal_Id.length() > 21) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":fee_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
		}

		if ((tp_Pid < 0) || (tp_Pid > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":tp_Pid").append(CMPPConstant.INT_SCOPE_ERROR))));
		}

		if ((tp_Udhi < 0) || (tp_Udhi > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":tp_Udhi").append(CMPPConstant.INT_SCOPE_ERROR))));
		}

		if ((msg_Fmt < 0) || (msg_Fmt > 255)) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":msg_Fmt").append(CMPPConstant.INT_SCOPE_ERROR))));
		}

		if (msg_Src.length() > 6) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":msg_Src").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
		}

		if (fee_Type.length() > 2) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":fee_Type").append(CMPPConstant.STRING_LENGTH_GREAT).append("2"))));
		}

		if (fee_Code.length() > 6) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":fee_Code").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
		}

		if (src_Terminal_Id.length() > 21) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":src_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
		}

		if (dest_Terminal_Id.length > 100) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":dest_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("100"))));
		}

		for (int i = 0; i < dest_Terminal_Id.length; i++) {
			if (dest_Terminal_Id[i].length() > 21) {
				throw new IllegalArgumentException(String.valueOf(
						String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
								.append(":dest_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
			}

		}

		if (msg_Fmt == 0) {
			if (msg_Content.length > 160) {
				throw new IllegalArgumentException(String.valueOf(
						String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
								.append(":msg_Content").append(CMPPConstant.STRING_LENGTH_GREAT).append("160"))));
			}

		} else if (msg_Content.length > 140) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":msg_Content").append(CMPPConstant.STRING_LENGTH_GREAT).append("140"))));
		}

		if (reserve.length() > 8) {
			throw new IllegalArgumentException(String.valueOf(
					String.valueOf(new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))
							.append(":reserve").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
		}

		int len = 138 + 21 * dest_Terminal_Id.length + msg_Content.length;

		this.buf = new byte[len];

		TypeConvert.int2byte(len, this.buf, 0);
		TypeConvert.int2byte(4, this.buf, 4);

		this.buf[20] = ((byte) pk_Total);
		this.buf[21] = ((byte) pk_Number);
		this.buf[22] = ((byte) registered_Delivery);
		this.buf[23] = ((byte) msg_Level);
		System.arraycopy(service_Id.getBytes(), 0, this.buf, 24, service_Id.length());
		this.buf[34] = ((byte) fee_UserType);
		System.arraycopy(fee_Terminal_Id.getBytes(), 0, this.buf, 35, fee_Terminal_Id.length());
		this.buf[56] = ((byte) tp_Pid);
		this.buf[57] = ((byte) tp_Udhi);
		this.buf[58] = ((byte) msg_Fmt);
		System.arraycopy(msg_Src.getBytes(), 0, this.buf, 59, msg_Src.length());
		System.arraycopy(fee_Type.getBytes(), 0, this.buf, 65, fee_Type.length());
		System.arraycopy(fee_Code.getBytes(), 0, this.buf, 67, fee_Code.length());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

		if (valid_Time != null) {
			String tmpTime = String.valueOf(String.valueOf(dateFormat.format(valid_Time))).concat("032+");
			System.arraycopy(tmpTime.getBytes(), 0, this.buf, 73, 16);
		}

		if (at_Time != null) {
			String tmpTime = String.valueOf(String.valueOf(dateFormat.format(at_Time))).concat("032+");
			System.arraycopy(tmpTime.getBytes(), 0, this.buf, 90, 16);
		}

		System.arraycopy(src_Terminal_Id.getBytes(), 0, this.buf, 107, src_Terminal_Id.length());

		this.buf[''] = ((byte) dest_Terminal_Id.length);

		int i = 0;
		for (i = 0; i < dest_Terminal_Id.length; i++) {
			System.arraycopy(dest_Terminal_Id[i].getBytes(), 0, this.buf, 129 + i * 21, dest_Terminal_Id[i].length());
		}

		int loc = 129 + i * 21;
		this.buf[loc] = ((byte) msg_Content.length);
		System.arraycopy(msg_Content, 0, this.buf, loc + 1, msg_Content.length);

		loc = loc + 1 + msg_Content.length;
		System.arraycopy(reserve.getBytes(), 0, this.buf, loc, reserve.length());

		this.outStr = ",msg_id=";
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",pk_Total=").append(pk_Total)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",pk_Number=").append(pk_Number)));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",registered_Delivery=").append(registered_Delivery)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Level=").append(msg_Level)));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",service_Id=").append(service_Id)));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",fee_UserType=").append(fee_UserType)));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",fee_Terminal_Id=").append(fee_Terminal_Id)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",tp_Pid=").append(tp_Pid)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",tp_Udhi=").append(tp_Udhi)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Fmt=").append(msg_Fmt)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",msg_Src=").append(msg_Src)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Type=").append(fee_Type)));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",fee_Code=").append(fee_Code)));
		if (valid_Time != null) {
			this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
					.append(",valid_Time=").append(dateFormat.format(valid_Time))));
		} else {
			this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",valid_Time=null");
		}
		if (at_Time != null) {
			this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
					.append(",at_Time=").append(dateFormat.format(at_Time))));
		} else {
			this.outStr = String.valueOf(String.valueOf(this.outStr)).concat(",at_Time=null");
		}
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",src_Terminal_Id=").append(src_Terminal_Id)));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",destusr_Tl=").append(dest_Terminal_Id.length)));
		for (int t = 0; t < dest_Terminal_Id.length; t++) {
			this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
					.append(",dest_Terminal_Id[").append(t).append("]=").append(dest_Terminal_Id[t])));
		}
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",msg_Length=").append(msg_Content.length)));
		this.outStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(this.outStr)))
				.append(",msg_Content=").append(new String(msg_Content))));
		this.outStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(this.outStr))).append(",reserve=").append(reserve)));
	}

	public String toString() {
		String tmpStr = "CMPP_Submit: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(tmpStr)).concat(String.valueOf(String.valueOf(this.outStr)));
		return tmpStr;
	}

	public int getCommandId() {
		return 4;
	}
}
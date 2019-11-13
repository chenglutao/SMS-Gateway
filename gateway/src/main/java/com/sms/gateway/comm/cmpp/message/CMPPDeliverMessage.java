package com.sms.gateway.comm.cmpp.message;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import com.sms.gateway.comm.cmpp.CMPPConstant;
import com.sms.gateway.utils.TypeConvert;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPDeliverMessage extends CMPPMessage {
	private int deliverType = 0;

	public CMPPDeliverMessage(byte[] buf) throws IllegalArgumentException {
		this.deliverType = buf[67];
		int len = 77 + (buf[68] & 0xFF);
		if (buf.length != len) {
			throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
		}
		this.buf = new byte[len];
		System.arraycopy(buf, 0, this.buf, 0, buf.length);
		this.sequence_Id = TypeConvert.byte2int(this.buf, 0);
	}

	public byte[] getMsgId() {
		byte[] tmpMsgId = new byte[8];
		System.arraycopy(this.buf, 69, tmpMsgId, 0, 8);
		return tmpMsgId;
	}

	public String getDestnationId() {
		byte[] tmpId = new byte[21];
		System.arraycopy(this.buf, 12, tmpId, 0, 21);
		return new String(tmpId).trim();
	}

	public String getServiceId() {
		byte[] tmpId = new byte[10];
		System.arraycopy(this.buf, 33, tmpId, 0, 10);
		return new String(tmpId).trim();
	}

	public int getTpPid() {
		int tmpId = this.buf[43];
		return tmpId;
	}

	public int getTpUdhi() {
		int tmpId = this.buf[44];
		return tmpId;
	}

	public int getMsgFmt() {
		int tmpFmt = this.buf[45];
		return tmpFmt;
	}

	public String getSrcterminalId() {
		byte[] tmpId = new byte[21];
		System.arraycopy(this.buf, 46, tmpId, 0, 21);
		return new String(tmpId).trim();
	}

	public int getRegisteredDeliver() {
		return this.buf[67];
	}

	public int getMsgLength() {
		return this.buf[68] & 0xFF;
	}

	public byte[] getMsgContent() {
		if (this.deliverType == 0) {
			int len = this.buf[68] & 0xFF;
			byte[] tmpContent = new byte[len];
			System.arraycopy(this.buf, 69, tmpContent, 0, len);
			return tmpContent;
		}

		return null;
	}

	public String getReserve() {
		int loc = 69 + (this.buf[68] & 0xFF);
		byte[] tmpReserve = new byte[8];
		System.arraycopy(this.buf, loc, tmpReserve, 0, 8);
		return new String(tmpReserve).trim();
	}

	public byte[] getStatusMsgId() {
		if (this.deliverType == 1) {
			byte[] tmpId = new byte[8];
			System.arraycopy(this.buf, 69, tmpId, 0, 8);
			return tmpId;
		}

		return null;
	}

	public String getStat() {
		if (this.deliverType == 1) {
			byte[] tmpStat = new byte[7];
			System.arraycopy(this.buf, 77, tmpStat, 0, 7);
			return new String(tmpStat).trim();
		}

		return null;
	}

	public Date getSubmitTime() {
		if (this.deliverType == 1) {
			byte[] tmpbyte = new byte[2];

			System.arraycopy(this.buf, 84, tmpbyte, 0, 2);
			String tmpstr = new String(tmpbyte);
			int tmpYear = 2000 + Integer.parseInt(tmpstr);

			System.arraycopy(this.buf, 86, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpMonth = Integer.parseInt(tmpstr) - 1;

			System.arraycopy(this.buf, 88, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpDay = Integer.parseInt(tmpstr);

			System.arraycopy(this.buf, 90, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpHour = Integer.parseInt(tmpstr);

			System.arraycopy(this.buf, 92, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpMinute = Integer.parseInt(tmpstr);

			Calendar calendar = Calendar.getInstance();
			calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute);

			return calendar.getTime();
		}

		return null;
	}

	public Date getDoneTime() {
		if (this.deliverType == 1) {
			byte[] tmpbyte = new byte[2];

			System.arraycopy(this.buf, 94, tmpbyte, 0, 2);
			String tmpstr = new String(tmpbyte);
			int tmpYear = 2000 + Integer.parseInt(tmpstr);

			System.arraycopy(this.buf, 96, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpMonth = Integer.parseInt(tmpstr) - 1;

			System.arraycopy(this.buf, 98, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpDay = Integer.parseInt(tmpstr);

			System.arraycopy(this.buf, 100, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpHour = Integer.parseInt(tmpstr);

			System.arraycopy(this.buf, 102, tmpbyte, 0, 2);
			tmpstr = new String(tmpbyte);
			int tmpMinute = Integer.parseInt(tmpstr);

			Calendar calendar = Calendar.getInstance();
			calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute);
			return calendar.getTime();
		}

		return null;
	}

	public String getDestTerminalId() {
		if (this.deliverType == 1) {
			byte[] tmpId = new byte[21];
			System.arraycopy(this.buf, 104, tmpId, 0, 21);
			return new String(tmpId);
		}

		return null;
	}

	public int getSMSCSequence() {
		if (this.deliverType == 1) {
			int tmpSequence = TypeConvert.byte2int(this.buf, 125);
			return tmpSequence;
		}

		return -1;
	}

	public String toString() {
		String tmpStr = "CMPP_Deliver: ";
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append("Sequence_Id=").append(getSequenceId())));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append(",MsgId=").append(TypeConvert.bytesToHexString(this.getMsgId(), this.getMsgId().length))));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append(",DestnationId=").append(getDestnationId())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",ServiceId=").append(getServiceId())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpPid=").append(getTpPid())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",TpUdhi=").append(getTpUdhi())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgFmt=").append(getMsgFmt())));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append(",SrcterminalId=").append(getSrcterminalId())));
		tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
				.append(",RegisteredDeliver=").append(getRegisteredDeliver())));
		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",MsgLength=").append(getMsgLength())));
		if (getRegisteredDeliver() == 1) {
			tmpStr = String.valueOf(String.valueOf(
					new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Stat=").append(getStat())));
			tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
					.append(",SubmitTime=").append(getSubmitTime())));
			tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
					.append(",DoneTime=").append(getDoneTime())));
			tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
					.append(",DestTerminalId=").append(getDestTerminalId())));
			tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
					.append(",SMSCSequence=").append(getSMSCSequence())));
		} else {
			if (8 == getMsgFmt() || 15 == getMsgFmt()) {// 处理中文字符
				try {
					String msgContent = new String(getMsgContent(), "GBK");
					tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
							.append(",MsgContent=").append(new String(msgContent))));
				} catch (UnsupportedEncodingException e) {
					e.getMessage();
					e.printStackTrace();
				}
			} else {// 处理非中文字符
				tmpStr = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String.valueOf(tmpStr)))
						.append(",MsgContent=").append(new String(getMsgContent()))));
			}
		}

		tmpStr = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(tmpStr))).append(",Reserve=").append(getReserve())));
		return tmpStr;
	}

	public int getCommandId() {
		return 5;
	}

}
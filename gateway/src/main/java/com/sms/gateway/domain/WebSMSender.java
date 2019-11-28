package com.sms.gateway.domain;

import java.io.IOException;
import java.util.Date;

import com.sms.gateway.comm.cmpp.message.CMPPDeliverMessage;
import com.sms.gateway.comm.cmpp.message.CMPPDeliverRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPMessage;
import com.sms.gateway.comm.cmpp.message.CMPPSubmitMessage;
import com.sms.gateway.comm.cmpp.message.CMPPSubmitRepMessage;
import com.sms.gateway.utils.Args;
import com.sms.gateway.utils.TypeConvert;
import com.sms.smproxy.SMProxy;

/**
 * @author chenglutao on 2019/10/25
 */
public class WebSMSender extends SMProxy {

	private int pkTotal = 1;// 相同msg_Id消息总条数,短短信这里是1
	private int pkNumber = 1;// 相同msg_Id的消息序号
	private int registeredDelivery = 1;// 是否要求返回状态报告 0：不需要 1：需要
	private int msgLevel = 1;// 信息级别
	private String serviceId = "123456";// 移动提供的业务代码   需修改
	private int feeUserType = 2;// 0：对目的终端MSISDN计费； 1：对源终端MSISDN计费； 2：对SP计费;
	private String feeTerminalId = "";// 如本字节填空，则表示本字段无效，对谁计费参见Fee_UserType字段，本字段与Fee_UserType字段互斥
	private int tpPid = 0; // GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.9 二进制填127 普通文本0   需修改
	private int tpUdhi = 0;// GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐 0不包含头部信息 1包含头部信息
	private int msgFmt = 15;// 信息格式 246：二进制信息   需修改
	private String msgSrc = "123456";// 信息内容来源(source-addr)   需修改
	private String feeType = "01";// 资费类别 一般为02：按条计信息费
	private String feeCode = "10";// 资费代码（以分为单位）
	private Date validTime;// 存活有效期，格式遵循SMPP3.3协议 48小时
	private Date atTime = null;// 定时发送时间，格式遵循SMPP3.3协议
	private String srcTerminalId = "123456";// 短信接入号码  需修改
	private String[] phone;// 目标手机号码
	private byte[] msgContent;// 二进制数据
	private String reserve = "";// 保留字段

	private static Args arg = Env.getConfig().getArgs("CMPPConnect");

	private static WebSMSender instance;

	public static WebSMSender getInstance() {
		if (instance == null) {
			instance = new WebSMSender(arg);
		}
		return instance;
	}

	public WebSMSender(Args args) {
		super(args);
	}

	@Override
	public CMPPMessage onDeliver(CMPPDeliverMessage msg) {
		byte[] msgId = msg.getMsgId();
		String bytesToHexString = TypeConvert.bytesToHexString(msgId, msgId.length);
		System.out.println("获取msgId" + bytesToHexString);
		int registeredDeliver = msg.getRegisteredDeliver();
		if (registeredDeliver == 1) {// 短信状态报告
			if (String.valueOf(msg.getStat()).equalsIgnoreCase("DELIVRD")) {
				System.out.println("收到短信状态报告： " + msg.toString());
				try {
					return super.onDeliver(msg);
				} catch (Exception ex) {
					return new CMPPDeliverRepMessage(msgId, 9);
				}
			}
		}
		if (registeredDeliver == 0) {// 短信上行消息
			String moContent = null;
			byte[] msgContent = msg.getMsgContent();
			System.out.println("收到短信网关上行消息：" + msg.toString());
			if (8 == msg.getMsgFmt() || 15 == msg.getMsgFmt()) {// 兼容普通文本输出
				try {
					moContent = new String(msgContent, "GBK");// 经测试 需要编码为GBk或GB2312
				} catch (Exception e) {
					System.out.println("解析普通文本出错了：" + e.getMessage());
					return new CMPPDeliverRepMessage(msgId, 9);
				}
			} else {// 处理其他消息内容
				msgContent = msg.getMsgContent();
				moContent = new String(msgContent);
			}
			System.out.println("收到短信网关上行消息：" + moContent);
		}
		return super.onDeliver(msg);
	}

	public CMPPSubmitRepMessage send(CMPPSubmitMessage msg) {
		CMPPSubmitRepMessage reportMsg = null;
		try {
			reportMsg = (CMPPSubmitRepMessage) super.send(msg);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return reportMsg;
	}

	public void CmppSmsSubmitClient(String[] phone, byte[] msgContent) {
		validTime = new Date(System.currentTimeMillis() + 172800000);
		CMPPSubmitMessage submitMessage = new CMPPSubmitMessage(pkTotal, pkNumber, registeredDelivery, msgLevel,
				serviceId, feeUserType, feeTerminalId, tpPid, tpUdhi, msgFmt, msgSrc, feeType, feeCode, validTime,
				atTime, srcTerminalId, phone, msgContent, reserve);
		CMPPSubmitRepMessage submitRepMessage = send(submitMessage);
		if (submitRepMessage == null || submitRepMessage.getResult() != 0) {
			System.out.println("发送短信失败");
		}
		// 短信发送成功
		String msgId = TypeConvert.bytesToHexString(submitRepMessage.getMsgId(), submitRepMessage.getMsgId().length);
		System.out.println("提交成功: " + msgId);
	}

	public void OnTerminate() {
		System.out.println("Connection have been breaked! ");
	}
}
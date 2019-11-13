package com.sms.gateway.domain;

/**
 * @author chenglutao on 2019/10/25
 */
public class TestSendSms {

	public static void main(String[] args) throws InterruptedException {
		WebSMSender.getInstance().CmppSmsSubmitClient(new String[] { "18813024889" }, "消息哈".getBytes());
		Thread.sleep(10000000);
	}
}

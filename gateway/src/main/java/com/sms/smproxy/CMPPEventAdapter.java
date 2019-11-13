package com.sms.smproxy;

import com.gateway.comm.PEventAdapter;
import com.gateway.comm.PException;
import com.gateway.comm.PLayer;
import com.gateway.comm.PMessage;
import com.sms.gateway.comm.cmpp.CMPPConnection;
import com.sms.gateway.comm.cmpp.CMPPTransaction;
import com.sms.gateway.comm.cmpp.message.CMPPActiveRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPDeliverMessage;
import com.sms.gateway.comm.cmpp.message.CMPPMessage;
import com.sms.gateway.comm.cmpp.message.CMPPTerminateRepMessage;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPEventAdapter extends PEventAdapter {
	private SMProxy smProxy = null;
	private CMPPConnection conn = null;

	public CMPPEventAdapter(SMProxy smProxy) {
		this.smProxy = smProxy;
		this.conn = smProxy.getConn();
	}

	public void childCreated(PLayer child) {
		CMPPTransaction t = (CMPPTransaction) child;
		CMPPMessage msg = t.getResponse();
		CMPPMessage resmsg = null;
		if (msg.getCommandId() == 2) {
			resmsg = new CMPPTerminateRepMessage();
			this.smProxy.onTerminate();
		} else if (msg.getCommandId() == 8) {
			resmsg = new CMPPActiveRepMessage(0);
		} else if (msg.getCommandId() == 5) {
			CMPPDeliverMessage tmpmes = (CMPPDeliverMessage) msg;
			resmsg = this.smProxy.onDeliver(tmpmes);
		} else {
			t.close();
		}

		if (resmsg != null) {
			try {
				t.send((PMessage) resmsg);
			} catch (PException var6) {
				var6.printStackTrace();
			}

			t.close();
		}

		if (msg.getCommandId() == 2) {
			this.conn.close();
		}
	}
}

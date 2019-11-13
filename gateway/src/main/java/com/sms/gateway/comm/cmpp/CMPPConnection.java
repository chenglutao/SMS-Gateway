package com.sms.gateway.comm.cmpp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.gateway.comm.PException;
import com.gateway.comm.PLayer;
import com.gateway.comm.PMessage;
import com.gateway.comm.PReader;
import com.gateway.comm.PSocketConnection;
import com.gateway.comm.PWriter;
import com.sms.gateway.comm.cmpp.message.CMPPActiveMessage;
import com.sms.gateway.comm.cmpp.message.CMPPActiveRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPConnectMessage;
import com.sms.gateway.comm.cmpp.message.CMPPConnectRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPMessage;
import com.sms.gateway.comm.cmpp.message.CMPPTerminateMessage;
import com.sms.gateway.utils.Args;
import com.sms.gateway.utils.Resource;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPConnection extends PSocketConnection {
	private int degree = 0;
	private int hbnoResponseOut = 3;
	private String source_addr = null;
	private int version;
	private String shared_secret;

	public CMPPConnection(Args args) {
		this.hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
		this.source_addr = args.get("source-addr", "huawei");
		this.version = args.get("version", 1);
		this.shared_secret = args.get("shared-secret", "");
		CMPPConstant.debug = args.get("debug", false);
		CMPPConstant.initConstant(this.getResource());
		this.init(args);
	}

	protected PWriter getWriter(OutputStream out) {
		return new CMPPWriter(out);
	}

	protected PReader getReader(InputStream in) {
		return new CMPPReader(in);
	}

	public int getChildId(PMessage message) {
		CMPPMessage mes = (CMPPMessage) message;
		int sequenceId = mes.getSequenceId();
		return mes.getCommandId() != 5 && mes.getCommandId() != 8 && mes.getCommandId() != 2 ? sequenceId : -1;
	}

	public PLayer createChild() {
		return new CMPPTransaction(this);
	}

	public int getTransactionTimeout() {
		return super.transactionTimeout;
	}

	public Resource getResource() {
		try {
			Resource var1 = new Resource(this.getClass(), "resource");
			return var1;
		} catch (IOException var3) {
			var3.printStackTrace();
			Object var2 = null;
			return (Resource) var2;
		}
	}

	public synchronized void waitAvailable() {
		try {
			if (this.getError() == PSocketConnection.NOT_INIT) {
				this.wait((long) super.transactionTimeout);
			}
		} catch (InterruptedException var2) {
		}

	}

	public void close() {
		try {
			CMPPTerminateMessage msg = new CMPPTerminateMessage();
			this.send(msg);
		} catch (PException var2) {
		}

		super.close();
	}

	protected void heartbeat() throws IOException {
		CMPPTransaction t = (CMPPTransaction) this.createChild();
		CMPPActiveMessage hbmes = new CMPPActiveMessage();
		t.send(hbmes);
		t.waitResponse();
		CMPPActiveRepMessage rsp = (CMPPActiveRepMessage) t.getResponse();
		if (rsp == null) {
			++this.degree;
			if (this.degree == this.hbnoResponseOut) {
				this.degree = 0;
				throw new IOException(CMPPConstant.HEARTBEAT_ABNORMITY);
			}
		} else {
			this.degree = 0;
		}

		t.close();
	}

	protected synchronized void connect() {
		super.connect();
		if (this.available()) {
			CMPPConnectMessage request = null;
			CMPPConnectRepMessage rsp = null;

			try {
				request = new CMPPConnectMessage(this.source_addr, this.version, this.shared_secret, new Date());
			} catch (IllegalArgumentException var6) {
				var6.printStackTrace();
				this.close();
				this.setError(CMPPConstant.CONNECT_INPUT_ERROR);
			}

			CMPPTransaction t = (CMPPTransaction) this.createChild();

			try {
				t.send(request);
				PMessage m = super.in.read();
				this.onReceive(m);
			} catch (IOException var5) {
				var5.printStackTrace();
				this.close();
				this.setError(String.valueOf(String.valueOf(CMPPConstant.LOGIN_ERROR))
						.concat(String.valueOf(String.valueOf(this.explain(var5)))));
			}

			rsp = (CMPPConnectRepMessage) t.getResponse();
			if (rsp == null) {
				this.close();
				this.setError(CMPPConstant.CONNECT_TIMEOUT);
			}

			t.close();
			if (rsp != null && rsp.getStatus() != 0) {
				this.close();
				if (rsp.getStatus() == 1) {
					this.setError(CMPPConstant.STRUCTURE_ERROR);
				} else if (rsp.getStatus() == 2) {
					this.setError(CMPPConstant.NONLICETSP_ID);
				} else if (rsp.getStatus() == 3) {
					this.setError(CMPPConstant.SP_ERROR);
				} else if (rsp.getStatus() == 4) {
					this.setError(CMPPConstant.VERSION_ERROR);
				} else {
					this.setError(CMPPConstant.OTHER_ERROR);
				}
			}

			this.notifyAll();
		}
	}
}

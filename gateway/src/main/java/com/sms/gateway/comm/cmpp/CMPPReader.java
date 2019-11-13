package com.sms.gateway.comm.cmpp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.gateway.comm.PMessage;
import com.gateway.comm.PReader;
import com.sms.gateway.comm.cmpp.message.CMPPActiveMessage;
import com.sms.gateway.comm.cmpp.message.CMPPActiveRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPCancelRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPConnectRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPDeliverMessage;
import com.sms.gateway.comm.cmpp.message.CMPPQueryRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPSubmitRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPTerminateMessage;
import com.sms.gateway.comm.cmpp.message.CMPPTerminateRepMessage;

/**
 * @author chenglutao on 2019/10/25
 */
public class CMPPReader extends PReader {
	protected DataInputStream in;

	public CMPPReader(InputStream is) {
		this.in = new DataInputStream(is);
	}

	public PMessage read() throws IOException {
		int total_Length = this.in.readInt();
		int command_Id = this.in.readInt();
		byte[] buf = new byte[total_Length - 8];
		this.in.readFully(buf);
		if (command_Id == -2147483647) {
			return new CMPPConnectRepMessage(buf);
		} else if (command_Id == 5) {
			return new CMPPDeliverMessage(buf);
		} else if (command_Id == -2147483644) {
			return new CMPPSubmitRepMessage(buf);
		} else if (command_Id == -2147483642) {
			return new CMPPQueryRepMessage(buf);
		} else if (command_Id == -2147483641) {
			return new CMPPCancelRepMessage(buf);
		} else if (command_Id == -2147483640) {
			return new CMPPActiveRepMessage(buf);
		} else if (command_Id == 8) {
			return new CMPPActiveMessage(buf);
		} else if (command_Id == 2) {
			return new CMPPTerminateMessage(buf);
		} else {
			return command_Id == -2147483646 ? new CMPPTerminateRepMessage(buf) : null;
		}
	}
}

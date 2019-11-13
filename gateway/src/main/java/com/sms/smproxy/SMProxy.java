package com.sms.smproxy;

import java.io.IOException;
import java.util.Map;

import com.sms.gateway.comm.cmpp.CMPPConnection;
import com.sms.gateway.comm.cmpp.CMPPTransaction;
import com.sms.gateway.comm.cmpp.message.CMPPDeliverMessage;
import com.sms.gateway.comm.cmpp.message.CMPPDeliverRepMessage;
import com.sms.gateway.comm.cmpp.message.CMPPMessage;
import com.sms.gateway.utils.Args;

/**
 * @author chenglutao on 2019/10/25
 */
public class SMProxy {
    private CMPPConnection conn;

    public SMProxy(Map args) {
        this(new Args(args));
    }

    public SMProxy(Args args) {
        this.conn = new CMPPConnection(args);
        this.conn.addEventListener(new CMPPEventAdapter(this));
        this.conn.waitAvailable();
        if (!this.conn.available()) {
            throw new IllegalStateException(this.conn.getError());
        }
    }

    public CMPPMessage send(CMPPMessage message) throws IOException {
        if (message == null) {
            return null;
        } else {
            CMPPTransaction t = (CMPPTransaction)this.conn.createChild();

            CMPPMessage var4;
            try {
                t.send(message);
                t.waitResponse();
                CMPPMessage rsp = t.getResponse();
                var4 = rsp;
            } finally {
                t.close();
            }

            return var4;
        }
    }

    public void onTerminate() {
    }

    public CMPPMessage onDeliver(CMPPDeliverMessage msg) {
        return new CMPPDeliverRepMessage(msg.getMsgId(), 0);
    }

    public void close() {
        this.conn.close();
    }

    public CMPPConnection getConn() {
        return this.conn;
    }

    public String getConnState() {
        return this.conn.getError();
    }
}

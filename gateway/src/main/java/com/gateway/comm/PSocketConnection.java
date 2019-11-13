package com.gateway.comm;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sms.gateway.utils.Args;
import com.sms.gateway.utils.Resource;
import com.sms.gateway.utils.WatchThread;

/**
 * @author chenglutao on 2019/10/25
 */
public abstract class PSocketConnection extends PLayer {
	protected static String NOT_INIT;
	protected static String CONNECTING;
	protected static String RECONNECTING;
	protected static String CONNECTED;
	protected static String HEARTBEATING;
	protected static String RECEIVEING;
	protected static String CLOSEING;
	protected static String CLOSED;
	protected static String UNKNOWN_HOST;
	protected static String PORT_ERROR;
	protected static String CONNECT_REFUSE;
	protected static String NO_ROUTE_TO_HOST;
	protected static String RECEIVE_TIMEOUT;
	protected static String CLOSE_BY_PEER;
	protected static String RESET_BY_PEER;
	protected static String CONNECTION_CLOSED;
	protected static String COMMUNICATION_ERROR;
	protected static String CONNECT_ERROR;
	protected static String SEND_ERROR;
	protected static String RECEIVE_ERROR;
	protected static String CLOSE_ERROR;
	private String error;
	protected Date errorTime = new Date();
	protected String name;
	protected String host;
	protected int port = -1;
	protected String localHost;
	protected int localPort = -1;
	protected int heartbeatInterval;
	protected PReader in;
	protected PWriter out;
	protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	protected int readTimeout;
	protected int reconnectInterval;
	protected Socket socket;
	protected WatchThread heartbeatThread;
	protected WatchThread receiveThread;
	protected int transactionTimeout;
	protected Resource resource;

	public PSocketConnection(Args args) {
		super((PLayer) null);
		this.init(args);
	}

	protected PSocketConnection() {
		super((PLayer) null);
	}

	protected void init(Args args) {
		this.resource = this.getResource();
		this.initResource();
		this.error = NOT_INIT;
		this.setAttributes(args);
		if (this.heartbeatInterval > 0) {
			class HeartbeatThread extends WatchThread {
				public HeartbeatThread() {
					super(String.valueOf(String.valueOf(PSocketConnection.this.name)).concat("-heartbeat"));
					this.setState(PSocketConnection.HEARTBEATING);
				}

				public void task() {
					try {
						Thread.sleep((long) PSocketConnection.this.heartbeatInterval);
					} catch (InterruptedException var3) {
					}

					if (PSocketConnection.this.error == null && PSocketConnection.this.out != null) {
						try {
							PSocketConnection.this.heartbeat();
						} catch (IOException var2) {
							PSocketConnection.this.setError(String.valueOf(String.valueOf(PSocketConnection.SEND_ERROR))
									.concat(String.valueOf(String.valueOf(PSocketConnection.this.explain(var2)))));
						}
					}

				}
			}

			this.heartbeatThread = new HeartbeatThread();
			this.heartbeatThread.start();
		}

		class ReceiveThread extends WatchThread {
			public ReceiveThread() {
				super(String.valueOf(String.valueOf(PSocketConnection.this.name)).concat("-receive"));
			}

			public void task() {
				try {
					if (PSocketConnection.this.error == null) {
						PMessage m = PSocketConnection.this.in.read();
						if (m != null && m != null) {
							PSocketConnection.this.onReceive(m);
						}
					} else {
						if (PSocketConnection.this.error != PSocketConnection.NOT_INIT) {
							try {
								Thread.sleep((long) PSocketConnection.this.reconnectInterval);
							} catch (InterruptedException var2) {
							}
						}

						PSocketConnection.this.connect();
					}
				} catch (IOException var3) {
				}

			}
		}
		this.receiveThread = new ReceiveThread();
		this.receiveThread.start();
	}

	public void setAttributes(Args args) {
		if (this.name != null && this.name.equals(String
				.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(String.valueOf(this.host)))))
						.append(':').append(this.port))))) {
			this.name = null;
		}

		String oldHost = this.host;
		int oldPort = this.port;
		String oldLocalHost = this.localHost;
		int oldLocalPort = this.localPort;
		this.host = args.get("host", (String) null);
		this.port = args.get("port", -1);
		this.localHost = args.get("local-host", (String) null);
		this.localPort = args.get("local-port", -1);
		this.name = args.get("name", (String) null);
		if (this.name == null) {
			this.name = String.valueOf(String.valueOf(
					(new StringBuffer(String.valueOf(String.valueOf(this.host)))).append(':').append(this.port)));
		}

		this.readTimeout = 1000 * args.get("read-timeout", this.readTimeout / 1000);
		if (this.socket != null) {
			try {
				this.socket.setSoTimeout(this.readTimeout);
			} catch (SocketException var7) {
			}
		}

		this.reconnectInterval = 1000 * args.get("reconnect-interval", -1);
		this.heartbeatInterval = 1000 * args.get("heartbeat-interval", -1);
		this.transactionTimeout = 1000 * args.get("transaction-timeout", -1);
		if (this.error == null && this.host != null && this.port != -1 && (!this.host.equals(oldHost)
				|| this.port != this.port || !this.host.equals(oldHost) || this.port != this.port)) {
			this.setError(this.resource.get("comm/need-reconnect"));
			this.receiveThread.interrupt();
		}

	}

	public void send(PMessage message) throws PException {
		if (this.error != null) {
			throw new PException(
					String.valueOf(String.valueOf(SEND_ERROR)).concat(String.valueOf(String.valueOf(this.getError()))));
		} else {
			try {
				this.out.write(message);
				this.fireEvent(new PEvent(8, this, message));
			} catch (PException var3) {
				this.fireEvent(new PEvent(16, this, message));
				this.setError(String.valueOf(String.valueOf(SEND_ERROR))
						.concat(String.valueOf(String.valueOf(this.explain(var3)))));
				throw var3;
			} catch (Exception var4) {
				this.fireEvent(new PEvent(16, this, message));
				this.setError(String.valueOf(String.valueOf(SEND_ERROR))
						.concat(String.valueOf(String.valueOf(this.explain(var4)))));
			}

		}
	}

	public String getName() {
		return this.name;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public int getReconnectInterval() {
		return this.reconnectInterval / 1000;
	}

	public String toString() {
		return String.valueOf(String.valueOf((new StringBuffer("PSocketConnection:")).append(this.name).append('(')
				.append(this.host).append(':').append(this.port).append(')')));
	}

	public int getReadTimeout() {
		return this.readTimeout / 1000;
	}

	public boolean available() {
		return this.error == null;
	}

	public String getError() {
		return this.error;
	}

	public Date getErrorTime() {
		return this.errorTime;
	}

	public synchronized void close() {
		try {
			if (this.socket != null) {
				this.socket.close();
				this.in = null;
				this.out = null;
				this.socket = null;
				if (this.heartbeatThread != null) {
					this.heartbeatThread.kill();
				}

				this.receiveThread.kill();
			}
		} catch (Exception var2) {
		}

		this.setError(NOT_INIT);
	}

	protected synchronized void connect() {
		if (this.error == NOT_INIT) {
			this.error = CONNECTING;
		} else if (this.error == null) {
			this.error = RECONNECTING;
		}

		this.errorTime = new Date();
		if (this.socket != null) {
			try {
				this.socket.close();
			} catch (IOException var5) {
			}
		}

		try {
			if (this.port > 0 && this.port <= 65535) {
				if (this.localPort >= -1 && this.localPort <= 65535) {
					if (this.localHost == null) {
						this.socket = new Socket(this.host, this.port);
					} else {
						boolean isConnected = false;
						InetAddress localAddr = InetAddress.getByName(this.localHost);
						if (this.localPort != -1) {
							this.socket = new Socket(this.host, this.port, localAddr, this.localPort);
						} else {
							for (int p = (int) (Math.random() * (double) 'ﯴ'); p < 903000; p += 13) {
								try {
									this.socket = new Socket(this.host, this.port, localAddr, 1025 + p % 'ﯴ');
									isConnected = true;
									break;
								} catch (IOException var6) {
								} catch (SecurityException var7) {
								}
							}

							if (!isConnected) {
								throw new SocketException("Can not find an avaliable local port");
							}
						}
					}

					this.socket.setSoTimeout(this.readTimeout);
					this.out = this.getWriter(this.socket.getOutputStream());
					this.in = this.getReader(this.socket.getInputStream());
					this.setError((String) null);
				} else {
					this.setError(
							String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(PORT_ERROR))))
									.append("local-port:").append(this.localPort))));
				}
			} else {
				this.setError(
						String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(PORT_ERROR))))
								.append("port:").append(this.port))));
			}
		} catch (IOException var8) {
			this.setError(String.valueOf(String.valueOf(CONNECT_ERROR))
					.concat(String.valueOf(String.valueOf(this.explain(var8)))));
		}
	}

	protected void setError(String desc) {
		if ((this.error != null || desc != null) && (desc == null || !desc.equals(this.error))) {
			this.error = desc;
			this.errorTime = new Date();
			if (desc == null) {
				desc = CONNECTED;
			}

		}
	}

	protected abstract PWriter getWriter(OutputStream var1);

	protected abstract PReader getReader(InputStream var1);

	protected abstract Resource getResource();

	protected void heartbeat() throws IOException {
	}

	public void initResource() {
		NOT_INIT = this.resource.get("comm/not-init");
		CONNECTING = this.resource.get("comm/connecting");
		RECONNECTING = this.resource.get("comm/reconnecting");
		CONNECTED = this.resource.get("comm/connected");
		HEARTBEATING = this.resource.get("comm/heartbeating");
		RECEIVEING = this.resource.get("comm/receiveing");
		CLOSEING = this.resource.get("comm/closeing");
		CLOSED = this.resource.get("comm/closed");
		UNKNOWN_HOST = this.resource.get("comm/unknown-host");
		PORT_ERROR = this.resource.get("comm/port-error");
		CONNECT_REFUSE = this.resource.get("comm/connect-refused");
		NO_ROUTE_TO_HOST = this.resource.get("comm/no-route");
		RECEIVE_TIMEOUT = this.resource.get("comm/receive-timeout");
		CLOSE_BY_PEER = this.resource.get("comm/close-by-peer");
		RESET_BY_PEER = this.resource.get("comm/reset-by-peer");
		CONNECTION_CLOSED = this.resource.get("comm/connection-closed");
		COMMUNICATION_ERROR = this.resource.get("comm/communication-error");
		CONNECT_ERROR = this.resource.get("comm/connect-error");
		SEND_ERROR = this.resource.get("comm/send-error");
		RECEIVE_ERROR = this.resource.get("comm/receive-error");
		CLOSE_ERROR = this.resource.get("comm/close-error");
	}

	protected String explain(Exception ex) {
		String msg = ex.getMessage();
		if (msg == null) {
			msg = "";
		}

		if (ex instanceof PException) {
			return ex.getMessage();
		} else if (ex instanceof EOFException) {
			return CLOSE_BY_PEER;
		} else if (msg.indexOf("Connection reset by peer") != -1) {
			return RESET_BY_PEER;
		} else if (msg.indexOf("SocketTimeoutException") != -1) {
			return RECEIVE_TIMEOUT;
		} else if (ex instanceof NoRouteToHostException) {
			return NO_ROUTE_TO_HOST;
		} else if (ex instanceof ConnectException) {
			return CONNECT_REFUSE;
		} else if (ex instanceof UnknownHostException) {
			return UNKNOWN_HOST;
		} else if (msg.indexOf("errno: 128") != -1) {
			return NO_ROUTE_TO_HOST;
		} else {
			ex.printStackTrace();
			return ex.toString();
		}
	}
}

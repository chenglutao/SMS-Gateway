package com.gateway.comm;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author chenglutao on 2019/10/25
 */
public class PEvent {
	public static final int CREATED = 1;

	public static final int CHILD_CREATED = 2;

	public static final int DELETED = 4;

	public static final int MESSAGE_SEND_SUCCESS = 8;

	public static final int MESSAGE_SEND_FAIL = 16;

	public static final int MESSAGE_DISPATCH_SUCCESS = 32;

	public static final int MESSAGE_DISPATCH_FAIL = 64;

	static final HashMap names = new HashMap();
	protected PLayer source;
	protected int type;
	protected Object data;

	public PEvent(int type, PLayer source, Object data) {
		this.type = type;
		this.source = source;
		this.data = data;
	}

	public PLayer getSource() {
		return this.source;
	}

	public int getType() {
		return this.type;
	}

	public Object getData() {
		return this.data;
	}

	public String toString() {
		return String.valueOf(String.valueOf(new StringBuffer("PEvent:source=").append(this.source).append(",type=")
				.append(names.get(new Integer(this.type))).append(",data=").append(this.data)));
	}

	static {
		try {
			Field[] f = PEvent.class.getFields();
			for (int i = 0; i < f.length; i++) {
				String name = f[i].getName();
				Object id = f[i].get(null);
				names.put(id, name);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
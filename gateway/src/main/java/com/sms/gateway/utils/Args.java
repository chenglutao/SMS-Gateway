package com.sms.gateway.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenglutao on 2019/10/25
 */
public class Args {
	public static final Args EMPTY = new Args().lock();
	boolean locked;
	Map args;

	public Args() {
		this(new HashMap());
	}

	public Args(Map theArgs) {
		if (theArgs == null) {
			throw new NullPointerException("argument is null");
		}
		this.args = theArgs;
	}

	public String get(String key, String def) {
		try {
			return this.args.get(key).toString();
		} catch (Exception ex) {
			return def;
		}
	}

	public int get(String key, int def) {
		try {
			return Integer.parseInt(this.args.get(key).toString());
		} catch (Exception ex) {
			return def;
		}
	}

	public long get(String key, long def) {
		try {
			return Long.parseLong(this.args.get(key).toString());
		} catch (Exception ex) {
			return def;
		}
	}

	public float get(String key, float def) {
		try {
			return Float.parseFloat(this.args.get(key).toString());
		} catch (Exception ex) {
			return def;
		}
	}

	public boolean get(String key, boolean def) {
		try {
			return "true".equals(this.args.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public Object get(String key, Object def) {
		Object localObject1;
		try {
			Object obj = this.args.get(key);
			if (obj == null) {
				return def;
			}
			return obj;
		} catch (Exception ex) {
			localObject1 = def;
		}
		return localObject1;
	}

	public Args set(String key, Object value) {
		if (this.locked) {
			throw new UnsupportedOperationException("Args have locked,can modify");
		}
		this.args.put(key, value);
		return this;
	}

	public Args set(String key, int value) {
		if (this.locked) {
			throw new UnsupportedOperationException("Args have locked,can modify");
		}
		this.args.put(key, new Integer(value));
		return this;
	}

	public Args set(String key, boolean value) {
		if (this.locked) {
			throw new UnsupportedOperationException("Args have locked,can modify");
		}
		this.args.put(key, new Boolean(value));
		return this;
	}

	public Args set(String key, long value) {
		if (this.locked) {
			throw new UnsupportedOperationException("Args have locked,can modify");
		}
		this.args.put(key, new Long(value));
		return this;
	}

	public Args set(String key, float value) {
		if (this.locked) {
			throw new UnsupportedOperationException("Args have locked,can modify");
		}
		this.args.put(key, new Float(value));
		return this;
	}

	public Args set(String key, double value) {
		if (this.locked) {
			throw new UnsupportedOperationException("Args have locked,can modify");
		}
		this.args.put(key, new Double(value));
		return this;
	}

	public Args lock() {
		this.locked = true;
		return this;
	}

	public String toString() {
		return this.args.toString();
	}
}
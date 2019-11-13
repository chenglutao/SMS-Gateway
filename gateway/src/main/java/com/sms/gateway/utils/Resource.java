package com.sms.gateway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * @author chenglutao on 2019/10/25
 */
public class Resource {
	private Cfg resource;

	public Resource(String url) throws IOException {
		init(url);
	}

	public Resource(Class c, String url) throws IOException {
		String className = c.getName();

		int i = className.lastIndexOf('.');
		if (i > 0) {
			className = className.substring(i + 1);
		}
		URL u = new URL(c.getResource(String.valueOf(String.valueOf(className)).concat(".class")), url);
		init(u.toString());
	}

	public void init(String url) throws IOException {
		String str = String.valueOf(String.valueOf(
				new StringBuffer(String.valueOf(String.valueOf(url))).append('_').append(Locale.getDefault())));
		InputStream in = null;
		while (true)
			try {
				this.resource = new Cfg(String.valueOf(String.valueOf(str)).concat(".xml"), false);
				return;
			} catch (IOException ex) {
				int i = str.lastIndexOf('_');
				if (i < 0) {
					throw new MissingResourceException(
							String.valueOf(String
									.valueOf(new StringBuffer("Can't find resource url:").append(url).append(".xml"))),
							getClass().getName(), null);
				}

				str = str.substring(0, i);
			}
	}

	public String get(String key) {
		return this.resource.get(key, key);
	}

	public String[] childrenNames(String key) {
		return this.resource.childrenNames(key);
	}

	public String get(String key, Object[] params) {
		String value = this.resource.get(key, key);
		try {
			return MessageFormat.format(value, params);
		} catch (Exception ex) {
			ex.printStackTrace();
			return key;
		}
	}

	public String get(String key, Object param) {
		return get(key, new Object[] { param });
	}

	public String get(String key, Object param1, Object param2) {
		return get(key, new Object[] { param1, param2 });
	}

	public String get(String key, Object param1, Object param2, Object param3) {
		return get(key, new Object[] { param1, param2, param3 });
	}
}
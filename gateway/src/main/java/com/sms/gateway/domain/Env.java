package com.sms.gateway.domain;

import java.io.IOException;

import com.sms.gateway.utils.Cfg;
import com.sms.gateway.utils.Resource;

/**
 * @author chenglutao on 2019/10/25
 */
public class Env {

	static Cfg config;
	static Resource resource;

	public static Cfg getConfig() {
		if (config == null) {
			try {
				String url = Env.class.getClassLoader().getResource("cmpp.xml").toString();
				config = new Cfg(url);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return config;
	}

	public static Resource getResource() {
		if (resource == null) {
			try {
				resource = new Resource("resource");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return resource;
	}
}

package com.sms.gateway.utils;

/**
 * @author chenglutao on 2019/10/25
 */
public abstract class WatchThread extends Thread {
	private boolean alive = true;

	private String state = "unknown";

	public static final ThreadGroup tg = new ThreadGroup("watch-thread");

	public WatchThread(String name) {
		super(tg, name);
		setDaemon(true);
	}

	public void kill() {
		this.alive = false;
	}

	public final void run() {
		while (this.alive)
			try {
				task();
			} catch (Exception ex) {
				ex.printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
			}
	}

	protected void setState(String newState) {
		this.state = newState;
	}

	/*
	 * public String getState() { return state; }
	 */
	protected abstract void task();
}
package com.gateway.comm;

/**
 * @author chenglutao on 2019/10/25
 */
public class PEventAdapter implements PEventListener {
	public void handle(PEvent e) {
		switch (e.getType()) {
		case 2:
			childCreated((PLayer) e.getData());

			break;
		case 1:
			created();

			break;
		case 4:
			deleted();

			break;
		case 64:
			messageDispatchFail((PMessage) e.getData());

			break;
		case 32:
			messageDispatchFail((PMessage) e.getData());

			break;
		case 8:
			messageDispatchFail((PMessage) e.getData());

			break;
		case 16:
			messageDispatchFail((PMessage) e.getData());

			break;
		}
	}

	public void childCreated(PLayer child) {
	}

	public void messageSendError(PMessage msg) {
	}

	public void messageSendSuccess(PMessage msg) {
	}

	public void messageDispatchFail(PMessage msg) {
	}

	public void messageDispatchSuccess(PMessage msg) {
	}

	public void created() {
	}

	public void deleted() {
	}
}
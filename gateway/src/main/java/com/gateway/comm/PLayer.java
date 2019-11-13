package com.gateway.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenglutao on 2019/10/25
 */
public abstract class PLayer {
	public static final int maxId = 1000000000;
	protected int id;
	protected int nextChildId;
	protected PLayer parent;
	private HashMap children;
	private List listeners;

	protected PLayer(PLayer theParent) {
		if (theParent != null) {
			this.id = (++theParent.nextChildId);
			if (theParent.nextChildId >= 1000000000) {
				theParent.nextChildId = 0;
			}
			if (theParent.children == null) {
				theParent.children = new HashMap();
			}
			theParent.children.put(new Integer(this.id), this);
			this.parent = theParent;
		}
	}

	public abstract void send(PMessage paramPMessage) throws PException;

	public void onReceive(PMessage message) {
		int childId = getChildId(message);
		if (childId == -1) {
			PLayer child = createChild();
			child.onReceive(message);
			fireEvent(new PEvent(2, this, child));
		} else {
			PLayer child = (PLayer) this.children.get(new Integer(getChildId(message)));
			if (child == null) {
				fireEvent(new PEvent(64, this, message));
			} else
				child.onReceive(message);
		}
	}

	public PLayer getParent() {
		return this.parent;
	}

	public int getChildNumber() {
		if (this.children == null) {
			return 0;
		}
		return this.children.size();
	}

	protected PLayer createChild() {
		throw new UnsupportedOperationException("Not implement");
	}

	protected int getChildId(PMessage message) {
		throw new UnsupportedOperationException("Not implement");
	}

	public void close() {
		if (this.parent == null) {
			throw new UnsupportedOperationException("Not implement");
		}
		this.parent.children.remove(new Integer(this.id));
	}

	public void addEventListener(PEventListener l) {
		if (this.listeners == null) {
			this.listeners = new ArrayList();
		}
		this.listeners.add(l);
	}

	public void removeEventListener(PEventListener l) {
		this.listeners.remove(l);
	}

	protected void fireEvent(PEvent e) {
		if (this.listeners == null) {
			return;
		}

		for (Iterator i = this.listeners.iterator(); i.hasNext();)
			((PEventListener) i.next()).handle(e);
	}
}
package org.cee.store.jcr;

import javax.jcr.Session;

public class PooledSession {
	
	private Session session;
	
	private Thread thread;

	public PooledSession(Session session, Thread thread) {
		this.session = session;
		this.thread = thread;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Session getSession() {
		return session;
	}
}
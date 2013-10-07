package org.cee.store.jcr;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalSessionManager implements SessionManager {
	
	private final static Logger LOG = LoggerFactory.getLogger(ThreadLocalSessionManager.class);

	private static Repository repository;
	
	private static Credentials credentials;
	
	private static final List<PooledSession> sessions = new ArrayList<PooledSession>();
	
	private static final ThreadLocal<Session> session = new ThreadLocal<Session>() {
		@Override
		protected synchronized Session initialValue() {
			try {
				for (PooledSession pooledSession : sessions) {
					if (!pooledSession.getThread().isAlive()) {
						pooledSession.setThread(Thread.currentThread());
						return pooledSession.getSession();
					}
				}
				Session session = repository.login(credentials);
				sessions.add(new PooledSession(session, Thread.currentThread()));
				LOG.info("Added new session, {} sessions are managed.", sessions.size());
				return session;
			} catch (Exception e) {
				throw new RuntimeException("Could not login to JCR repository", e);
			}
		}
	};
	
	public static void setRepository(Repository repository) {
		ThreadLocalSessionManager.repository = repository;
	}
	
	public static void setCredentials(Credentials credentials) {
		ThreadLocalSessionManager.credentials = credentials;
	}
	
	@Override
	public Session getSession() {
		return session.get();
	}
	
	public static synchronized void closeSessions() {
		LOG.info("Closing {} managed sessions.", sessions.size());
		for (PooledSession pooledSession : sessions) {
			pooledSession.getSession().logout();
		}
	}
}

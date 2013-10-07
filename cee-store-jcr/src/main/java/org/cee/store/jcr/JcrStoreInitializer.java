package org.cee.store.jcr;

import static org.cee.store.jcr.JcrStoreConstants.*;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;
import org.cee.news.store.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrStoreInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(JcrStoreInitializer.class);
	
	private static final String NODE_TYPES_CND_FILE = "NodeTypes.cnd";

	private Session session;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void registerNodeTypes() throws StoreException {
		Workspace workspace = session.getWorkspace();
		InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(NODE_TYPES_CND_FILE));
		try {
			CndImporter.registerNodeTypes(reader, "cnd",
					workspace.getNodeTypeManager(),
					workspace.getNamespaceRegistry(),
					session.getValueFactory(), true);
			Node root = session.getRootNode();
			if (!root.hasNode(NODE_CONTENT)) {
				root.addNode(NODE_CONTENT, NODE_CONTENT);
			}
			session.save();
			if(LOG.isDebugEnabled()) {
				LOG.debug("Initialized node types for workspace {}", workspace.getName());
			}
		} catch (RepositoryException e) {
			throw new StoreException("Could not initialize JCR store", e);
		} catch (ParseException e) {
			throw new StoreException("Could not initialize JCR store", e);
		} catch (IOException e) {
			throw new StoreException("Could not initialize JCR store", e);
		}
	}
}

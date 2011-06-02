package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.*;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.commons.cnd.ParseException;

import com.cee.news.store.StoreException;

public class JcrStoreInitializer {
    
    private static final String NODE_TYPES_CND_FILE = "NodeTypes.cnd";

    public void registerNodeTypes(Session session) throws StoreException {
        Workspace workspace = session.getWorkspace();
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(NODE_TYPES_CND_FILE));
        try {
            CndImporter.registerNodeTypes(reader, "cnd", workspace.getNodeTypeManager(), workspace.getNamespaceRegistry(), session.getValueFactory(), true);
            Node root = session.getRootNode();
            if (!root.hasNode(NODE_CONTENT)) {
                root.addNode(NODE_CONTENT, NODE_CONTENT);
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

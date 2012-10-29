package org.eclipse.jetty;

import java.net.InetSocketAddress;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Starter {

	/**
	 * Starts the jetty server using the war file in which this class is embedded
	 */
	public static void main(String[] args) {
		InetSocketAddress inetAddress = new InetSocketAddress("localhost", 8888);
		Server server = new Server(inetAddress);
		
		WebAppContext context = new WebAppContext();
	    context.setServer(server);
	    context.setContextPath("/");
	 
	    ProtectionDomain protectionDomain = Starter.class.getProtectionDomain();
	    URL location = protectionDomain.getCodeSource().getLocation();
	    context.setWar(location.toExternalForm());
	 
	    server.setHandler(context);
	    try {
	        server.start();
	        System.in.read();
	        server.stop();
	        server.join();
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.exit(100);
	    }
	}

}

package org.eclipse.jetty;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.ProtectionDomain;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Starter {

	/**
	 * Starts the jetty server using the war file in which this class is embedded
	 */
	public static void main(String[] args) {
		Options options = buildOptions();
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			String hostname = cmd.getOptionValue('h', "localhost");
			int port = parsePort(cmd);
			if (cmd.hasOption('l')) {
				String loggingFile = cmd.getOptionValue('l');
				if (!new File(loggingFile).exists()) {
					System.out.println("The provided logging configuration file " + loggingFile + " does not exists!");
					printHelp(options);
					return;
				}
				System.setProperty("log4j.configuration", "file:" + loggingFile);
			}
			if (cmd.hasOption('c')) {
				String propertiesFile = cmd.getOptionValue('c');
				if (!new File(propertiesFile).exists()) {
					System.out.println("The provided properties file " + propertiesFile + " does not exists!");
					printHelp(options);
					return;
				}
				System.setProperty("configFile", propertiesFile);
			}
			
			startServer(hostname, port);
			
		} catch (MissingArgumentException mae) {
			System.out.println("The option " + mae.getOption().getOpt() + " is missing required argument!");
			printHelp(options);
		} catch (UnrecognizedOptionException uoe) {
			System.out.println("Usage of unsupported option " + uoe.getOption() + "!");
			printHelp(options);
		} catch (ParseException pe) {
			System.out.println("Wrong command line arguments provided!");
			printHelp(options);
		}
	}

	private static Options buildOptions() {
		Options options = new Options();
		options.addOption("h","hostname",true,"Hostname or IP to bind webserver, default is localhost.");
		options.addOption("p","port",true,"Port number to use, default is 8888");
		options.addOption("c","conf",true,"Configuration properties file name. If omitted, internal configuration file default.properties will be used");
		options.addOption("l","logconf",true,"Configuration file for the log4j logging framework. Default configuration enabled debug level for the com.cee namespace and all logging to console");
		return options;
	}
	
	private static void printHelp(Options options) {
		new HelpFormatter().printHelp("java -jar newsreader.war", options);
	}
	
	private static void startServer(String hostname, int port) {
		InetSocketAddress inetAddress = new InetSocketAddress(hostname, port);
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
	        System.out.println("Visit http://" + inetAddress.getHostName() + ":" + port);
	        System.out.println("Press any key to stop server!");
	        System.in.read();
	        server.stop();
	        server.join();
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.exit(100);
	    }	
	}
	
	private static int parsePort(CommandLine cmd) {
		String portStr = cmd.getOptionValue('p', "8888");
		try {
			return Integer.parseInt(portStr);
		} catch (NumberFormatException nfe) {
			return 8888;
		}
	}
}

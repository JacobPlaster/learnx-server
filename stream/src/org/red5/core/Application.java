package org.red5.core;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 * 
 * Add jars:
 * 	- libs/red5-server-common
 * 	- red5-server
 *  - red5-service
 *  - libs/slf4j-api
 */

import java.io.IOException;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;
import org.red5.server.net.remoting.RemotingClient;
import org.slf4j.Logger;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends MultiThreadedApplicationAdapter {
	
	// Handle logs
	private static Logger log = Red5LoggerFactory.getLogger(Application.class);
	RemoteServerHandler rHandler;
	String conn_secret_key;

	/** {@inheritDoc} */
    @Override
	public boolean connect(IConnection conn, IScope scope, Object[] params) {
    	
    	log.info("\n\n[STREAM] New connection attempt from " + conn.getRemoteAddress());
    	conn_secret_key = "8a7df9a87f89dyf89ad7f89dhasdhd9f8a";
    	
    	// Connect to remote server
    	try 
    	{
    		rHandler = new RemoteServerHandler(conn, log);
    		rHandler.connectToServer();
    	} catch (Exception e) 
    	{
    		log.error("[STREAM] Unable to connect \""+ conn.getRemoteAddress() +"\" to flash services");
    		return false;
    	}
    	
    	// Authenticate users connection
    	if(!rHandler.AuthenticateAndInitialise(conn_secret_key))
    	{
    		log.info("[STREAM] User ("+ conn.getRemoteAddress() +") does not have access.");
    		return false;
    	} else
    	{
    		log.info("[STREAM] Authenticated user ("+ conn.getRemoteAddress() +") successfully.");
    	}
    	
    	
    	log.info("[STREAM] Connection with user ("+ conn.getRemoteAddress() +") fully established.");
    	return true;   
	}
    
   
    
    

	/** {@inheritDoc} */
    @Override
	public void disconnect(IConnection conn, IScope scope) {
    	log.info("[STREAM] Connection closed by " + conn.getRemoteAddress());
    	rHandler.DisconnectFromServer(conn_secret_key);
		super.disconnect(conn, scope);
	}

}

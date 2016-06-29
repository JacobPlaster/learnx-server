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
import java.util.Map;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.exception.ClientRejectedException;
import org.slf4j.Logger;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends MultiThreadedApplicationAdapter{
	
	// Handle logs
	private static Logger log = Red5LoggerFactory.getLogger(Application.class);
	RemoteServerHandler rHandler;
	String conn_secret_key;
	String queryString;
	
	String authTargetRoomName;

	/** {@inheritDoc} */
    @Override
	public synchronized boolean connect(IConnection conn, IScope scope, Object[] params) {
    	/*if (!super.connect(conn, scope, params))
           return false; */
    	
    	log.info("[STREAM - APPLICATION] connect");
    	// Connect to remote server
    	try 
    	{
    		rHandler = new RemoteServerHandler(conn, log);
    		rHandler.connectToServer();
    		queryString = String.valueOf(conn.getConnectParams().get("queryString")); 
    	} catch (Exception e) 
    	{
    		this.rejectClient("Unable to connect to flash services.");
    	}
    	return true;   
	}
    
    /** {@inheritDoc} */
    @Override
    public void streamPublishStart(IBroadcastStream stream) {
    	log.info("[STREAM - APPLICATION] streamPublishStart");
    	
    	// parse secret from query string
    	conn_secret_key = ServiceFunctions.parseQueryForSecret(queryString);
    	// no secret in query
    	if(conn_secret_key == null) this.rejectClient("No secret found.");
    	
    	/// authenticate error string against room name (needs to be associated with user)
    	String response = rHandler.AuthenticateAndInitialise(conn_secret_key);
    	if(response == "404")
    	{
    		this.rejectClient("Invalid secret.");
    	} else
    	{
    		authTargetRoomName = response;
    	}
    	
    	// Checks if room the client is publishing to is the same name as the user linked to the secret key
        if(!stream.getPublishedName().equals(authTargetRoomName))
        {
        	this.rejectClient("Incorrect authentication details.");
        }
        super.streamPublishStart(stream);
    }
    
	/** {@inheritDoc} */
    @Override
	public void disconnect(IConnection conn, IScope scope) {
    	log.info("[STREAM - APPLICATION] disconnect");
    	rHandler.DisconnectFromServer(conn_secret_key);
		super.disconnect(conn, scope);
	}

}

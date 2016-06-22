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

	/** {@inheritDoc} */
    @Override
	public boolean connect(IConnection conn, IScope scope, Object[] params) {
    	log.info("[STREAM] New connection attempt from " + conn.getRemoteAddress());
    	
    	try 
    	{
    		// was working with amfphp 2.2 but was returning long double
    		// amfphp 1.6 is compatable with red5 but requires php 5.4 (slow af) and mamp doesnt support
    		// amf_zend is an alternative but effort to setup and no red5 documentation 
    		//RemotingClient client = new RemotingClient("http://localhost/learnx-service/flash-services/index.php");
    		RemotingClient client = new RemotingClient("http://localhost/learnx-service/flash-services/gateway.php");
    		
    		Object[] args = new Object[]{"admin","admin"};
    		System.out.println("\n\n-- Connect called: --\n\n");
    		
    		Object result = client.invokeMethod("red5service.login", args);
    		
    		// AMFPHP 2.2.2
    		//Object result = client.invokeMethod("service.red5service.login", args);
    		
    		System.out.println("[AMFPHP response] "+ result);

    		if(!result.equals(true)) 
			{
    			log.info("[STREAM] User ("+ conn.getRemoteAddress() +") does not have access.");
				return false;  
			}
		} catch (Exception e) 
    	{
			log.info("[STREAM] Unable to connect \""+ conn.getRemoteAddress() +"\" to flash services");
    		return false;
    	}
    	log.info("[STREAM] \""+ conn.getRemoteAddress() +"\" connected to flash services successfully");
    	return true;   
	}

	/** {@inheritDoc} */
    @Override
	public void disconnect(IConnection conn, IScope scope) {
    	log.info("[STREAM] Connection closed by " + conn.getRemoteAddress());
		super.disconnect(conn, scope);
	}

}

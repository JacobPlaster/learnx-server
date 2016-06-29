package org.red5.core;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IConnection;
import org.red5.server.net.remoting.RemotingClient;
import org.slf4j.Logger;

public class RemoteServerHandler {
	
	private static Logger log;
	IConnection conn;
	RemotingClient client;
	
	// main 
	public static String CONNECTION_URL = "http://localhost/learnx-service/flash-services/gateway.php";
	// FUNCTIONS
	public static String AUTHENTICATE_AND_INITIALISE_FUNCTION = "red5service.AuthenticateAndInitialise";
	public static String DISCONNECT_STREAM_FUNCTION = "red5service.DisconnectStream";
	
	
	public RemoteServerHandler(IConnection inConnection, Logger inLog)
	{
		this.log = inLog;
		this.conn = inConnection;
	}
	
	public RemotingClient connectToServer()
    {
		this.client = new RemotingClient(CONNECTION_URL);
		return this.client;
    }
	
	public String AuthenticateAndInitialise(String secureKey)
    {
		Object result;
    	try 
    	{
    		Object[] args = new Object[]{secureKey};
    		result = client.invokeMethod(AUTHENTICATE_AND_INITIALISE_FUNCTION , args);
		} catch (Exception e) 
    	{
			log.info("[STREAM] Unable to connect \""+ conn.getRemoteAddress() +"\" to flash services");
    		return "404";
    	}
    	// Successfull 
    	return (String) result;
    }
	
	public void DisconnectFromServer(String secureKey)
	{
		try 
    	{
    		Object[] args = new Object[]{secureKey};
    		Object result = client.invokeMethod(DISCONNECT_STREAM_FUNCTION, args);
		} catch (Exception e) 
    	{
			log.error("[STREAM] Unable to diconnect \""+ conn.getRemoteAddress() +"\" from remote servicer");
    	}
	}

}

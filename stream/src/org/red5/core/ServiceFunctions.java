package org.red5.core;

public class ServiceFunctions {
	
	/**
	 * Parses through the given string to check if it contains the secret key. If does not then returns null
	 * @param queryString
	 * @return secret key
	 */
	public static String parseQueryForSecret(String queryString)
	{
		if(queryString.length() < 3)
			return null;
		
		// ?secret=02012012
		// ?user=admin&pwd=admin
		// current secret 02012012
		// remove question mark to show query
		queryString = queryString.substring(1);
		String[] elements = queryString.split("(=)|(&)");
		
		for(int i = 0; i < elements.length; i++)
		{
			// thne next item is going to be the secret
			if(elements[i].equals("secret") && elements.length > i+1)
			{
				return elements[i+1];
			}
		}
		return null;
	}

}

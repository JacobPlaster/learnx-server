package org.red5.core;

public class ServiceFunctions {
	
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
			if(elements[i].equals("secret") && elements.length > i+1)
			{
				return elements[i+1];
			}
		}
		return null;
	}

}

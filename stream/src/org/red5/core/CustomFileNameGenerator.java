package org.red5.core;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IStreamFilenameGenerator;
import org.slf4j.Logger;

public class CustomFileNameGenerator implements IStreamFilenameGenerator {
	
	private static Logger log = Red5LoggerFactory.getLogger(CustomFileNameGenerator.class);
	
	


	@Override
	public String generateFilename(IScope scope, String name, GenerationType type) {
		log.debug("Filename (before): "+name);
		return null;
	}

	@Override
	public String generateFilename(IScope scope, String name, String extension, GenerationType type) {
		log.debug("Filename (before): "+name);
		return null;
	}

	@Override
	public boolean resolvesToAbsolutePath() {
		// TODO Auto-generated method stub
		return false;
	}

}

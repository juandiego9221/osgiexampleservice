package pe.com.jdmm21.felix.bookshelf.service.impl;

public class SessionNotValidRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 961135440693610206L;

	public SessionNotValidRuntimeException(String session) {
		super("Session not valid " + session);
	}

}

package pe.com.jdmm21.felix.bookshelf.service.api;

public class InvalidCredentialsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3572914260271220246L;

	public InvalidCredentialsException(String username) {
		super("Invalid credentials for " + username);
	}

}

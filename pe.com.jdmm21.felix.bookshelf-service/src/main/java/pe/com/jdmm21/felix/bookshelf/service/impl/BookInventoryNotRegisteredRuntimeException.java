package pe.com.jdmm21.felix.bookshelf.service.impl;

public class BookInventoryNotRegisteredRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2368913468135266313L;

	public BookInventoryNotRegisteredRuntimeException(String className) {
		super("Bookinvetory not registered, looking unser: " + className);
	}

}

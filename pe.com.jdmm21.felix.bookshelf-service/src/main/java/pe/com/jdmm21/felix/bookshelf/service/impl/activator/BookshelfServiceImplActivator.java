package pe.com.jdmm21.felix.bookshelf.service.impl.activator;

import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pe.com.jdmm21.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import pe.com.jdmm21.felix.bookshelf.inventory.api.InvalidBookException;
import pe.com.jdmm21.felix.bookshelf.service.api.BookshelfService;
import pe.com.jdmm21.felix.bookshelf.service.api.InvalidCredentialsException;
import pe.com.jdmm21.felix.bookshelf.service.impl.BookshelfServiceImpl;

public class BookshelfServiceImplActivator implements BundleActivator {

	ServiceRegistration reg = null;

	@Override
	public void start(BundleContext context) throws Exception {
		this.reg = context.registerService(BookshelfService.class.getName(), new BookshelfServiceImpl(context), null);
		//testService(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (this.reg != null) {
			context.ungetService(reg.getReference());
		}
	}

	private void testService(BundleContext context) {
		String name = BookshelfService.class.getName();
		ServiceReference ref = context.getServiceReference(name);
		if (ref == null) {
			throw new RuntimeException("Service not refistered" + name);
		}
		BookshelfService service = (BookshelfService) context.getService(ref);
		String sessionId;
		try {
			System.out.print("\nSigning in ...");
			sessionId = service.login("admin", "admin".toCharArray());
		} catch (InvalidCredentialsException e) {
			e.printStackTrace();
			return;
		}

		try {
			System.out.println("adding books ...");
			service.addBook(sessionId, "123-4567890100", "Book 1 Title", "John Doe", "Group 1", 0);
			service.addBook(sessionId, "123-4567890101", "Book 2 Title", "Will Smith", "Group 1", 0);
			service.addBook(sessionId, "123-4567890200", "Book 3 Title", "John Doe", "Group 2", 0);
			service.addBook(sessionId, "123-4567890201", "Book 4 Title", "Jane Doe", "Group 2", 0);
		} catch (BookAlreadyExistsException e) {
			e.printStackTrace();
			return;
		} catch (InvalidBookException e) {
			e.printStackTrace();
			return;
		}

//		String authorLike = "%Doe";
//		System.out.println("Searching for books with author like: " + authorLike);
//		Set<String> results = service.searchBooksByAuthor(sessionId, authorLike);
//		for (String isbn : results) {
//			try {
//				System.out.println("-" + service.getBook(sessionId, isbn));
//			} catch (Exception e) {
//				System.err.println(e.getMessage());
//			}
//		}
	}

}

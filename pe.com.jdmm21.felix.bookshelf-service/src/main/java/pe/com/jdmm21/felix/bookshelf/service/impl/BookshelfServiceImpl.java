package pe.com.jdmm21.felix.bookshelf.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pe.com.jdmm21.felix.bookshelf.inventory.api.Book;
import pe.com.jdmm21.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import pe.com.jdmm21.felix.bookshelf.inventory.api.BookInventory;
import pe.com.jdmm21.felix.bookshelf.inventory.api.BookInventory.SearchCriteria;
import pe.com.jdmm21.felix.bookshelf.inventory.api.BookNotfoundException;
import pe.com.jdmm21.felix.bookshelf.inventory.api.InvalidBookException;
import pe.com.jdmm21.felix.bookshelf.inventory.api.MutableBook;
import pe.com.jdmm21.felix.bookshelf.service.api.BookshelfService;
import pe.com.jdmm21.felix.bookshelf.service.api.InvalidCredentialsException;

public class BookshelfServiceImpl implements BookshelfService {

	private String sessionId;
	private BundleContext context;

	public BookshelfServiceImpl(BundleContext context) {
		this.context = context;
	}

	public String login(String username, char[] password) throws InvalidCredentialsException {
		if ("admin".equals(username) && Arrays.equals(password, "admin".toCharArray())) {
			this.sessionId = Long.toString(System.currentTimeMillis());
			return this.sessionId;
		}
		throw new InvalidCredentialsException(username);
	}

	public void logout(String sessionId) {
		checkSession(sessionId);
		this.sessionId = null;
	}

	public boolean sessionIsValid(String sessionId) {
		return this.sessionId != null && this.sessionId.equals(sessionId);
	}

	protected void checkSession(String sessionId) {
		if (!sessionIsValid(sessionId)) {
			throw new SessionNotValidRuntimeException(sessionId);
		}
	}

	private BookInventory lookupbookInventory() {
		ServiceReference ref = this.context.getServiceReference(BookInventory.class.getName());
		if (ref == null) {
			throw new BookInventoryNotRegisteredRuntimeException(BookInventory.class.getName());
		}
		return (BookInventory) this.context.getService(ref);
	}

	@Override
	public Set<String> getCategories(String sessionId) {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		return inv.getCategories();
	}

	@Override
	public void modifyBookCategory(String sessionId, String isbn, String title, String author, String category,
			int rating) throws BookNotfoundException, InvalidBookException {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		MutableBook book = inv.loadBookForEdit(isbn);
		book.setCategory(category);
		inv.storeBook(book);
	}

	@Override
	public void modifyBookRating(String sessionId, String isbn, int rating)
			throws InvalidBookException, BookNotfoundException {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		MutableBook book = inv.loadBookForEdit(isbn);
		book.setRating(rating);
		inv.storeBook(book);
	}

	@Override
	public void removeBook(String sessionId, String isbn) throws BookNotfoundException {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		inv.removeBook(isbn);
	}

	@Override
	public Book getBook(String session, String isbn) throws BookNotfoundException {
		checkSession(sessionId);
		BookInventory bookInventory = lookupbookInventory();
		return bookInventory.loadBook(isbn);
	}

	@Override
	public Set<String> searchBooksByCategory(String sessionId, String groupLike) {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.CATEGORY_LIKE, groupLike);
		return inv.searchBooks(criteria);
	}

	@Override
	public Set<String> searchBooksByAuthor(String sessionId, String authorLike) {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.AUTHOR_LIKE, authorLike);
		return inv.searchBooks(criteria);
	}

	@Override
	public Set<String> searchBooksByTitle(String sessionId, String titleLike) {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.TITLE_LIKE, titleLike);
		return inv.searchBooks(criteria);
	}

	@Override
	public Set<String> searchBooksByRating(String sessionId, int gradeLower, int gradeUpper) {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
		criteria.put(SearchCriteria.RATING_LT, Integer.toString(gradeLower));
		criteria.put(SearchCriteria.RATING_GT, Integer.toString(gradeUpper));
		return inv.searchBooks(criteria);
	}

	public MutableBook getBookForEdit(String sessionId, String isbn) throws BookNotfoundException {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		return inv.loadBookForEdit(isbn);
	}

	@Override
	public void addBook(String session, String isbn, String title, String author, String category, int rating)
			throws BookAlreadyExistsException, InvalidBookException {
		checkSession(sessionId);
		BookInventory inv = lookupbookInventory();
		MutableBook book = inv.createBook(isbn);
		book.setTitle(title);
		book.setAuthor(author);
		book.setCategory(category);
		book.setRating(rating);
		inv.storeBook(book);
	}

}

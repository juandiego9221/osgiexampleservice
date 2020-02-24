package pe.com.jdmm21.felix.bookshelf.service.api;

import java.util.Set;

import pe.com.jdmm21.felix.bookshelf.inventory.api.Book;
import pe.com.jdmm21.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import pe.com.jdmm21.felix.bookshelf.inventory.api.BookNotfoundException;
import pe.com.jdmm21.felix.bookshelf.inventory.api.InvalidBookException;

public interface BookshelfService extends Authentication {
	Set<String> getCategories(String sessionId);

	void addBook(String session, String isbn, String title, String author, String category, int rating)
			throws BookAlreadyExistsException, InvalidBookException;

	void modifyBookCategory(String session, String isbn, String title, String author, String category, int rating)
			throws BookNotfoundException, InvalidBookException;

	void modifyBookRating(String session, String isbn, int  rating)
			throws InvalidBookException, BookNotfoundException;

	void removeBook(String session, String isbn) throws BookNotfoundException;

	Book getBook(String session, String isbn) throws BookNotfoundException;

	Set<String> searchBooksByCategory(String session, String categoryLink);

	Set<String> searchBooksByAuthor(String session, String authorLike);

	Set<String> searchBooksByTitle(String session, String titleLike);

	Set<String> searchBooksByRating(String session, int ratingLower, int ratingUpper);

}

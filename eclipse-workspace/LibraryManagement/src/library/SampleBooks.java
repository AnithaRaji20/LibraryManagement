package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleBooks {

	    public static List<Book> createBooks() {
	        List<Book> booksList = new ArrayList<>();
	        
	        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", LocalDate.of(2024, 11, 15));
	        Book book2 = new Book("1984", "George Orwell", "9780451524935", LocalDate.of(2024, 12, 1));
	        Book book3 = new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084");
	        Book book4 = new Book("Moby Dick", "Herman Melville", "9780142437247");
	        Book book5 = new Book("War and Peace", "Leo Tolstoy", "9780192833948");
	        Book book6 = new Book("Pride and Prejudice", "Jane Austen", "9781503290563", LocalDate.of(2024, 11, 28));
	        Book book7 = new Book("The Catcher in the Rye", "J.D. Salinger", "9780316769488");
	        Book book8 = new Book("Crime and Punishment", "Fyodor Dostoevsky", "9780140449136");
	        Book book9 = new Book("The Odyssey", "Homer", "9780140268867");
	        Book book10 = new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 15));
	        Book book11 = new Book("Frankenstein", "Mary Shelley", "9780486282114");
	        Book book12 = new Book("Dracula", "Bram Stoker", "9780485126242");
	        Book book13 = new Book();
	        book13.setTitle("One Indian Girl");
	        book13.setAuthor("Chetan Bhagat");
	        book13.setStatus(null);
	        
	               
	        booksList.add(book1);
	        booksList.add(book2);
	        booksList.add(book3);
	        booksList.add(book4);
	        booksList.add(book5);
	        booksList.add(book6);
	        booksList.add(book7);
	        booksList.add(book8);
	        booksList.add(book9);
	        booksList.add(book10);
	        booksList.add(book11);
	        booksList.add(book12);
	        booksList.add(book13);
	        
	        return booksList;
	    }
	}

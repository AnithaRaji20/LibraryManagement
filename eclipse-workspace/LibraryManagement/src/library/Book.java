package library;

import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private BookStatus status;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    
    public Book() {
    	
    }
    
    // Parameterized Constructor for Book
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;
        this.checkoutDate = null;
        this.dueDate = null;
    }
    
    //method overloading 
    public Book(String title, String author, String isbn, LocalDate checkoutDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;
        this.checkoutDate = checkoutDate;
        this.dueDate = null;
    }

    public Book(String title, String author, String isbn, LocalDate checkoutDate, LocalDate dueDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = BookStatus.OVERDUE;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }
    
    // Getter and Setter methods for encapsulation
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Overriding toString() for displaying book info
    @Override
    public String toString() {
        return String.format("Title: %s, Author: %s, ISBN: %s, Status: %s, Due Date: %s", 
                              title, author, isbn, status, checkoutDate, dueDate);
    }
    
    public boolean isOverdue() {
        return this.status == BookStatus.OVERDUE;
    }
    
    public boolean isCheckedOut() {
        return this.status == BookStatus.CHECKED_OUT;
    }

    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
        this.checkoutDate = null;
    }

}

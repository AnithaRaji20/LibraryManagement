package library;

import java.util.concurrent.Callable;
import java.time.LocalDate;

public class CheckoutTask implements Callable<String> {
    private final Library library;
    private final Book book;
    private final Customer customer;
    private final LocalDate checkoutDate;
    private final PaymentStatus paymentStatus;

    public CheckoutTask(Library library, Book book, Customer customer, LocalDate checkoutDate, PaymentStatus paymentStatus) {
        this.library = library;
        this.book = book;
        this.customer = customer;
        this.checkoutDate = checkoutDate;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String call() throws Exception {
        synchronized (library) {  // Ensure thread safety for shared resources
            // Assuming you already have a method to check if a book is available
            if (Loanable.isAvailable(book)) {
                library.checkoutBook(book, customer, checkoutDate, paymentStatus);  // Perform checkout
                return customer.getName() + " has checked out the book: " + book.getTitle();
            } else {
                return "Book not available: " + book.getTitle();  // If not available
            }
        }
    }
}

package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Library {
    private List<Book> books = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Staff> staffs = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();; // List to store transactions
    private List<BookTransaction> bookTransactions = new ArrayList<>();
    private Map<String, Book> bookMap;

    public Library() {
        this.books = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.bookTransactions = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    public void addStaff(Staff staff) {
        staffs.add(staff);
    }
    
    public void displayBooks() {
    	StringBuilder builder = new StringBuilder();
        for (Book book : books) {
            builder.append(book).append("\n");
        }
        System.out.println(builder.toString());
    }
     
    public List<Book> searchBooks(String... keywords) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (Arrays.asList(keywords).contains(book.getTitle()) || Arrays.asList(keywords).contains(book.getAuthor())) {
                result.add(book);
            }
        }
        return result;
    }

    public void checkoutBook(Book book, Customer customer, LocalDate checkoutDate, PaymentStatus paymentStatus) 
            throws IllegalArgumentException, InvalidCheckoutDateException  {
      
    	long checkedOutBooks = 0;

    	// Inform the user about the maximum books allowed for the customer
        //System.out.println(customer.getName() + " can check out up to " + customer.getMaxBooksAllowed() + " books.");
        customer.displayInfo();
        
        // Defensive copy: Create a new Book object to avoid modifying the original
        final Book bookCopy = new Book(book.getTitle(), book.getAuthor(), book.getIsbn(), book.getCheckoutDate());

        if (checkoutDate == null) {
            checkoutDate = LocalDate.now();
        }

        if (checkoutDate.isAfter(LocalDate.now())) {
            throw new InvalidCheckoutDateException("Checkout date cannot be in the Future.");
        }

        for (Transaction transaction : transactions) {
            // Check if the transaction belongs to the given customer and the book is checked out
            if (transaction.getCustomer().equals(customer) && transaction.getBook().getStatus() == BookStatus.CHECKED_OUT) {
                checkedOutBooks++;  // Increment the counter if the conditions match
            }
        }

        // Check if the customer has already checked out the max allowed books
        if (checkedOutBooks >= customer.getMaxBooksAllowed()) {
            System.out.println("Checkout failed: You have already checked out the maximum allowed books.");
            return; // Exit the method if the customer has reached their limit
        }
        
        // If payment status is provided, check it
        if (paymentStatus != null) {
            // Validate the payment status (only allow checkout if payment is "Paid")
            if (paymentStatus instanceof Paid) {
                System.out.println("Payment Completed Successfully");
            } else {
                // If payment is not completed , we don't allow checkout
                System.out.println("Checkout failed: " + paymentStatus.statusMessage());
                return;  // Exit the method as checkout cannot proceed
            }
        }
        
        // Usage of default method in an interface to check if the book is available
        if (Loanable.isAvailable(book)) {
            System.out.println("Book is available. Proceeding with checkout...");
            new Loanable() {}.checkout(customer, bookCopy);
            book.setStatus(BookStatus.CHECKED_OUT);

            LocalDate dueDate = checkoutDate.plusDays(14); // Assuming 14 days for checkout
            book.setDueDate(dueDate);
            book.setCheckoutDate(checkoutDate);
            
            // Record the transaction
            Transaction transaction = new Transaction(book, customer, checkoutDate, dueDate);
            transactions.add(transaction);
        } else {
            System.out.println("Book is not available for checkout.");
        }
    }

    public void checkoutBook(Book book, Staff staff,LocalDate checkoutDate) throws IllegalArgumentException, InvalidCheckoutDateException {
    	
    	//Effective final ( checkoutDateLocal cannot be changed once assigned)
    	var checkoutDateLocal = (checkoutDate != null) ? checkoutDate : LocalDate.now();
    	
    	if (checkoutDate.isAfter(LocalDate.now())) {
             throw new InvalidCheckoutDateException("Checkout date cannot be in the Future.");
        }

    	if (Loanable.isAvailable(book)) {
            System.out.println("Book is available. Proceeding with checkout...");
            System.out.println(staff.getName() + " has checked out " + book.getTitle()); 
            book.setStatus(BookStatus.CHECKED_OUT);
            LocalDate dueDate = checkoutDateLocal.plusDays(14); 
            book.setDueDate(dueDate);
            book.setCheckoutDate(checkoutDateLocal);
            
		  BookTransaction recordTransaction = new BookTransaction(book, staff, checkoutDate, dueDate);
		  bookTransactions.add(recordTransaction);
    	} else {
            System.out.println("Book is not available for checkout.");
        }
	 }
	 
    public void displayTransactions() {
    	if (!transactions.isEmpty()) {
            transactions.forEach(System.out::println);
        } 
        if (!bookTransactions.isEmpty()) {
            bookTransactions.forEach(System.out::println);
        }
    }
    
    public void returnBook(Book book) {
        if (!book.isCheckedOut()) {
            throw new IllegalArgumentException("The book was not checked out."); // Unchecked exception (runtime exceptions)
        }
        book.returnBook();  // Change the book status to AVAILABLE or whatever status is appropriate after return

        // Handle transactions involving customers (Customer-related return)
        transactions.stream()
                    .filter(t -> t.getBook().equals(book))
                    .forEach(t -> System.out.println("Customer returned: " + t)); 

        // Handle transactions involving staff (Staff-related return)
        bookTransactions.stream()
                        .filter(t -> t.book().equals(book)) // Matching by book in BookTransaction
                        .forEach(t -> System.out.println("Staff returned: " + t)); 
    }
    
    public List<Book> filterBooks(Predicate<Book> condition) {
        return books.stream().filter(condition).collect(Collectors.toList());
    }

    public void showOverdueBooks() {
    	//predicate is used here to check whether the status of the book is overdue or not 
        Predicate<Book> overduePredicate = book -> book.getStatus() == BookStatus.OVERDUE;
        var overdueBooks = filterBooks(overduePredicate);
        overdueBooks.forEach(System.out::println);
    }
    
    //Null case matching - java 22 enhancement
    public void printBookStatus(Book book) {
        switch (book.getStatus()) {
            case AVAILABLE -> System.out.println("The book is available.");
            case CHECKED_OUT -> System.out.println("The book is checked out.");
            case OVERDUE -> System.out.println("The book is overdue.");
            case null -> System.out.println("Book status is unknown."); 
            default -> throw new IllegalStateException("Unexpected value: " + book.getStatus());
        }
    }
    
    public Customer findCustomerByName(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null; 
    }
    
    public Staff findStaffByName(String name) {
        for (Staff staff : staffs) {
            if (staff.getName().equalsIgnoreCase(name)) {
                return staff;
            }
        }
        return null; 
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null; 
    }
    
    public Map<String, List<Book>> groupBooksByAuthor() {
        return (Map)this.books.stream().collect(Collectors.groupingBy(Book::getAuthor));
    }
    
    public Map<Boolean, List<Book>> partitionByAvailability() {
        return (Map)this.books.stream().collect(Collectors.partitioningBy((book) -> book.getStatus() == BookStatus.AVAILABLE));
    }
   
	public Transaction getLatestTransaction() {
        if (transactions.isEmpty()) {
            return null;  
        }
        return transactions.get(transactions.size() - 1);
    }

	public class InvalidCheckoutDateException extends Exception {   // checked exception (compile time exceptions)
        public InvalidCheckoutDateException(String message) {
            super(message);
        }
    }


}


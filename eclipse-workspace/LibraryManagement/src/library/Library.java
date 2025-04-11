package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Library {
    private List<Book> books = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Staff> staffs = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();; // List to store transactions
    private List<BookTransaction> bookTransactions = new ArrayList<>();
    private Map<String, Book> bookMap;
    private BookSorter bookSorter;

    public Library(Locale locale) {
        this.books = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.bookTransactions = new ArrayList<>();
        this.bookMap = new HashMap<>();
        this.bookSorter = new BookSorter();
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
    
    public void addBookByIsbn(Book book) {
        bookMap.put(book.getIsbn(), book);
    }

    public Book getBookByIsbn(String isbn) {
        return bookMap.get(isbn);
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
            throw new InvalidCheckoutDateException(locManager.getMessage("error.unhandled", "Future checkout date"));
        }

        for (Transaction transaction : transactions) {
            // Check if the transaction belongs to the given customer and the book is checked out
            if (transaction.getCustomer().equals(customer) && transaction.getBook().getStatus() == BookStatus.CHECKED_OUT) {
                checkedOutBooks++;  // Increment the counter if the conditions match
            }
        }

        // Check if the customer has already checked out the max allowed books
        if (checkedOutBooks >= customer.getMaxBooksAllowed()) {
        	String errorMsg = locManager.getMessage("checkout.failed", book.getTitle(), "max books limit reached");
            System.out.println(errorMsg);
        	TransactionLogger.logTransaction(errorMsg);
        	return;
        }
        
        if (paymentStatus != null) {
            if (paymentStatus instanceof Paid) {
                System.out.println("Payment Completed Successfully");
            } else {
                System.out.println("Checkout failed: " + paymentStatus.statusMessage());
                return;  
            }
        }
        
        if (Loanable.isAvailable(book)) {
        	System.out.println(locManager.getMessage("book.available"));
            new Loanable() {}.checkout(customer, bookCopy);
            book.setStatus(BookStatus.CHECKED_OUT);

            LocalDate dueDate = checkoutDate.plusDays(14); // Assuming 14 days for checkout
            book.setDueDate(dueDate);
            book.setCheckoutDate(checkoutDate);
            
            Transaction transaction = new Transaction(book, customer, checkoutDate, dueDate);
            transactions.add(transaction);
            String successMsg = locManager.getMessage("checkout.successful", book.getTitle());
            System.out.println(successMsg);
            TransactionLogger.logTransaction(successMsg + " by " + customer.getName() +
                    " on " + checkoutDate + ", due " + dueDate);
        } else {
        	String errorMsg = locManager.getMessage("checkout.failed", book.getTitle(), "book unavailable");
            System.out.println(errorMsg);
        	TransactionLogger.logTransaction(errorMsg);
        }
    }

    public void checkoutBook(Book book, Staff staff,LocalDate checkoutDate) throws IllegalArgumentException, InvalidCheckoutDateException {
    	
    	//Effective final ( checkoutDateLocal cannot be changed once assigned)
    	var checkoutDateLocal = (checkoutDate != null) ? checkoutDate : LocalDate.now();
    	
    	if (checkoutDate.isAfter(LocalDate.now())) {
             throw new InvalidCheckoutDateException(locManager.getMessage("error.unhandled", "Future checkout date"));
        }

    	if (Loanable.isAvailable(book)) {
    		System.out.println(locManager.getMessage("book.available"));
            System.out.println(staff.getName() + " has checked out " + book.getTitle()); 
            book.setStatus(BookStatus.CHECKED_OUT);
            LocalDate dueDate = checkoutDateLocal.plusDays(14); 
            book.setDueDate(dueDate);
            book.setCheckoutDate(checkoutDateLocal);
            
		  BookTransaction recordTransaction = new BookTransaction(book, staff, checkoutDate, dueDate);
		  bookTransactions.add(recordTransaction);
		  TransactionLogger.logTransaction(locManager.getMessage("checkout.successful", book.getTitle()) +
                  " by staff " + staff.getName() + " on " + checkoutDateLocal + ", due " + dueDate);
    		} else {
          String errorMsg = locManager.getMessage("checkout.failed", book.getTitle(), "book unavailable");
          System.out.println(errorMsg);
          TransactionLogger.logTransaction(errorMsg + " by staff " + staff.getName());
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
        	String errorMsg = locManager.getMessage("return.failed", book.getTitle());
            TransactionLogger.logTransaction(errorMsg + " - Book was not checked out");
            throw new IllegalArgumentException(errorMsg);
        	}
        book.returnBook();  // Change the book status to AVAILABLE or whatever status is appropriate after return

        // Handle transactions involving customers (Customer-related return)
        transactions.stream()
        .filter(t -> t.getBook().equals(book))
        .forEach(t -> {
            String successMsg = locManager.getMessage("return.successful", book.getTitle());
            System.out.println(successMsg);
            TransactionLogger.logTransaction(successMsg + " by customer " + t.getCustomer().getName());
        });

        bookTransactions.stream()
        .filter(t -> t.book().equals(book))
        .forEach(t -> {
            String successMsg = locManager.getMessage("return.successful", book.getTitle());
            System.out.println(successMsg);
            TransactionLogger.logTransaction(successMsg + " by staff " + t.staff().getName());
        });
    }
    
    public void displayLoggedTransactions() {
        List<String> loggedTransactions = TransactionLogger.readTransactions();
        if (loggedTransactions.isEmpty()) {
            System.out.println("No transactions logged yet.");
        } else {
            System.out.println("\nLogged Transactions:");
            loggedTransactions.forEach(System.out::println);
        }
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
    	LocalizationManager locManager = new LocalizationManager(Locale.getDefault());
    	switch (book.getStatus()) {
        case AVAILABLE -> System.out.println(locManager.getMessage("book.available"));
        case CHECKED_OUT -> System.out.println(locManager.getMessage("book.checkedOut"));
        case OVERDUE -> System.out.println(locManager.getMessage("book.overdue"));
        case null -> System.out.println(locManager.getMessage("book.unknownStatus"));
        default -> throw new IllegalStateException(locManager.getMessage("error.unhandled", book.getStatus()));
    	}
    }

    public List<Book> getBooksSortedByTitle() {
        return bookSorter.sortBooksByTitle(bookMap.values().stream().toList());
    }

    public List<Book> getBooksSortedByAuthor() {
        return bookSorter.sortBooksByAuthor(bookMap.values().stream().toList());
    }

    public List<Book> getBooksSortedByCheckoutDate() {
        return bookSorter.sortBooksByCheckoutDate(bookMap.values().stream().toList());
    }

    public Customer findCustomerByName(String name, Function<String, String> nameTransformer) {
        String transformedName = nameTransformer.apply(name);
        return customers.stream()
                .filter(c -> c.getName().equalsIgnoreCase(transformedName))
                .findFirst()
                .orElse(null);
    }
    
    public Staff findStaffByName(String name) {
        return staffs.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Book findBookByTitle(String title) {
        return books.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }
    
    public Map<String, List<Book>> groupBooksByAuthor() {
        return books.stream()
                    .collect(Collectors.groupingBy(Book::getAuthor));
    }
    
   
	public Transaction getLatestTransaction() {
        if (transactions.isEmpty()) {
            return null;  
        }
        return transactions.get(transactions.size() - 1);
    }

	public Map<Boolean, List<Book>> partitionByAvailability() {
	    return books.stream()
	                .collect(Collectors.partitioningBy(book -> book.getStatus() == BookStatus.AVAILABLE));
	}
	
	public class InvalidCheckoutDateException extends Exception {   // checked exception (compile time exceptions)
        public InvalidCheckoutDateException(String message) {
            super(message);
        }
    }


}


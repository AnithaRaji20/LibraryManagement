package library;

public interface Loanable {
	
    default void checkout(Customer customer, Book book) {
    	System.out.println(customer.getName() + " has checked out the book: " + book.getTitle());
        printReceipt();
    }

    static boolean isAvailable(Book book) {
        return book.getStatus() == BookStatus.AVAILABLE;
    }

    private void printReceipt() {
        System.out.println("Receipt printed.");
    }
}

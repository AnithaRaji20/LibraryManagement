package library;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class LibraryApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize sample data
        List<Book> booksList = SampleBooks.createBooks();
        booksList.forEach(library::addBook);
        booksList.forEach(library::addBookByIsbn);
        List<Customer> customersList = SampleCustomers.createCustomers();
        customersList.forEach(library::addCustomer);
        List<Staff> staffList = SampleStaff.createStaff();
        staffList.forEach(library::addStaff);

        ConcurrentCheckoutService checkoutService = new ConcurrentCheckoutService();

        while (true) {
            System.out.println("\n=== Library System ===");
            System.out.println("1. Display all books");
            System.out.println("2. Search for books");
            System.out.println("3. Check out a book (Customer)");
            System.out.println("4. Check out a book (Staff)");
            System.out.println("5. Return a book");
            System.out.println("6. Display all transactions");
            System.out.println("7. Show overdue books");
            System.out.println("8. Check book status");
            System.out.println("9. Group books by author");
            System.out.println("10. Partition books by availability");
            System.out.println("9. Sorting Using Comparator");
            System.out.println("10. Concurrent checkout (Customer)");
            System.out.println("11. Display logged transactions");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                	System.out.println("\nAll Books in the Library:");
                    library.displayBooks();
                    System.out.println("\nGrouped by Author:");
                    library.groupBooksByAuthor().forEach((author, booksByAuthor) -> {
                        System.out.println("Author: " + author);
                        booksByAuthor.forEach(book -> System.out.println("  - " + book.getTitle()));
                    });
                    System.out.println("\nPartitioned by Availability:");
                    var partitionedBooks = library.partitionByAvailability();
                    System.out.println("Available Books:");
                    partitionedBooks.get(true).forEach(book -> System.out.println("  - " + book.getTitle()));
                    System.out.println("Unavailable Books:");
                    partitionedBooks.get(false).forEach(book -> System.out.println("  - " + book.getTitle()));
                    break;
                case 2:
                    System.out.print("Enter title or author to search: ");
                    String keyword = scanner.nextLine();
                    System.out.println("\nSearch Results for '" + keyword + "':");
                    library.searchBooks(keyword).forEach(System.out::println);
                    break;
                case 3:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter book title to check out: ");
                    String customerBookTitle = scanner.nextLine();

                    Customer customer = library.findCustomerByName(customerName, Function.identity());
                    if (customer != null) {
                        Book bookToCheckout = library.findBookByTitle(customerBookTitle);
                        if (bookToCheckout != null) {
                            System.out.print("Enter checkout date (YYYY-MM-DD) or press Enter for today: ");
                            String dateInput = scanner.nextLine();
                            LocalDate checkoutDate = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);

                            System.out.println("Choose checkout type:");
                            System.out.println("1. Checkout with payment validation");
                            System.out.println("2. Checkout with unpaid status");
                            System.out.println("3. Checkout without payment validation");
                            int checkoutChoice = scanner.nextInt();
                            scanner.nextLine();

                            PaymentStatus paymentStatus = null;
                            switch (checkoutChoice) {
                                case 1 -> paymentStatus = new Paid();
                                case 2 -> paymentStatus = new Unpaid();
                                case 3 -> paymentStatus = null;
                                default -> {
                                    System.out.println("Invalid choice!");
                                    return;
                                }
                            }

                            try {
                                library.checkoutBook(bookToCheckout, customer, checkoutDate, paymentStatus);
                            } catch (Exception e) {
                                System.out.println(locManager.getMessage("error.unhandled", e.getMessage()));
                            }
                        } else {
                            System.out.println("Book not found!");
                        }
                    } else {
                        System.out.println("Customer not found!");
                    }
                    break;
                case 4:
                    System.out.print("Enter staff name: ");
                    String staffName = scanner.nextLine();
                    System.out.print("Enter book title to check out: ");
                    String staffBookTitle = scanner.nextLine();

                    Staff staff = library.findStaffByName(staffName);
                    if (staff != null) {
                        Book bookToCheckoutStaff = library.findBookByTitle(staffBookTitle);
                        if (bookToCheckoutStaff != null) {
                            System.out.print("Enter checkout date (YYYY-MM-DD) or press Enter for today: ");
                            String dateInput = scanner.nextLine();
                            LocalDate checkoutDate = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);
                            try {
                                library.checkoutBook(bookToCheckoutStaff, staff, checkoutDate);
                            } catch (Exception e) {
                                System.out.println(locManager.getMessage("error.unhandled", e.getMessage()));
                            }
                        } else {
                            System.out.println("Book not found!");
                        }
                    } else {
                        System.out.println("Staff not found!");
                    }
                    break;
                case 5:
                    System.out.print("Enter book title to return: ");
                    String bookTitleToReturn = scanner.nextLine();
                    Book bookToReturn = library.findBookByTitle(bookTitleToReturn);
                    if (bookToReturn != null) {
                        library.returnBook(bookToReturn);
                    } else {
                        System.out.println("Book not found!");
                    }
                    break;
                case 6:
                    System.out.println("\nAll Transactions:");
                    library.displayTransactions(); 
                    break;
                case 7:
                    library.showOverdueBooks();
                    break;
                case 8:
                    System.out.print("Enter book title to check status: ");
                    String bookTitleForStatus = scanner.nextLine();
                    Book bookForStatus = library.findBookByTitle(bookTitleForStatus);
                    if (bookForStatus != null) {
                        library.printBookStatus(bookForStatus);
                    } else {
                        System.out.println("Book not found!");
                    }
                    break;
                case 9:
                    System.out.println("\nChoose sorting criterion:");
                    System.out.println("1. Sort by Title");
                    System.out.println("2. Sort by Author");
                    System.out.println("3. Sort by Checkout Date");
                    int sortingChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (sortingChoice) {
                        case 1 -> library.getBooksSortedByTitle().forEach(book -> System.out.println(book.getTitle()));
                        case 2 -> library.getBooksSortedByAuthor().forEach(book -> System.out.println(book.getAuthor()));
                        case 3 -> library.getBooksSortedByCheckoutDate().forEach(book -> System.out.println(book.getCheckoutDate()));
                        default-> System.out.println("Invalid choice!");
                    }
                    break;
                case 10:
                    System.out.println("Enter customer name for checkout:");
                    String name = scanner.nextLine();
                    Customer customerForCheckout = library.findCustomerByName(name, Function.identity());
                    if (customerForCheckout == null) {
                        System.out.println("Customer not found.");
                        break;
                    }
                    System.out.println("Enter the number of books to checkout:");
                    int numBooks = scanner.nextInt();
                    scanner.nextLine();
                    List<Book> booksToCheckout = new ArrayList<>();
                    for (int i = 0; i < numBooks; i++) {
                        System.out.println("Enter book title to checkout:");
                        String bookTitle = scanner.nextLine();
                        Book book = library.findBookByTitle(bookTitle);
                        if (book != null) booksToCheckout.add(book);
                        else System.out.println("Book not found: " + bookTitle);
                    }
                    checkoutService.processCheckouts(library, booksToCheckout, customerForCheckout);
                    break;
                case 11:
                    library.displayLoggedTransactions();
                    break;
                case 12:
                    System.out.println("Exiting the library system.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
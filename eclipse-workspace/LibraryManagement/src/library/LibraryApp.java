package library;

import java.time.LocalDate;
import java.util.*;

import java.util.Scanner;

import library.Library.InvalidCheckoutDateException;

public class LibraryApp {

    public static void main(String[] args) {
        
        Library library = new Library();
        
        List<Book> booksList = SampleBooks.createBooks();
        for (Book book : booksList) {
            library.addBook(book);
        }
        
        List<Customer> customersList = SampleCustomers.createCustomers();
        for (Customer customer : customersList) {
            library.addCustomer(customer);
        }
        
        List<Staff> staffList = SampleStaff.createStaff();
        for (Staff staff : staffList) {
            library.addStaff(staff);
        }
        
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Library System ===");
            System.out.println("1. Display all books");
            System.out.println("2. Search for books");
            System.out.println("3. Check out a book (Customer)");
            System.out.println("4. Check out a book (Staff)");
            System.out.println("5. Display all transactions");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    // Display all books
                    System.out.println("\nAll Books in the Library:");
                    library.displayBooks();
                    break;
                case 2:
                    // Search for books 
                    System.out.print("Enter title or author to search: ");
                    String keyword = scanner.nextLine();
                    System.out.println("\nSearch Results for '" + keyword + "':");
                    library.searchBooks(keyword).forEach(System.out::println);
                    break;
                case 3:
                    // Check out a book by Customer
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter book title to check out: ");
                    String customerBookTitle = scanner.nextLine();

                    Customer customer = library.findCustomerByName(customerName);
                   
                    if (customer != null) {
                        Book bookToCheckout = library.findBookByTitle(customerBookTitle);
                        if (bookToCheckout != null) {
                            System.out.print("Enter checkout date (YYYY-MM-DD) or press Enter to use today's date: ");
                            String dateInput = scanner.nextLine();
                            LocalDate checkoutDate;
                            if (dateInput.isEmpty()) {
                                checkoutDate = LocalDate.now();  // Default to today's date if no input
                            } else {
                                try {
                                    checkoutDate = LocalDate.parse(dateInput);  
                                } catch (Exception e) {
                                    System.out.println("Invalid date format. Using today's date.");
                                    checkoutDate = LocalDate.now();  // Fall back to today's date on error
                                }
                            }

                            // Ask the user if they want to checkout with payment validation
                            System.out.println("Choose checkout type:");
                            System.out.println("1. Checkout with payment validation (Payment must be completed)");
                            System.out.println("2. Checkout with unpaid status (Payment not completed)");
                            System.out.println("3. Checkout without payment validation (No payment check)");

                            String checkoutChoiceStr = scanner.nextLine();
                            int checkoutChoice;

                            try {
                                checkoutChoice = Integer.parseInt(checkoutChoiceStr);  
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input! Please enter a valid number (1, 2, or 3).");
                                return;  
                            }

                            // Determine the payment status based on the user's choice
                            PaymentStatus paymentStatus = null;
                            if (checkoutChoice == 1) {
                                paymentStatus = new Paid();  // Checkout with payment status set to Paid
                            } else if (checkoutChoice == 2) {
                                paymentStatus = new Unpaid();  // Checkout with payment status set to Unpaid
                            } else if (checkoutChoice == 3) {
                                paymentStatus = null;  // No payment validation, pass null
                            } else {
                                System.out.println("Invalid choice! Returning to main menu.");
                                return;  // Exit if invalid choice
                            }

                            // Proceed to checkout with the selected payment status
                            try {
                                library.checkoutBook(bookToCheckout, customer, checkoutDate, paymentStatus);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error during checkout: " + e.getMessage());
                            } catch (InvalidCheckoutDateException e) {
                                System.out.println("Error during checkout: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Book not found!");
                        }
                    } else {
                        System.out.println("Customer not found!");
                    }
                    break;  

                case 4:
                    // Check out a book by Staff (just to show the difference between immutable class and record)
                    System.out.print("Enter staff name: ");
                    String staffName = scanner.nextLine();
                    System.out.print("Enter book title to check out: ");
                    String staffBookTitle = scanner.nextLine();

                    Staff staff = library.findStaffByName(staffName);
                    if (staff != null) {
                        Book bookToCheckoutStaff = library.findBookByTitle(staffBookTitle);
                        if (bookToCheckoutStaff != null) {
                        	System.out.print("Enter checkout date (YYYY-MM-DD) or press Enter to use today's date: ");
                            String dateInput = scanner.nextLine();
                            LocalDate checkoutDate;
                            if (dateInput.isEmpty()) {
                                checkoutDate = LocalDate.now();
                            } else {
                                try {
                                    checkoutDate = LocalDate.parse(dateInput);
                                } catch (Exception e) {
                                    System.out.println("Invalid date format. Using today's date.");
                                    checkoutDate = LocalDate.now();
                                }
                            }
                            try {
                            library.checkoutBook(bookToCheckoutStaff, staff, checkoutDate); 
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error during checkout: " + e.getMessage());
                            } catch (InvalidCheckoutDateException e) {
                                System.out.println("Error during checkout: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Book not found!");
                        }
                    } else {
                        System.out.println("Staff not found!");
                    }
                    break;
                case 5:
                    // Display all transactions
                    System.out.println("\nAll Transactions:");
                    library.displayTransactions();
                    break;
                case 6:
                    // Exit the program
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

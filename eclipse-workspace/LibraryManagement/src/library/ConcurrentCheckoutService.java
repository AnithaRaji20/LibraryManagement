package library;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class ConcurrentCheckoutService {
    private final ExecutorService executor = Executors.newFixedThreadPool(5); // Pool with 5 threads

    public void processCheckouts(Library library, List<Book> booksToCheckout, Customer customer) {
        List<Callable<String>> tasks = new ArrayList<>();
        LocalDate today = LocalDate.now();
        PaymentStatus status = new Paid();  // Create instance of Paid class
        boolean allBooksAvailable = true;  // Flag to check if all books are available

        // Create a task for each book
        for (Book book : booksToCheckout) {
            tasks.add(new CheckoutTask(library, book, customer, today, status));  // Pass instance of Paid as status
        }

        try {
            List<Future<String>> results = executor.invokeAll(tasks); // Execute all tasks

            // Process and print the results
            for (Future<String> result : results) {
                try {
                    String checkoutResult = result.get();
                    if (checkoutResult.startsWith("Book not available")) {
                        allBooksAvailable = false;  // If any book is unavailable, flag it
                    }
                    System.out.println(checkoutResult);  // Print result from each task
                } catch (ExecutionException _) {
                    System.err.println("Error during task execution: ");
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                    System.err.println("Task interrupted: ");
                }
            }

        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            System.err.println("Execution interrupted: ");
        } finally {
            executor.shutdown(); // Shutdown the executor service
        }
    }
}

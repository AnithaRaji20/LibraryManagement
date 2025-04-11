package library;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;

public class TransactionLogger {
    private static final Path logPath = Paths.get("library_transactions.log");

    // Ensure the log file exists or create it if it doesn't
    static {
        try {
            if (Files.notExists(logPath)) {
                Files.createFile(logPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create log file: " + e.getMessage());
        }
    }

    public static void logTransaction(String logEntry) {
        try {
            String timestampedEntry = String.format("[%s] %s", 
                LocalDateTime.now().toString(), logEntry);
            Files.writeString(logPath, timestampedEntry + System.lineSeparator(), 
                StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    // Method to read all transactions from the log file
    public static List<String> readTransactions() {
        try {
            return Files.readAllLines(logPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read log file: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }
}

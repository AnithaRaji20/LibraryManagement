package library;

import java.time.LocalDate;

public record BookTransaction(Book book, Staff staff, LocalDate checkoutDate, LocalDate dueDate) {
    // This is a record class, it automatically generates the constructor, toString(), hashCode(), and equals() methods.
	
	@Override
    public String toString() {
        return String.format("Book='%s', Staff='%s', Checkout Date='%s', Due Date='%s'",
                             book.getTitle(), staff.getName(), checkoutDate, dueDate);
    }
	
}

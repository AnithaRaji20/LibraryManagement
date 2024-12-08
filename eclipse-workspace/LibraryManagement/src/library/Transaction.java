package library;

import java.time.LocalDate;

//immutable class
public final class Transaction {
	private final Book book;
	private final Customer customer;
	private final LocalDate checkoutDate;
	private final LocalDate dueDate;

	public Transaction(Book book, Customer customer, LocalDate checkoutDate, LocalDate dueDate) {
		this.book = book;
		this.customer = customer;
		this.checkoutDate = checkoutDate;
		this.dueDate = dueDate;
	}

	public Book getBook() {
		return book;
	}

	public Customer getCustomer() {
		return customer;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}
	
	@Override
	public String toString() {
		return String.format("Book='%s', Customer='%s', Checkout Date='%s', Due Date='%s'",
				book.getTitle(), customer.getName(), checkoutDate, dueDate);
	}
}

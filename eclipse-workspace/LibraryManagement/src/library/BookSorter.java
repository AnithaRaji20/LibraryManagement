package library;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookSorter {
    public BookSorter() {
    }

    public List<Book> sortBooksByTitle(List<Book> books) {
        return (List)books.stream().sorted(Comparator.comparing(Book::getTitle)).collect(Collectors.toList());
    }

    public List<Book> sortBooksByAuthor(List<Book> books) {
        return (List)books.stream().sorted(Comparator.comparing(Book::getAuthor)).collect(Collectors.toList());
    }

    public List<Book> sortBooksByCheckoutDate(List<Book> books) {
        return (List)books.stream().sorted(Comparator.comparing(Book::getCheckoutDate, Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
    }
}

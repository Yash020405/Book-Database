import java.util.*;

public class BookRepository {
    private final Map<String, List<Book>> booksByAuthor = new HashMap<>();
    private final Map<Double, List<Book>> booksByRating = new HashMap<>();

    public BookRepository(List<Book> books) {
        for (Book book : books) {
            booksByAuthor.computeIfAbsent(book.getAuthor(), k -> new ArrayList<>()).add(book);
            booksByRating.computeIfAbsent(book.getUserRating(), k -> new ArrayList<>()).add(book);
        }
    }

    public int countBooksByAuthor(String author) {
        return findBooksByAuthor(author).size();
    }

    public Set<String> listAllAuthors() {
        return booksByAuthor.keySet();
    }

    public List<Book> findBooksByAuthor(String author) {
        for (Map.Entry<String, List<Book>> entry : booksByAuthor.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(author)) {
                return entry.getValue();
            }
        }
        return Collections.emptyList();
    }

    public List<Book> findBooksByRating(double rating) {
        return booksByRating.getOrDefault(rating, Collections.emptyList());
    }

    public Map<String, Double> findBooksAndPricesByAuthor(String author) {
        List<Book> books = findBooksByAuthor(author);
        Map<String, Double> result = new HashMap<>();
        for (Book book : books) {
            result.put(book.getTitle(), book.getPrice());
        }
        return result;
    }
}
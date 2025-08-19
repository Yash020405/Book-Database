import java.util.*;

public class BookRepository {
    private final Map<String, List<Book>> booksByAuthor = new HashMap<>();
    private final Map<Double, List<Book>> booksByRating = new HashMap<>();

    public BookRepository(List<Book> books) {
        if (books == null) {
            throw new IllegalArgumentException("Books list cannot be null");
        }
        
        for (Book book : books) {
            if (book == null) {
                System.err.println("Warning: Skipping null book");
                continue;
            }
            booksByAuthor.computeIfAbsent(book.getAuthor().toLowerCase(), k -> new ArrayList<>()).add(book);
            booksByRating.computeIfAbsent(book.getUserRating(), k -> new ArrayList<>()).add(book);
        }
    }

    public int countBooksByAuthor(String author) {
        if (author == null) {
            return 0;
        }
        return booksByAuthor.getOrDefault(author.toLowerCase(), Collections.emptyList()).size();
    }

    public Set<String> listAllAuthors() {
        Set<String> authors = new HashSet<>();
        for (List<Book> bookList : booksByAuthor.values()) {
            if (!bookList.isEmpty()) {
                authors.add(bookList.get(0).getAuthor());
            }
        }
        return authors;
    }

    public List<Book> findBooksByAuthor(String author) {
        if (author == null) {
            return Collections.emptyList();
        }
        return booksByAuthor.getOrDefault(author.toLowerCase(), Collections.emptyList());
    }

    public List<Book> findBooksByRating(double rating) {
        return booksByRating.getOrDefault(rating, Collections.emptyList());
    }

    public Map<String, Double> findBooksAndPricesByAuthor(String author) {
        if (author == null) {
            return Collections.emptyMap();
        }
        List<Book> books = booksByAuthor.getOrDefault(author.toLowerCase(), Collections.emptyList());
        Map<String, Double> result = new HashMap<>();
        for (Book book : books) {
            result.put(book.getTitle(), book.getPrice());
        }
        return result;
    }
}
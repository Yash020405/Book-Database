import java.util.List;
import java.util.Map;
import java.util.Set;

public class Driver {
    public static void main(String[] args) {
        DataReader dataReader = new DatasetReader();
        
        String filePath = "data.csv";
        List<Book> books = dataReader.readBooks(filePath);
        
        BookRepository repository = new BookRepository(books);

        System.out.println("Task 1 - Total books by George Orwell: " + repository.countBooksByAuthor("George Orwell"));

        System.out.println("\nTask 2 - All authors:");
        Set<String> authors = repository.listAllAuthors();
        authors.stream().limit(5).forEach(System.out::println);

        System.out.println("\nTask 3 - Books by George Orwell:");
        List<Book> booksByOrwell = repository.findBooksByAuthor("George Orwell");
        booksByOrwell.forEach(book -> System.out.println(book.getTitle()));

        System.out.println("\nTask 4 - Books with rating 4.7:");
        List<Book> highRatedBooks = repository.findBooksByRating(4.7);
        highRatedBooks.stream().limit(3).forEach(book -> 
            System.out.println(book.getTitle() + " by " + book.getAuthor()));

        System.out.println("\nTask 5 - Prices of books by George Orwell:");
        Map<String, Double> booksAndPrices = repository.findBooksAndPricesByAuthor("George Orwell");
        booksAndPrices.forEach((title, price) -> 
            System.out.println(title + ": $" + price));
    }
}
import java.io.*;
import java.util.*;

public class DatasetReader implements DataReader {
    
    private Map<String, Integer> columnMap = new HashMap<>();
    
    @Override
    public List<Book> readBooks(String filePath) {
        List<Book> books = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                parseHeader(headerLine);
            }
            
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Book book = parseBookFromLine(line);
                    if (book != null) {
                        books.add(book);
                    }
                } catch (Exception e) {
                    System.err.println("Warning: Skipping invalid line");
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        
        return books;
    }
    
    private void parseHeader(String headerLine) {
        String[] headers = splitCsvLine(headerLine);
        columnMap.clear();
        
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim().toLowerCase();
            columnMap.put(header, i);
        }
    }
    
    private Book parseBookFromLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        
        String[] data = splitCsvLine(line);
        
        String title = getColumnValue(data, "name");
        String author = getColumnValue(data, "author");
        double userRating = Double.parseDouble(getColumnValue(data, "user rating"));
        int reviews = Integer.parseInt(getColumnValue(data, "reviews"));
        double price = Double.parseDouble(getColumnValue(data, "price"));
        int year = Integer.parseInt(getColumnValue(data, "year"));
        String genre = getColumnValue(data, "genre");
        
        return new Book(title, author, userRating, reviews, price, year, genre);
    }
    
    private String getColumnValue(String[] data, String columnName) {
        Integer columnIndex = columnMap.get(columnName.toLowerCase());
        return data[columnIndex].trim();
    }
    
    private String[] splitCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }
}
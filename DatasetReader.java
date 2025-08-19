import java.io.*;
import java.util.*;

public class DatasetReader implements DataReader {
    
    private Map<String, Integer> columnMap = new HashMap<>();
    
    @Override
    public List<Book> readBooks(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        List<Book> books = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                parseHeader(headerLine);
            }
            
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                try {
                    Book book = parseBookFromLine(line);
                    if (book != null) {
                        books.add(book);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Line " + lineNum + ": Invalid number - " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Line " + lineNum + ": Missing columns");
                } catch (Exception e) {
                    System.err.println("Line " + lineNum + ": Error - " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        
        return books;
    }
    
    private void parseHeader(String headerLine) {
        String[] headers = splitLine(headerLine);
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
        
        String[] data = splitLine(line);
        
        String title = getValue(data, "name");
        String author = getValue(data, "author");
        double rating = Double.parseDouble(getValue(data, "user rating"));
        int reviews = Integer.parseInt(getValue(data, "reviews"));
        double price = Double.parseDouble(getValue(data, "price"));
        int year = Integer.parseInt(getValue(data, "year"));
        String genre = getValue(data, "genre");
        
        return new Book(title, author, rating, reviews, price, year, genre);
    }
    
    private String getValue(String[] data, String columnName) {
        Integer index = columnMap.get(columnName.toLowerCase());
        if (index == null) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        if (index >= data.length) {
            throw new ArrayIndexOutOfBoundsException("Missing data for column: " + columnName);
        }
        return data[index].trim();
    }
    
    private String[] splitLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }
}
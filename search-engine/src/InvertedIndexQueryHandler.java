import exception.ExceptionHandler;
import model.SearchResult;
import util.StringCleaner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class InvertedIndexQueryHandler implements ExceptionHandler {
    private final StringCleaner stringCleaner;
    private final Map<String, List<SearchResult>> queryMap;
    private final InvertedIndex invertedIndex = InvertedIndex.getInstance();

    public InvertedIndexQueryHandler() {
        this.stringCleaner = new StringCleaner();
        queryMap = new LinkedHashMap<>();
    }

    public void handleQuery(String filePath, String writeOutfile) {
        try {
            Files.lines(Paths.get(filePath)).forEachOrdered(this::updateQueryBasedResultsFromIndex);
            print(writeOutfile);
        } catch (IOException e) {
            displayException("The file: " + filePath + " could not be read.");
        }
    }

    private void updateQueryBasedResultsFromIndex(String queryLine) {
        String normalizeQuery = stringCleaner.sanitizeQuery(queryLine);
        String[] multiWordQuery = normalizeQuery.split("\\s");
        List<SearchResult> searchResults = invertedIndex.search(Arrays.asList(multiWordQuery));
        queryMap.put(normalizeQuery, searchResults);
    }

    public void print(String writeOutFile) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(writeOutFile), StandardCharsets.UTF_8)) {
            for (String query : queryMap.keySet()) {
                writer.write(query);
                writer.newLine();
                writer.write(queryMap.get(query).toString());
                writer.newLine();
            }
        } catch (IOException e) {
            displayException("Could not write search results to file");
        }
    }

    @Override
    public void displayException(String exception) {
        System.out.println(exception);
    }
}

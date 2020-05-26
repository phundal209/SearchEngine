import model.Index;
import model.SearchResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class InvertedIndex {
    private final TreeMap<String, Map<String, List<Integer>>> invertedIndex;
    private static InvertedIndex instance = null;

    private InvertedIndex() {
        this.invertedIndex = new TreeMap<>();
    }

    public synchronized static InvertedIndex getInstance() {
        if (instance == null) {
            instance = new InvertedIndex();
        }
        return instance;
    }

    public void addAll(List<Index> indices) {
        for (Index index : indices) {
            add(index);
        }
    }

    public void add(Index index) {
        insertIfWordHasNotBeenAdded(index);
        updateIfWordExistsButNewFile(index);
        updateIfWordsExistsAndFileExitsButNewPosition(index);
    }

    private void insertIfWordHasNotBeenAdded(Index index) {
        if (!invertedIndex.containsKey(index.getWord())) {
            Map<String, List<Integer>> fileAndPositionMap = new TreeMap<>();
            fileAndPositionMap.put(index.getFile(), new ArrayList<>(index.getPosition()));
            invertedIndex.put(index.getWord(), fileAndPositionMap);
        }
    }

    private void updateIfWordExistsButNewFile(Index index) {
        Map<String, List<Integer>> fileAndPositionMap = invertedIndex.get(index.getWord());
        if (!fileAndPositionMap.containsKey(index.getFile())) {
            fileAndPositionMap.put(index.getFile(), new ArrayList<>(index.getPosition()));
            invertedIndex.put(index.getWord(), fileAndPositionMap);
        }
    }

    private void updateIfWordsExistsAndFileExitsButNewPosition(Index index) {
        Map<String, List<Integer>> fileAndPositionMap = invertedIndex.get(index.getWord());
        if (fileAndPositionMap.containsKey(index.getFile())) {
            List<Integer> positions = fileAndPositionMap.get(index.getFile());
            positions.add(index.getPosition());
            fileAndPositionMap.put(index.getFile(), positions);
            invertedIndex.put(index.getWord(), fileAndPositionMap);
        }
    }

    public List<SearchResult> search(List<String> queries) {
        Map<String, SearchResult> mapOfResultPerFile = new HashMap<>();
        for (String query : queries) {
            searchIndexForQuery(mapOfResultPerFile, query);
        }
        List<SearchResult> results = new ArrayList<>(mapOfResultPerFile.values());
        Collections.sort(results);
        return results;
    }

    private void searchIndexForQuery(Map<String, SearchResult> mapOfResultPerFile, String query) {
        for (String searchWord : invertedIndex.tailMap(query).keySet()) {
            if (searchWord.startsWith(query)) {
                Map<String, List<Integer>> filesAndPositionsOfSearchWord = invertedIndex.get(searchWord);
                getResultsPerFile(mapOfResultPerFile, filesAndPositionsOfSearchWord);
            }
        }
    }

    private void getResultsPerFile(Map<String, SearchResult> mapOfResultPerFile,
                                   Map<String, List<Integer>> filesAndPositionsOfSearchWord) {
        for (String filePath : filesAndPositionsOfSearchWord.keySet()) {
            int frequency = filesAndPositionsOfSearchWord.get(filePath).size();
            int position = filesAndPositionsOfSearchWord.get(filePath).get(0);
            if (!mapOfResultPerFile.containsKey(filePath)) {
                SearchResult searchResult = createSearchResult(filePath, frequency, position);
                mapOfResultPerFile.put(filePath, searchResult);
            } else {
                SearchResult result = mapOfResultPerFile.get(filePath);
                result.updatePosition(position);
                result.updateFrequency(frequency);
                mapOfResultPerFile.put(filePath, result);
            }
        }
    }

    private SearchResult createSearchResult(String filePath, int frequency, int position) {
        return new SearchResult(
                filePath,
                frequency,
                position);
    }

    public void write(String fileName) {
        Path path = Paths.get(fileName);
        writeIndex(fileName, path);
    }

    private void writeIndex(String fileName, Path path) {
        try (BufferedWriter outputMap = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (String word : invertedIndex.keySet()) {
                outputMap.write(word);
                outputMap.newLine();
                writeFilesAndPositions(outputMap, word);
                outputMap.newLine();
            }
            outputMap.newLine();

        } catch (IOException e) {
            System.out.println("Your text file " + fileName + " cannot be accessed.");
        }
    }

    private void writeFilesAndPositions(BufferedWriter outputMap, String word) throws IOException {
        for (String files : invertedIndex.get(word).keySet()) {
            outputMap.write("\"" + files + "\"");
            writePositions(outputMap, word, files);
            outputMap.newLine();
        }
    }

    private void writePositions(BufferedWriter outputMap, String word, String files) throws IOException {
        int pos = 1;
        for (Integer position : invertedIndex.get(word).get(files)) {
            outputMap.write(position.toString());
            if (pos != invertedIndex.get(word).get(files).size()) {
                outputMap.write(", ");
                pos++;
            }
        }
    }
}

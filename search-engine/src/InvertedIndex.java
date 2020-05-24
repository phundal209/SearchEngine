import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InvertedIndex {
    private Map<String, Map<String, List<Integer>>> invertedIndex = new TreeMap<>();
    private static InvertedIndex instance = null;

    private InvertedIndex() {}

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

    public void write(String fileName) {
        Path path = Paths.get(fileName);
        writeIndex(fileName, path);
    }

    private void writeIndex(String fileName, Path path) {
        try (BufferedWriter outputMap = Files.newBufferedWriter(path, StandardCharsets.UTF_8);){
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
        for (Integer position : invertedIndex.get(word).get(files)) {
            outputMap.write(", ");
            outputMap.write(position.toString());
        }
    }
}

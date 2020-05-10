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

    public void print() {
        for (String word : invertedIndex.keySet()) {
            System.out.println("word: " + word);
            Map<String, List<Integer>> fileAndPositionMap = invertedIndex.get(word);
            printFileAndPositions(fileAndPositionMap);
            System.out.println();
        }
    }

    private void printFileAndPositions(Map<String, List<Integer>> fileAndPositions) {
        for (String file : fileAndPositions.keySet()) {
            System.out.print("{ ");
            System.out.print(file + ", ");
            System.out.print("[");
            List<Integer> positions = fileAndPositions.get(file);
            printPositions(positions);
            System.out.print("]");
            System.out.print(" },");
            System.out.println();
        }
    }

    private void printPositions(List<Integer> positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.print(positions.get(i));
            if (i + 1 != positions.size()) {
                System.out.print(",");
            }
        }
    }
}

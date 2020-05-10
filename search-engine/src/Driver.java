import java.io.File;
import java.util.List;

public class Driver {
    private static final String PATH = "/Users/phundal/SearchEngine/extended/pg11.txt";
    private static InvertedIndex invertedIndex = InvertedIndex.getInstance();
    private static DirectoryTraverser directoryTraverser = new DirectoryTraverser();
    private static InvertedIndexFileParser invertedIndexFileParser = new InvertedIndexFileParser();

    public static void main(String[] args) {
        System.out.println("Loading inverted index. This may take some time.....");
        List<File> files = directoryTraverser.recursivelyTraverseAndFlattenFiles
                (new File(PATH));
        for (File file : files) {
            invertedIndexFileParser.readFile(file, invertedIndex::addAll);
        }
        System.out.println("Inverted Index output = \n");
        invertedIndex.print();
    }
}

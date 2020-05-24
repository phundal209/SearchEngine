import java.io.File;
import java.util.List;
import java.util.Map;

public class Driver {
    private static final InvertedIndex invertedIndex = InvertedIndex.getInstance();
    private static final DirectoryTraverser directoryTraverser = new DirectoryTraverser();
    private static final InvertedIndexFileParser invertedIndexFileParser = new InvertedIndexFileParser();
    private static final ArgumentParser argumentParser = new ArgumentParser();

    public static void main(String[] args) {
        Map<Argument, String> arguments = argumentParser.parse(args);
        String directoryPath = arguments.get(Argument.Directory);
        System.out.println("Loading inverted index. This may take some time.....");
        List<File> files = directoryTraverser.recursivelyTraverseAndFlattenFiles
                (new File(directoryPath));
        for (File file : files) {
            invertedIndexFileParser.readFile(file, invertedIndex::addAll);
        }
        System.out.println("Outputting results results to results.txt.....\n");
        invertedIndex.write("/Users/phundal/SearchEngine/results.txt");
        System.out.println("Complete!");
    }
}

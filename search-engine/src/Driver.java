import io.Argument;
import io.ArgumentParser;
import util.file.DirectoryTraverser;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Driver {
    private static final InvertedIndex invertedIndex = InvertedIndex.getInstance();
    private static final DirectoryTraverser directoryTraverser = new DirectoryTraverser();
    private static final InvertedIndexBuilder invertedIndexFileParser = new InvertedIndexBuilder();
    private static final InvertedIndexQueryHandler queryParser = new InvertedIndexQueryHandler();
    private static final ArgumentParser argumentParser = new ArgumentParser();

    public static void main(String[] args) {
        Map<Argument, String> arguments = argumentParser.parse(args);
        if (!arguments.containsKey(Argument.Directory)) {
            System.out.println("Need a valid directory to build index.");
            return;
        }
        if (arguments.containsKey(Argument.Directory) &&
                arguments.containsKey(Argument.IndexOutputFile)) {
            String indexOutputFilePath = arguments.get(Argument.IndexOutputFile);
            String directoryPath = arguments.get(Argument.Directory);
            buildInvertedIndex(directoryPath, indexOutputFilePath);
        }

        if (arguments.containsKey(Argument.Query) && arguments.containsKey(Argument.ResultsFile)) {
            String queryFileInput = arguments.get(Argument.Query);
            String queryFileOutput = arguments.get(Argument.ResultsFile);
            getSearchResults(queryFileInput, queryFileOutput);
        }
    }

    private static void buildInvertedIndex(String directoryPath, String indexOutputFilePath) {
        System.out.println("Loading inverted index. This may take some time.....");
        List<File> files = directoryTraverser.recursivelyTraverseAndFlattenFiles
                (new File(directoryPath));
        for (File file : files) {
            invertedIndexFileParser.readAndUpdateIndex(file);
        }
        System.out.println("Outputting results results to results.txt.....\n");
        invertedIndex.write(indexOutputFilePath);
        System.out.println("Complete!");
    }

    private static void getSearchResults(String queryFileInput, String queryFileOutput) {
        System.out.println("Gathering search results from input: " + queryFileInput);
        queryParser.handleQuery(queryFileInput, queryFileOutput);
    }
}

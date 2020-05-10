import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class InvertedIndexFileParser implements ExceptionHandler {
    private int positionInFile = 1;
    private String currentFile;
    private WordCleaner wordCleaner = new WordCleaner();

    public void readFile(File file, InvertedIndexReader invertedIndexReader) {
        try {
            setCurrentFileName(file.getName());
            Files.lines(Paths.get(file.getAbsolutePath())).forEachOrdered(line -> getIndicesFromLineRead(line, invertedIndexReader));
        } catch (IOException e) {
            displayException("The file " + file.getName() + " was not able to be read." +
                    "Please verify this file exists in this location");
        }
    }

    private void setCurrentFileName(String fileName) {
        this.currentFile = fileName;
    }

    private void getIndicesFromLineRead(String line, InvertedIndexReader invertedIndexReader) {
        List<Index> indices = new LinkedList<>();
        String[] wordsInLine = getEachWord(line);
        for (String word : wordsInLine) {
            String cleanWord = wordCleaner.sanitize(word);
            insertAndUpdate(cleanWord, indices);
        }
        invertedIndexReader.onLineRead(indices);
    }

    private String[] getEachWord(String line) {
        String regexForSplittingWord = "\s";
        return line.trim().split(regexForSplittingWord);
    }

    private void insertAndUpdate(String cleanWord, List<Index> indices) {
        if (cleanWord != null) {
            indices.add(new Index(cleanWord, currentFile, positionInFile));
            positionInFile++;
        }
    }

    @Override
    public void displayException(String exception) {
        System.out.println(exception);
    }
}

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DirectoryTraverser implements ExceptionHandler {
    private List<File> filesInDirectory = new LinkedList<>();

    public List<File> recursivelyTraverseAndFlattenFiles(File input) {
        if (input.isFile()) {
            filesInDirectory.add(input);
            return filesInDirectory;
        }
        File[] inputFiles = input.listFiles();
        return recursiveTraversalHelper(inputFiles);
    }

    private List<File> recursiveTraversalHelper(File[] inputFiles) {
        try {
            return flattenedFiles(inputFiles);
        } catch (NullPointerException e) {
            displayException("There was an error parsing your files. Please refer to the following" +
                    "exception: + \n" + e.getMessage());
            return null;
        }
    }

    private List<File> flattenedFiles(File[] inputFiles) {
        for (File file : inputFiles) {
            if (file.isDirectory()) {
                filesInDirectory = recursivelyTraverseAndFlattenFiles(file);
            } else if (file.isFile()) {
                addIfMeetsTxtExtension(file, filesInDirectory);
            }
        }
        return filesInDirectory;
    }

    private void addIfMeetsTxtExtension(File file, List<File> filesInDirectory) {
        if (hasValidFileExtension(file)) {
            filesInDirectory.add(file);
        }
    }

    private boolean hasValidFileExtension(File file) {
        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return fileExtension.equals("txt") && !moreThanOneFileExtension(file);
    }

    private boolean moreThanOneFileExtension(File file) {
        int firstFileExtensionPosition = file.getName().indexOf(".");
        int lastFileExtensionPosition = file.getName().lastIndexOf(".");
        return firstFileExtensionPosition != lastFileExtensionPosition;
    }

    @Override
    public void displayException(String exception) {
        System.out.println(exception);
    }

    public void printDirectories(List<File> files) {
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}

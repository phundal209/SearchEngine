package io;

import exception.ExceptionHandler;

import java.util.*;

public class ArgumentParser implements ExceptionHandler {
    private final Map<Argument, String> argMap;

    public ArgumentParser() {
        this.argMap = new HashMap<>();
    }

    public Map<Argument, String> parse(String[] args) {
        checkForValidArgs(args);
        parseArgs(Arrays.asList(args));
        return argMap;
    }

    private void checkForValidArgs(String[] args) {
        if (args == null || args.length == 0) {
            displayException("Please enter arguments for the engine to run.");
        }
    }

    private void parseArgs(List<String> args) {
        if (args.contains(Argument.Directory.getFlag())) {
            parseDirectoryArgs(args);
        }
        if (args.contains(Argument.Query.getFlag())) {
            parseQueryArgs(args);
        }
        if (args.contains(Argument.ResultsFile.getFlag())) {
            parseResultsOutputArgs(args);
        }
        if (args.contains(Argument.IndexOutputFile.getFlag())) {
            parseIndexOutputFileArgs(args);
        }
    }

    private void parseDirectoryArgs(List<String> args) {
        int directoryValueIndex = args.indexOf(Argument.Directory.getFlag()) + 1;
        if (directoryValueIndex > args.size()) {
            displayException("No value provided for directory. Please enter after -d flag.");
        } else {
            argMap.put(Argument.Directory, args.get(directoryValueIndex));
        }
    }

    private void parseQueryArgs(List<String> args) {
        int queryValueIndex = args.indexOf(Argument.Query.getFlag()) + 1;
        if (queryValueIndex > args.size()) {
            displayException("No value provided for query file. Please enter a query file after -q flag.");
        } else {
            argMap.put(Argument.Query, args.get(queryValueIndex));
        }
    }

    private void parseResultsOutputArgs(List<String> args) {
        int searchResultsOutputFileIndex = args.indexOf(Argument.ResultsFile.getFlag()) + 1;
        if (searchResultsOutputFileIndex > args.size()) {
            displayException("No value provided for output file. Please know you will not receive any results output");
        } else {
            argMap.put(Argument.ResultsFile, args.get(searchResultsOutputFileIndex));
        }
    }

    private void parseIndexOutputFileArgs(List<String> args) {
        int indexOutputFileIndex = args.indexOf(Argument.IndexOutputFile.getFlag()) + 1;
        if (indexOutputFileIndex > args.size()) {
            displayException("No value provided for where to write out inverted index. Please know you will not receive" +
                    "any results of your index");
        } else {
            argMap.put(Argument.IndexOutputFile, args.get(indexOutputFileIndex));
        }
    }

    @Override
    public void displayException(String exception) {
        System.out.println(exception);
    }
}

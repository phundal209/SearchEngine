import java.util.*;

public class ArgumentParser implements ExceptionHandler {
    private Map<Argument, String> argMap;

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
            int directoryValueIndex = args.indexOf(Argument.Directory.getFlag()) + 1;
            if (directoryValueIndex > args.size()) {
                displayException("No value provided for directory. Please enter after -d");
            } else {
                argMap.put(Argument.Directory, args.get(directoryValueIndex));
            }
        }
    }


    @Override
    public void displayException(String exception) {
        System.out.println(exception);
    }
}

package util;

public class StringCleaner {

    public String sanitizeQuery(String query) {
        if (isValidWord(query) && hasOnlyCharactersDigitsOrBlank(query)) {
            String removeDash = query.replace("-", "");
            return makeCaseInsensitive(removeDash);
        }
        return "";
    }

    public String sanitize(String word) {
        if (isValidWord(word) && hasOnlyCharactersAndDigits(word)) {
            return makeCaseInsensitive(word).trim();
        }
        return "";
    }

    private boolean isValidWord(String input) {
        return (input != null || !input.isBlank());
    }

    private boolean hasOnlyCharactersAndDigits(String word) {
        for (Character character : word.toCharArray()) {
            if (!Character.isLetterOrDigit(character)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasOnlyCharactersDigitsOrBlank(String word) {
        for (Character character : word.toCharArray()) {
            if (!Character.isLetterOrDigit(character) && !Character.isSpaceChar(character)) {
                return false;
            }
        }
        return true;
    }

    private String makeCaseInsensitive(String word) {
        return word.toLowerCase();
    }
}

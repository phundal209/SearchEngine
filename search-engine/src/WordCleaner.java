public class WordCleaner {

    public String sanitize(String word) {
        if (hasOnlyCharactersAndDigits(word) && !word.isBlank()) {
            return makeCaseInsensitive(word).trim();
        }
        return null;
    }

    private boolean hasOnlyCharactersAndDigits(String word) {
        for (Character character : word.toCharArray()) {
            if (!Character.isLetterOrDigit(character)) {
                return false;
            }
        }
        return true;
    }

    private String makeCaseInsensitive(String word) {
        return word.toLowerCase();
    }
}

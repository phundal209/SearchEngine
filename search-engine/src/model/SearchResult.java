package model;

public class SearchResult implements Comparable<SearchResult> {
    private final String filePath;
    private int frequency;
    private int position;

    public SearchResult(String filePath, int frequency, int position) {
        this.filePath = filePath;
        this.frequency = frequency;
        this.position = position;
    }

    public void updateFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void updatePosition(int position) {
        if (this.position < position) {
            this.position = position;
        }
    }

    @Override
    public int compareTo(SearchResult other) {
        if (this.frequency != other.frequency) {
            return Integer.compare(other.frequency, this.frequency);
        }

        if (this.position != other.position) {
            return Integer.compare(this.position, other.position);
        }

        return String.CASE_INSENSITIVE_ORDER.compare(this.filePath, other.filePath);
    }

    @Override
    public String toString() {
        return "filePath='" + filePath + '\'' +
                ", frequency= " + frequency +
                ", position=" + position +
                " }";
    }
}

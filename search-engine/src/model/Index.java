package model;

import java.util.Objects;

public class Index {
    private final String word;
    private final String file;
    private final int position;

    public Index(String word, String file, int position) {
        this.word = word;
        this.file = file;
        this.position = position;
    }

    public String getWord() {
        return word;
    }

    public String getFile() {
        return file;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "model.Index{" +
                "word='" + word + '\'' +
                ", file='" + file + '\'' +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return Objects.equals(word, index.word) &&
                Objects.equals(file, index.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, file, position);
    }
}

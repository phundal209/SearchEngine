import java.util.List;

public interface InvertedIndexReader {
    void onLineRead(List<Index> line);
}

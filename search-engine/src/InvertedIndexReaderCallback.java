import java.util.List;

public interface InvertedIndexReaderCallback {
    void onLineRead(List<Index> line);
}

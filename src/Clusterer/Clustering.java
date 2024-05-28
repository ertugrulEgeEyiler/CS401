package Clusterer;

import java.io.IOException;
import java.util.Map;

public abstract class Clustering {

    // Abstract method to be implemented by subclasses for finding and clustering imports
    public abstract void findClusters(String inputFile, String outputFile) throws IOException;

    // Abstract method to be implemented by subclasses for reading imports from a file
    public abstract Map<String, Integer> readImports(String inputFile) throws IOException;

    // Concrete method to write clusters to a file
    public void writeClusters(Map<Integer, StringBuilder> clusters, String outputFile) throws IOException {
        // Implementation should go here
    }
}
package Clusterer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ImportClusterer extends Clustering {

    // Implementation of reading imports from a file
    @Override
    public Map<String, Integer> readImports(String inputFile) throws IOException {
        Map<String, Integer> imports = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            String currentFolder = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.endsWith(" imports:")) {
                    currentFolder = line.replace(" imports:", "");
                } else if (!line.isEmpty()) {
                    imports.put(currentFolder, imports.getOrDefault(currentFolder, 0) + 1);
                }
            }
        }
        return imports;
    }

    // Implementation of finding and clustering imports
    @Override
    public void findClusters(String inputFile, String outputFile) throws IOException {
        try {
            Map<String, Integer> imports = readImports(inputFile);
            Map<Integer, StringBuilder> clusters = clusterImports(imports);
            writeClusters(clusters, outputFile);
            System.out.println("Clustering completed. Results written to " + outputFile);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Private method to cluster imports
    private Map<Integer, StringBuilder> clusterImports(Map<String, Integer> imports) {
        Map<Integer, StringBuilder> clusters = new TreeMap<>((o1, o2) -> Integer.compare(o2, o1)); // Descending order
        Set<String> javaModules = new HashSet<>();
        for (String folder : imports.keySet()) {
            if (folder.contains("java.")) {
                javaModules.add(folder);
            }
        }
        for (Map.Entry<String, Integer> entry : imports.entrySet()) {
            String folder = entry.getKey();
            int count = entry.getValue();
            if (!javaModules.contains(folder)) {
                clusters.computeIfAbsent(count, k -> new StringBuilder()).append(folder).append(";\n");
            }
        }
        System.out.println("Java modules found: " + javaModules);
        return clusters;
    }

    // Overridden method to write clusters to output file with custom numbering
    @Override
    public void writeClusters(Map<Integer, StringBuilder> clusters, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            int folderCount = 1;  // Counter to number the folders correctly
            for (Map.Entry<Integer, StringBuilder> entry : clusters.entrySet()) {
                StringBuilder folders = entry.getValue();
                writer.write(folderCount + ". folder(s):\n");
                writer.write(folders.toString());
                folderCount++;  // Increment the counter for the next folder
            }
        }
    }
}
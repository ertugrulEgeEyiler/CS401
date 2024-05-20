package Clusterer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ImportClusterer extends Clustering {

    @Override
    public Map<String, Integer> readImports(String inputFile) throws IOException {
        Map<String, Integer> imports = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    imports.put(line, imports.getOrDefault(line, 0) + 1);
                }
            }
        }
        return imports;
    }

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

    private Map<Integer, StringBuilder> clusterImports(Map<String, Integer> imports) {
        Map<Integer, StringBuilder> clusters = new TreeMap<>((o1, o2) -> Integer.compare(o2, o1)); // Descending order
        StringBuilder javaImports = new StringBuilder(); // Special builder for java.* imports

        for (Map.Entry<String, Integer> entry : imports.entrySet()) {
            String importItem = entry.getKey();
            int count = entry.getValue();
            if (importItem.startsWith("java.")||
                    importItem.startsWith("Math.")||
                    importItem.startsWith("com.sun")||
                    importItem.startsWith("javax.")||
                    importItem.startsWith("jdk.internal")||
                    importItem.startsWith("META-INF.services.")||
                    importItem.startsWith("sun.")) {
                javaImports.append(importItem).append("\n");
            } else {
                StringBuilder cluster = clusters.computeIfAbsent(count, k -> new StringBuilder());
                cluster.append(importItem).append("\n");
            }
        }

        if (javaImports.length() > 0) {
            clusters.put(-1, javaImports); // Store java imports with a special key to ensure they are last
        }

        return clusters;
    }

    @Override
    public void writeClusters(Map<Integer, StringBuilder> clusters, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            int clusterNumber = 1;  // Counter to number the clusters correctly
            for (Map.Entry<Integer, StringBuilder> entry : clusters.entrySet()) {
                if (entry.getKey() == -1) continue; // Skip java imports for now
                StringBuilder imports = entry.getValue();
                formatImports(writer, imports.toString(), clusterNumber);
                clusterNumber++;  // Increment the counter for the next cluster
            }
            // Now write java imports last
            if (clusters.containsKey(-1)) {
                StringBuilder javaImports = clusters.get(-1);
                formatImports(writer, javaImports.toString(), clusterNumber);
            }
        }
    }

    private void formatImports(FileWriter writer, String imports, int number) throws IOException {
        int new_number = number -1;
        for (String line : imports.split("\n")) {
            if (!line.endsWith(" imports:")) {
                writer.write("contains " + new_number + " "  + line + "\n");
            }
        }
    }
}
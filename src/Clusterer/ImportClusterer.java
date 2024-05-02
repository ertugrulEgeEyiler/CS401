package Clusterer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ImportClusterer extends Clustering{

<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
    private static Map<Integer, StringBuilder> clusterImports(Map<String, Integer> imports) {
=======
    private Map<Integer, StringBuilder> clusterImports(Map<String, Integer> imports) {
>>>>>>> Stashed changes
        Map<Integer, StringBuilder> clusters = new TreeMap<>((o1, o2) -> Integer.compare(o2, o1)); // Descending order
        StringBuilder javaImports = new StringBuilder(); // Special builder for java.* imports

        for (Map.Entry<String, Integer> entry : imports.entrySet()) {
            String importItem = entry.getKey();
            int count = entry.getValue();
            if (importItem.startsWith("java.")) {
                javaImports.append(importItem).append("\n");
            } else {
                StringBuilder cluster = clusters.computeIfAbsent(count, k -> new StringBuilder());
                cluster.append(importItem).append("\n");
            }
        }
<<<<<<< Updated upstream
        System.out.println(javaModules);
        return clusters;
    }


=======

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
                if (entry.getKey() == -1) continue;
                StringBuilder imports = entry.getValue();
                writer.write("Cluster " + clusterNumber + ":\n");
                String value = imports.toString();
                if(value.contains("java")){
                    formatImports(writer, imports.toString());
                }
                clusterNumber++;  // Increment the counter for the next cluster
            }
            // Now write java imports last
            if (clusters.containsKey(-1)) {
                StringBuilder javaImports = clusters.get(-1);
                writer.write("Cluster " + clusterNumber + ":\n");
                formatImports(writer, javaImports.toString());
            }
        }
    }

    private void formatImports(FileWriter writer, String imports) throws IOException {
        for (String line : imports.split("\n")) {
            if (line.endsWith(" imports:")) {
                // Extract the filename up to ".java"
                line = line.substring(0, line.indexOf(".java") + ".java".length());
            }
            writer.write("\t" + line + "\n");
        }
    }
>>>>>>> Stashed changes
}
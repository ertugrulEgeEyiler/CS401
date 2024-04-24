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

public class ImportClusterer extends Clustering{

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

    private static Map<Integer, StringBuilder> clusterImports(Map<String, Integer> imports) {
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
        System.out.println(javaModules);
        return clusters;
    }


}
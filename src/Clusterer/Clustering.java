package Clusterer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Clustering {



    public static void writeClusters(Map<Integer, StringBuilder> clusters, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            for (Map.Entry<Integer, StringBuilder> entry : clusters.entrySet()) {
                int count = entry.getKey();
                StringBuilder folders = entry.getValue();
                writer.write(count + " folder:\n");
                writer.write(folders.toString());
            }
        }
    }

    public void findClusters(String inputFile, String outputFile) throws IOException {

    }

    public static Map<String, Integer> readImports(String inputFile) throws IOException {
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

}

package Clusterer;

import java.io.*;
import java.util.*;

public class KModeClusterer {
    private int numClusters;
    private List<Set<String>> clusters;
    private List<String> modes;
    private Random random;

    public KModeClusterer(int numClusters) {
        this.numClusters = numClusters;
        this.clusters = new ArrayList<>();
        this.modes = new ArrayList<>();
        this.random = new Random();
    }

    public void executeClustering(String inputFile, String outputFile) throws IOException {
        List<String> imports = readImportsFromFile(inputFile);
        initializeModes(imports);

        boolean changed;
        do {
            changed = assignToClusters(imports);
            recalculateModes();
        } while (changed);

        writeClusters(outputFile);
    }

    private List<String> readImportsFromFile(String filePath) throws IOException {
        List<String> imports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().endsWith("imports:")) {
                    imports.add(line.trim());
                }
            }
        }
        return imports;
    }

    private void initializeModes(List<String> imports) {
        for (int i = 0; i < numClusters; i++) {
            int randomIndex = random.nextInt(imports.size());
            modes.add(imports.get(randomIndex));
            clusters.add(new HashSet<>());
        }
    }

    private boolean assignToClusters(List<String> imports) {
        boolean changed = false;
        for (String imp : imports) {
            int bestCluster = findClosestMode(imp);
            if (!clusters.get(bestCluster).contains(imp)) {
                clusters.forEach(cluster -> cluster.remove(imp));
                clusters.get(bestCluster).add(imp);
                changed = true;
            }
        }
        return changed;
    }

    private int findClosestMode(String imp) {
        int bestCluster = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < numClusters; i++) {
            int distance = calculateDistance(imp, modes.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                bestCluster = i;
            }
        }
        return bestCluster;
    }

    private int calculateDistance(String imp, String mode) {
        return imp.equals(mode) ? 0 : 1;
    }

    private void recalculateModes() {
        for (int i = 0; i < clusters.size(); i++) {
            Map<String, Integer> frequency = new HashMap<>();
            clusters.get(i).forEach(imp -> frequency.put(imp, frequency.getOrDefault(imp, 0) + 1));
            String newMode = Collections.max(frequency.entrySet(), Map.Entry.comparingByValue()).getKey();
            modes.set(i, newMode);
        }
    }

    private void writeClusters(String outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < clusters.size(); i++) {
                writer.println("Cluster " + (i + 1) + ":");
                clusters.get(i).forEach(imp -> writer.println("\t" + imp));
            }
        }
    }


}

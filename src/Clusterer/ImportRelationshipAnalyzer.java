package Clusterer;

import java.io.*;
import java.util.*;

    public class ImportRelationshipAnalyzer {
        private Map<String, Map<String, Integer>> importUsage = new HashMap<>();
        private double threshold;
        private Map<String, String> fileToFolderMap = new HashMap<>();
        private Map<String, String> mostUsedTogether = new HashMap<>(); // En çok birlikte kullanılan dosyalar için

        public void analyzeAndPrintClusters(String outputFile) {
            calculateThreshold();
            calculateMostUsedTogether(); // En çok birlikte kullanılanları hesapla

            Map<String, Set<String>> clusters = new HashMap<>();
            Set<String> remainingFiles = new HashSet<>(importUsage.keySet());

            while (!remainingFiles.isEmpty()) {
                String file = remainingFiles.iterator().next();
                Set<String> cluster = new HashSet<>();
                cluster.add(file);
                remainingFiles.remove(file);

                for (String otherFile : new HashSet<>(remainingFiles)) {
                    double similarity = calculateSimilarity(file, otherFile);
                    if (similarity >= threshold || isSameFolder(file, otherFile)) {
                        cluster.add(otherFile);
                        remainingFiles.remove(otherFile);
                    }
                }

                // Yalnız kalan dosyaları en yakın dosyalarla birleştir
                if (cluster.size() == 1 && mostUsedTogether.containsKey(file)) {
                    String closestFile = mostUsedTogether.get(file);
                    // En yakın dosya hala mevcut clusterlar içindeyse, birleştir
                    clusters.values().forEach(cl -> {
                        if (cl.contains(closestFile)) {
                            cl.addAll(cluster);
                        }
                    });
                    cluster.clear(); // Mevcut clusterı temizle, çünkü birleştirildi
                }

                if (!cluster.isEmpty()) {
                    clusters.put(file, cluster);
                }
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                int clusterId = 0;
                for (Set<String> cluster : clusters.values()) {
                    for (String file : cluster) {
                        writer.println("contain " + clusterId + " " + file);
                    }
                    clusterId++;
                }
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }

        private void calculateMostUsedTogether() {
            for (String file1 : importUsage.keySet()) {
                Map<String, Integer> combinedCounts = new HashMap<>();
                for (String file2 : importUsage.keySet()) {
                    if (!file1.equals(file2)) {
                        double similarity = calculateSimilarity(file1, file2);
                        combinedCounts.put(file2, (int) similarity); // Similarity'i sayı olarak kaydet
                    }
                }

                // En yüksek similarity'e sahip dosyayı bul
                String mostUsedFile = Collections.max(combinedCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
                mostUsedTogether.put(file1, mostUsedFile);
            }
        }
    private boolean isSameFolder(String file1, String file2) {
        String folder1 = fileToFolderMap.get(file1);
        String folder2 = fileToFolderMap.get(file2);
        return folder1 != null && folder1.equals(folder2);
    }

    private void calculateThreshold() {
        int totalCount = 0;
        int totalPairs = 0;

        for (Map<String, Integer> fileImport : importUsage.values()) {
            int numImports = fileImport.size();
            totalPairs += numImports * (numImports - 1) / 2; // İmport sayısının kombinasyon sayısı
        }

        for (Map<String, Integer> fileImport : importUsage.values()) {
            for (int count : fileImport.values()) {
                totalCount += count; // Her bir importın toplam kullanım sayısı
            }
        }

        threshold = (double) totalCount / totalPairs;
    }

    private double calculateSimilarity(String file1, String file2) {
        Map<String, Integer> import1 = importUsage.get(file1);
        Map<String, Integer> import2 = importUsage.get(file2);

        double common = 0;
        double total = 0;

        for (Map.Entry<String, Integer> entry : import1.entrySet()) {
            String importName = entry.getKey();
            int count1 = entry.getValue();
            int count2 = import2.getOrDefault(importName, 0);
            common += Math.min(count1, count2);
            total += count1 + count2;
        }

        return total == 0 ? 0 : common / total;
    }

    public void readFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentFile = null;

            while ((line = reader.readLine()) != null) {
                if (line.contains("imports:")) {
                    currentFile = line.substring(0, line.indexOf(" imports:")).trim();
                    importUsage.put(currentFile, new HashMap<>());
                } else if (!line.trim().isEmpty() && currentFile != null) {
                    Map<String, Integer> fileImports = importUsage.get(currentFile);
                    fileImports.put(line.trim(), fileImports.getOrDefault(line.trim(), 0) + 1);
                }

                // Add file to folder mapping
                if (line.contains("Cluster")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String folderName = parts[1].trim();
                        fileToFolderMap.put(currentFile, folderName);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
package Ui.Listener;

import Clusterer.GeneticAlgorithm;
import Clusterer.ImportClusterer;
import Clusterer.ImportRelationshipAnalyzer;
import Clusterer.KModeClusterer;
import Ui.SaveFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;


public class ClusterListener implements ActionListener {
    private JButton button;
    private JFrame frame;
    private JTextArea outputArea;
    private String memoryPath;

    public ClusterListener(String path, JFrame frame, JTextArea outputArea) {
        this.frame = frame;
        this.outputArea = outputArea;
        this.memoryPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Memory";

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String filename;

        if (button.getText().equals("Genetic Algorithm")) {
            filename = "gaAlgorithmCluster.rsf";
            File file = new File(memoryPath + File.separator + filename);
            if (!file.isFile()) {
                generateClusters("gaAlgorithmCluster");
            }
            displayClusterOutput(memoryPath + File.separator + filename, button.getText(), filename);
        } else if (button.getText().equals("Import Clusterer")) {
            filename = "clustered.rsf";
            File file = new File(memoryPath + File.separator + filename);
            if (!file.isFile()) {
                generateClusters("clustered");
            }
            displayClusterOutput(memoryPath + File.separator + filename, button.getText(), filename);
        } else if (button.getText().equals("KMode Clusterer")) {
            filename = "kModesOutput.rsf";
            File file = new File(memoryPath + File.separator + filename);
            if (!file.isFile()) {
                generateClusters("kModesOutput");
            }
            displayClusterOutput(memoryPath + File.separator + filename, button.getText(), filename);
        } else if (button.getText().equals("Import Analyzer Clusterer")) {
            filename = "relationshipOutput.rsf";
            File file = new File(memoryPath + File.separator + filename);
            if (!file.isFile()) {
                generateClusters("relationshipOutput");
            }
            displayClusterOutput(memoryPath + File.separator + filename, button.getText(), filename);
        } else if (button.getText().equals("Matrix Cluster")) {
            filename = "matrixOutput.rsf";
            File file = new File(memoryPath + File.separator + filename);
            if (!file.isFile()) {
                generateClusters("matrixOutput");
            }
            displayClusterOutput(memoryPath + File.separator + filename, button.getText(), filename);
        } else if (button.getText().equals("Parser Output")) {
            try {
                outputArea.read(new BufferedReader(new FileReader(memoryPath + File.separator + "output.txt")), null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to display parser output: " + ex.getMessage());
            }
        } else if (button.getText().equals("Save")) {
            SaveFrame saveFrame = new SaveFrame();
        }
    }

    private void displayClusterOutput(String clusterOutputFile, String name, String outputName) {
        try {
            outputArea.setText("");
            outputArea.append(name + ";" + "\n");
           // outputArea.append("Mojo Score: " + findMojoScore(outputName) + "\n");

            // Use TreeMap with a custom comparator to sort keys numerically
            Map<String, List<String>> clusters = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));

            try (BufferedReader reader = new BufferedReader(new FileReader(clusterOutputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("contain")) {
                        // Use regex to split by one or more spaces
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length >= 3) {
                            String clusterId = parts[1];
                            String item = parts[2];
                            clusters.computeIfAbsent(clusterId, k -> new ArrayList<>()).add(item);
                        }
                    }
                }
            }

            // Iterate through sorted clusters and display
            for (Map.Entry<String, List<String>> entry : clusters.entrySet()) {
                outputArea.append("Cluster " + entry.getKey() + ":\n");
                for (String item : entry.getValue()) {
                    outputArea.append("  - " + item + "\n");
                }
                outputArea.append("\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to display cluster output: " + ex.getMessage());
        }
    }

    private void generateClusters(String name) {
        String outputFile = memoryPath + File.separator + "output.txt";
        if (name.equals("gaAlgorithmCluster")) {
            GeneticAlgorithm gaAlgorithmCluster = new GeneticAlgorithm(10, 2, 50, 0.05);
            String gaAlgorithmClusterFile = memoryPath + File.separator + "gaAlgorithmCluster.rsf";
            try {
                gaAlgorithmCluster.findClusters(outputFile, gaAlgorithmClusterFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to generate cluster: " + ex.getMessage());
            }
        } else if (name.equals("clustered")) {
            ImportClusterer importClusterer = new ImportClusterer();
            String clusteredFile = memoryPath + File.separator + "clustered.rsf";
            try {
                importClusterer.findClusters(outputFile, clusteredFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to generate cluster: " + ex.getMessage());

            }
        } else if (name.equals(("kModesOutput"))) {
            KModeClusterer kModesClusterer = new KModeClusterer(5);
            String kModesOutputFile = memoryPath + File.separator + "kModesOutput.rsf";
            try {
                kModesClusterer.executeClustering(outputFile, kModesOutputFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to generate cluster: " + ex.getMessage());
            }
        } else if (name.equals("relationshipOutput")) {
            String relationshipOutputFile = memoryPath + File.separator + "relationshipOutput.rsf";
            ImportRelationshipAnalyzer analyzer = new ImportRelationshipAnalyzer();
            analyzer.readFile(outputFile);
            analyzer.analyzeAndPrintClusters(relationshipOutputFile);

        } else if (name.equals("matrixOutput")) {
            try {
                String command = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Clusterer" + File.separator + "matrixAlgorithm.exe";
                String pathname = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Clusterer";

                File exeFile = new File(command);
                if (!exeFile.exists()) {
                    JOptionPane.showMessageDialog(null, "Executable not found.");
                    return;
                }
                Process p = Runtime.getRuntime().exec(command, null, new File(pathname));
                p.waitFor();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to generate cluster: " + ex.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int findMojoScore(String name) {
        File directory = new File(memoryPath);
        File[] files = directory.listFiles();
        int mojoScore = 0;
        if (files.length <= 2) {
            return mojoScore;
        }
        String mojoCommand = "java -jar " + System.getProperty("user.dir") + File.separator + "src" + File.separator + "Test" + File.separator + "mojo.jar ";
        String mainClusterPath = memoryPath + File.separator + name + " ";
        Integer[] mojoScores = new Integer[files.length - 2];
        if (files != null) {
            int i = 0;
            for (File file : files) {
                if (!file.getName().equals(name) & !file.getName().equals("output.txt")) {
                    try {
                        String comparedClusterPath = memoryPath + File.separator + file.getName();
                        String command = mojoCommand + mainClusterPath + comparedClusterPath + " -fm";
                        System.out.println(command);
                        System.out.println(comparedClusterPath);
                        Process p = Runtime.getRuntime().exec(command, null);
                        p.waitFor();
                        InputStream is = p.getInputStream();
                        int score = is.read();
                        mojoScores[i] = score;
                    } catch (IOException e) {
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    i++;
                }
            }
            System.out.println(Arrays.toString(mojoScores));
        }
        for (int score : mojoScores)
            mojoScore += score;

        return mojoScore / mojoScores.length;
    }
}
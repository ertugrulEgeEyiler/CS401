package Ui.Listener;

import Clusterer.GeneticAlgorithm;
import Clusterer.ImportClusterer;
import Clusterer.ImportRelationshipAnalyzer;
import Clusterer.KModeClusterer;
import Ui.SaveFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ClusterListener implements ActionListener {
    private JButton button;
    private JFrame frame;
    private JTextArea outputArea; // Add this
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

        if(button.getText().equals("Genetic Algorithm")) {
            filename = "gaAlgorithmCluster.rsf";
            File file = new File(memoryPath + File.separator + filename );
            if(!file.isFile()){
                generateClusters("gaAlgorithmCluster");
            }
            displayClusterOutput(memoryPath + File.separator + filename);
        }
        else if(button.getText().equals("Import Cluster")) {
                filename = "clustered.rsf";
                File file = new File(memoryPath + File.separator + filename );
                if(!file.isFile()){
                    generateClusters("clustered");
                }
            displayClusterOutput(memoryPath + File.separator + filename);
        }
        else if(button.getText().equals("KMode Clusterer")) {
            filename = "kModesOutput.rsf";
            File file = new File(memoryPath + File.separator + filename );
            if(!file.isFile()){
                generateClusters("kModesOutput");
            }
            displayClusterOutput(memoryPath + File.separator + filename);
        }
        else if(button.getText().equals("Import Analyzer Clusterer")) {
            filename = "relationshipOutput.rsf";
            File file = new File(memoryPath + File.separator + filename );
            if(!file.isFile()){
                generateClusters("relationshipOutput");
            }
            displayClusterOutput(memoryPath + File.separator + filename);
        }
        else if(button.getText().equals("Matrix Cluster")) {
            filename = "matrixOutput.rsf";
            File file = new File(memoryPath + File.separator + filename);
            if (!file.isFile()) {
             generateClusters("matrixOutput");
            }
            displayClusterOutput(memoryPath + File.separator + filename);
        }

        else if(button.getText().equals("Save")) {
            frame.dispose();
            SaveFrame saveFrame = new SaveFrame();
        }
    }
    private void displayClusterOutput(String clusterOutputFile) {
        try {
            outputArea.setText("");
            outputArea.append("Cluster Output:\n");
            int mojoScore = 1;   // Will change
            outputArea.append("Mojo Score: " + mojoScore +"\n");
            Map<String, List<String>> clusters = new TreeMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(clusterOutputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("contain")) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 3) {
                            String clusterId = parts[1];
                            String item = parts[2];
                            clusters.computeIfAbsent(clusterId, k -> new ArrayList<>()).add(item);
                        }
                    }
                }
            }
            for (Map.Entry<String, List<String>> entry : clusters.entrySet()) {
                outputArea.append("Cluster " + entry.getKey() + ":\n");
                for (String item : entry.getValue()) {
                    outputArea.append("  - " + item + "\n");
                }
                outputArea.append("\n");
            }
        } catch (IOException ex) {
            outputArea.append("Error reading cluster output file: " + ex.getMessage() + "\n");
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
                throw new RuntimeException(ex);
            }
        } else if (name.equals("clustered")) {
            ImportClusterer importClusterer = new ImportClusterer();
            String clusteredFile = memoryPath + File.separator + "clustered.rsf";
            try {
                importClusterer.findClusters(outputFile, clusteredFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (name.equals(("kModesOutput"))) {
            KModeClusterer kModesClusterer = new KModeClusterer(5);
            String kModesOutputFile = memoryPath + File.separator + "kModesOutput.rsf";
            try {
                kModesClusterer.executeClustering(outputFile, kModesOutputFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (name.equals("relationshipOutput")) {
            String relationshipOutputFile = memoryPath + File.separator + "relationshipOutput.rsf";
            ImportRelationshipAnalyzer analyzer = new ImportRelationshipAnalyzer();
            analyzer.readFile(outputFile);
            analyzer.analyzeAndPrintClusters(relationshipOutputFile);

        }
        else if (name.equals("matrixOutput")){
            try {
                String command = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Clusterer" + File.separator + "matrixAlgorithm.exe";
                String pathname = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Clusterer";

                File exeFile = new File(command);
                if (!exeFile.exists()) {
                    System.err.println("Error: File not found -> " + command);
                    return;
                }

                // AWAIT TARZI BIR SEY EKLIYCEZ CALISMASI SURUYOR
                Runtime.getRuntime().exec(command, null, new File(pathname));
                System.out.println("Program executed successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

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
    private String path;
    private JFrame frame;
    private JTextArea outputArea; // Add this


    public ClusterListener(String path, JFrame frame, JTextArea outputArea) {
        this.path = path;
        this.frame = frame;
        this.outputArea = outputArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String currentPath = System.getProperty("user.dir");
        System.out.println(currentPath);
        String memoryPath = currentPath + File.separator + "src" + File.separator + "Memory";

        if(button.getText().equals("Genetic Algorithm")) {
            displayClusterOutput(memoryPath + File.separator + "gaAlgorithmCluster.rsf");
        }
        if(button.getText().equals("Import Clusterer")) {
            displayClusterOutput(memoryPath + File.separator + "clustered.rsf");
        }
        if(button.getText().equals("KMode Clusterer")) {
            displayClusterOutput(memoryPath + File.separator + "kModesOutput.rsf");
        }
        if(button.getText().equals("Import Analyzer Clusterer")) {
            displayClusterOutput(memoryPath + File.separator + "relationshipOutput.rsf");
        }
        if(button.getText().equals("Save")) {
            frame.dispose();
            SaveFrame saveFrame = new SaveFrame();
        }
    }
    private void displayClusterOutput(String clusterOutputFile) {
        try {
            outputArea.setText("");
            outputArea.append("Cluster Output:\n");
            int mojoScore = 1;   // Will change
           // outputArea.append("Mojo Score: " + mojoScore +"\n");
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

            // Display the clusters in the output area
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

}

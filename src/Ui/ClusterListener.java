package Ui;

import Clusterer.GeneticAlgorithm;
import Clusterer.ImportClusterer;
import Clusterer.ImportRelationshipAnalyzer;
import Clusterer.KModeClusterer;
import Parser.ImportFinder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class ClusterListener implements ActionListener {
    private JButton button;
    private String path;

    public ClusterListener(String path) {
        this.path = path;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String currentPath = System.getProperty("user.dir");
        System.out.println(currentPath);
        String memoryPath = currentPath + File.separator + "src" + File.separator + "Memory";
        String outputFile = memoryPath + File.separator + "output.txt";

        if(button.getText().equals("Genetic Algorithm")) {
            GeneticAlgorithm gaClusterer = new GeneticAlgorithm(10, 2, 50, 0.05);
            String gaAlgorithmClusterFile = memoryPath + File.separator + "gaAlgorithmCluster.rsf";
            try {
                gaClusterer.findClusters(outputFile, gaAlgorithmClusterFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(button.getText().equals("Import Clusterer")) {
            ImportClusterer importClusterer = new ImportClusterer();
            String clusteredFile = memoryPath + File.separator + "clustered.rsf";
            try{
            importClusterer.findClusters(outputFile, clusteredFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(button.getText().equals("KMode Clusterer")) {
            KModeClusterer kModesClusterer = new KModeClusterer(2);
            String kModesOutputFile = memoryPath + File.separator + "kModesOutput.rsf";
            try{
            kModesClusterer.executeClustering(outputFile, kModesOutputFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(button.getText().equals("Import Analyzer Clusterer")) {
            String relationshipOutputFile = memoryPath + File.separator + "relationshipOutput.rsf";
            ImportRelationshipAnalyzer analyzer = new ImportRelationshipAnalyzer();
            analyzer.readFile(outputFile);
            analyzer.analyzeAndPrintClusters(relationshipOutputFile);
            System.out.println("Import clustering completed, results saved to: " + relationshipOutputFile);
        }
    }
}

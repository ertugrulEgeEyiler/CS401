package Ui;

import Clusterer.GeneticAlgorithm;
import Clusterer.ImportClusterer;
import Clusterer.ImportRelationshipAnalyzer;
import Clusterer.KModeClusterer;
import Ui.Listener.ClusterListener;
import Ui.Listener.GraphListener;
import Ui.Listener.SaveListener;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ClusterFrame extends JFrame {
    private String path;
    private String memoryPath;

    public ClusterFrame(String path) {
        this.path = path;
        this.memoryPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Memory";
        generateClusters(memoryPath);
        init();
    }

    private void init() {
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel clusterPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        JTextArea outputArea = new JTextArea();
        ClusterListener clusterListener = new ClusterListener(path,this, outputArea);
        GraphListener graphListener = new GraphListener(outputArea); // Pass the output area

        JButton geneticAlgorithmButton = new JButton("Genetic Algorithm");
        clusterPanel.add(geneticAlgorithmButton);
        geneticAlgorithmButton.addActionListener(clusterListener);

        JButton importClustererButton = new JButton("Import Clusterer");
        clusterPanel.add(importClustererButton);
        importClustererButton.addActionListener(clusterListener);

        JButton kModeClustererButton = new JButton("KMode Clusterer");
        clusterPanel.add(kModeClustererButton);
        kModeClustererButton.addActionListener(clusterListener);

        JButton importAnalyzerButton = new JButton("Import Analyzer Clusterer");
        clusterPanel.add(importAnalyzerButton);
        importAnalyzerButton.addActionListener(clusterListener);

        JButton cluster = new JButton("Ertugrul Cluster");
        clusterPanel.add(cluster);
        cluster.addActionListener(clusterListener);

        JButton graphButton = new JButton("Show Graph");
        graphButton.addActionListener(graphListener);
        buttonPanel.add(graphButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(clusterListener);
        buttonPanel.add(saveButton);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(memoryPath + File.separator + "output.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                outputArea.append(line + "\n");
            }
        }
        catch (IOException e) {
            System.out.println("Error");
        }

        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        outputPanel.add(outputScrollPane);

        clusterPanel.setLayout(new FlowLayout());
        clusterPanel.setBounds(0, 0, 10, 10);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBounds(0, 0, 10, 10);
        outputPanel.setBounds(0, 200, 100, 200);
        outputPanel.setLayout(new GridLayout());

        this.add(clusterPanel);
        this.add(buttonPanel);
        this.add(outputPanel);
        this.setVisible(true);
    }

    private void generateClusters(String memoryPath) {
        String outputFile = memoryPath + File.separator + "output.txt";
        GeneticAlgorithm gaClusterer = new GeneticAlgorithm(10, 2, 50, 0.05);
        String gaAlgorithmClusterFile = memoryPath + File.separator + "gaAlgorithmCluster.rsf";
        try {
            gaClusterer.findClusters(outputFile, gaAlgorithmClusterFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ImportClusterer importClusterer = new ImportClusterer();
        String clusteredFile = memoryPath + File.separator + "clustered.rsf";
        try{
            importClusterer.findClusters(outputFile, clusteredFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        KModeClusterer kModesClusterer = new KModeClusterer(5);
        String kModesOutputFile = memoryPath + File.separator + "kModesOutput.rsf";
        try{
            kModesClusterer.executeClustering(outputFile, kModesOutputFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        String relationshipOutputFile = memoryPath + File.separator + "relationshipOutput.rsf";
        ImportRelationshipAnalyzer analyzer = new ImportRelationshipAnalyzer();
        analyzer.readFile(outputFile);
        analyzer.analyzeAndPrintClusters(relationshipOutputFile);

    }
}

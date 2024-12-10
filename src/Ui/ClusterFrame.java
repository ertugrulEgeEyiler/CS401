package Ui;

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

    public ClusterFrame(String path) {
        this.path = path;
        init();

    }

    private void init() {
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String currentPath = System.getProperty("user.dir");
        String memoryPath = currentPath + File.separator + "src" + File.separator + "Memory";

        JPanel clusterPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        ClusterListener clusterListener = new ClusterListener(path,this);
        GraphListener graphListener = new GraphListener();

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

        JTextArea outputArea = new JTextArea();

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

        clusterPanel.setLayout(new GridLayout());
        clusterPanel.setBounds(0, 0, 900, 50);
        buttonPanel.setLayout(new GridLayout());
        buttonPanel.setBounds(0, 52, 900, 50);
        outputPanel.setBounds(0, 200, 900, 300);
        outputScrollPane.setBounds(0,0,900,100);

        this.add(clusterPanel);
        this.add(buttonPanel);
        this.add(outputPanel);
        this.setVisible(true);


    }
}

package Ui;

import javax.swing.*;
import java.awt.*;

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

        JPanel clusterPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        ClusterListener clusterListener = new ClusterListener(path);

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

        JButton confirmButton = new JButton("Confirm");
        buttonPanel.add(confirmButton);
        JButton graphButton = new JButton("Show Graph");
        buttonPanel.add(graphButton);
        JButton saveButton = new JButton("Save");
        buttonPanel.add(saveButton);

        JButton output = new JButton("Output");
        outputPanel.add(output);

        clusterPanel.setLayout(new GridLayout());
        clusterPanel.setBounds(0, 0, 900, 50);
        buttonPanel.setLayout(new GridLayout());
        buttonPanel.setBounds(0, 52, 900, 50);
        outputPanel.setBounds(0, 200, 900, 300);

        this.add(clusterPanel);
        this.add(buttonPanel);
        this.add(outputPanel);
        this.setVisible(true);

    }
}

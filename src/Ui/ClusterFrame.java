package Ui;

import Ui.Listener.ClusterListener;
import Ui.Listener.GraphListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        init();
    }

    private void init() {
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                File dir = new File(memoryPath);
                for(File file: dir.listFiles()) {
                    file.delete();
                }
            }
        });

        JPanel clusterPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        JTextArea outputArea = new JTextArea();
        ClusterListener clusterListener = new ClusterListener(path, this, outputArea);
        GraphListener graphListener = new GraphListener(outputArea);

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

        JButton matrixButton = new JButton("Matrix Cluster");
        clusterPanel.add(matrixButton);
        matrixButton.addActionListener(clusterListener);

        JButton parserButton = new JButton("Parser Output");
        clusterPanel.add(parserButton);
        parserButton.addActionListener(clusterListener);

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
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to read parser file: " + e.getMessage());
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

}

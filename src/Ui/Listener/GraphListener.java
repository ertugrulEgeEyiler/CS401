package Ui.Listener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphListener implements ActionListener {

    private JTextArea outputArea;

    public GraphListener(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Fetch clustering output
        String clusterOutput = outputArea.getText().trim();

        if (clusterOutput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No clustering output available to generate graph.");
            return;
        }

        // Convert clustering output to Graphviz dot format
        String dotString = generateGraphvizString(clusterOutput);

        try {
            // Save dot string to file
            FileWriter writer = new FileWriter("graph_output.dot");
            writer.write(dotString);
            writer.close();

            // Generate PNG using Graphviz
            Process process = Runtime.getRuntime().exec("dot -Tpng graph_output.dot -o graph_output.png");
            process.waitFor();

            // Show graph image in GUI
            showGraphImage();
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "Error generating graph: " + ex.getMessage());
        }
    }

    private String generateGraphvizString(String clusterOutput) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        sb.append("\tfontname=\"Helvetica,Arial,sans-serif\";\n");
        sb.append("\tnode [fontname=\"Helvetica,Arial,sans-serif\"];\n");
        sb.append("\tedge [fontname=\"Helvetica,Arial,sans-serif\"];\n");

        String[] lines = clusterOutput.split("\n");
        int clusterIndex = 0;
        boolean inCluster = false;
        String previousNode = null;

        Map<String, List<String>> classImports = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/Memory/output.txt"))) {
            String currentClass = null;
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.endsWith("imports:")) {
                    currentClass = line.replace(" imports:", "").trim();
                    classImports.put(currentClass, new ArrayList<>());
                } else if (currentClass != null && !line.isEmpty()) {
                    classImports.get(currentClass).add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Cluster")) {
                if (inCluster) {
                    sb.append("\t}\n");
                }

                sb.append("\tsubgraph cluster_").append(clusterIndex).append(" {\n");
                sb.append("\t\tstyle=filled;\n");
                sb.append("\t\tcolor=lightgrey;\n");
                sb.append("\t\tnode [style=filled,color=white];\n");
                sb.append("\t\tlabel = \"Cluster ").append(clusterIndex).append("\";\n");

                clusterIndex++;
                inCluster = true;
                previousNode = null;
            } else if (!line.isEmpty()) {
                String sanitizedNode = line.replaceAll("[^a-zA-Z0-9_]", "_");
                sanitizedNode = sanitizedNode.replaceFirst("^_+", "");

                sb.append("\t\t").append(sanitizedNode).append(";\n");

                if (previousNode != null) {
                    sb.append("\t\t").append(previousNode).append(" -> ").append(sanitizedNode).append(";\n");
                }

                previousNode = sanitizedNode;
            }
        }

        for (Map.Entry<String, List<String>> entry : classImports.entrySet()) {
            String className = entry.getKey();
            List<String> imports = entry.getValue();

            String sanitizedClassName = className.replaceAll("[^a-zA-Z0-9_]", "_");

            for (String importClass : imports) {
                String sanitizedImport = importClass.replaceAll("[^a-zA-Z0-9_]", "_");
                sb.append("\t").append(sanitizedClassName).append(" -> ").append(sanitizedImport).append(";\n");
            }
        }

        if (inCluster) {
            sb.append("\t}\n");
        }

        sb.append("}");
        return sb.toString();
    }

    private void showGraphImage() {
        ImageIcon icon = new ImageIcon("graph_output.png");

        // Create a popup to display the generated graph
        JFrame frame = new JFrame("Generated Graph");
        frame.setSize(800, 600);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setVisible(true);
    }
}

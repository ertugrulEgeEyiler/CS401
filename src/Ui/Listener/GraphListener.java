package Ui.Listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

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

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Cluster")) {
                // Close previous cluster, if any
                if (inCluster) {
                    sb.append("\t}\n");
                }

                // Start a new cluster
                sb.append("\tsubgraph cluster_").append(clusterIndex).append(" {\n");
                sb.append("\t\tstyle=filled;\n");
                sb.append("\t\tcolor=lightgrey;\n");
                sb.append("\t\tnode [style=filled,color=white];\n");
                sb.append("\t\tlabel = \"Cluster ").append(clusterIndex).append("\";\n");

                clusterIndex++;
                inCluster = true;
                previousNode = null; // Reset the previous node for this cluster
            } else if (!line.isEmpty()) {
                // Sanitize and clean up import name
                String sanitizedNode = line.replaceAll("[^a-zA-Z0-9_]", "_");
                // Remove any leading underscores
                sanitizedNode = sanitizedNode.replaceFirst("^_+", "");

                // Add the node to the cluster
                sb.append("\t\t").append(sanitizedNode).append(";\n");

                // If there's a previous node, connect it to the current node
                if (previousNode != null) {
                    sb.append("\t\t").append(previousNode).append(" -> ").append(sanitizedNode).append(";\n");
                }

                // Set the current node as the previous node for the next iteration
                previousNode = sanitizedNode;
            }
        }

        // Close the last cluster
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

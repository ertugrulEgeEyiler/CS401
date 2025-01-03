package Ui;

import Ui.Listener.SaveListener;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

public class SaveFrame extends JFrame {

    public SaveFrame() {
        init();
    }

    private void init() {
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);

        JPanel pathPanel = new JPanel();
            pathPanel.setBounds(0, 0, 200, 50);
            pathPanel.setLayout(new FlowLayout());

        JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.setBounds(0, 0, 900, 50);

        String[] clusters = {"Genetic Algorithm", "Import Clusterer", "KMode Clusterer", "Import Analyzer Clusterer","Matrix Algorithm"};
        JList clusterList = new JList(clusters);
        clusterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(clusterList);
        pathPanel.add(scrollPane);

        JLabel userLabel = new JLabel("Please select the cluster you want to save, then enter the path you want to save onto.");
        buttonPanel.add(userLabel);
        JLabel label = new JLabel("Path:");
        pathPanel.add(label);
        JTextField pathInput = new JTextField(40);
        pathInput.setBounds(360, 250, 260, 25);
        pathPanel.add(pathInput);
        label.setBounds(330, 250, 80, 25);
        JButton confirm = new JButton("Confirm");
        buttonPanel.add(confirm);
        JButton cancel = new JButton("Cancel");
        buttonPanel.add(cancel);

        SaveListener saveListener = new SaveListener(this,pathInput,clusterList);
        cancel.addActionListener(saveListener);
        confirm.addActionListener(saveListener);

        this.add(pathPanel);
        this.add(buttonPanel);
        this.setLayout(new FlowLayout());
        this.setVisible(true);

    }
}

package Ui;

import Parser.ImportFinder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintWriter;

public class PathListener implements ActionListener {
    private JTextField userInput;
    private ImportFinder importFinder;

    public PathListener(JTextField userInput) {
        this.userInput = userInput;
        importFinder = new ImportFinder();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String path = userInput.getText();
        PrintWriter printWriter = new PrintWriter(System.out, true);
        importFinder.createImports(path, printWriter);
        ClusterFrame clusterFrame = new ClusterFrame();
    }
}